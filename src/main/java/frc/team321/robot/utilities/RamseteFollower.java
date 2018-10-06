package frc.team321.robot.utilities;

import frc.team321.robot.Constants;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Trajectory.Segment;

import java.io.File;

@SuppressWarnings("FieldCanBeLocal")
/**
 * Ramsete implementation by Brian for Team 321 based on Aaron's implementation with help from Prateek and all on the FIRST programming discord server
 */
public class RamseteFollower {

    //Should be greater than zero and this increases correction
    private static final double b = 0.18;

    //Should be between zero and one and this increases dampening
    private static final double zeta = 0.7;

    //Holds what segment we are on
    private int segmentIndex;
    private Segment current;

    //The trajectory to follow
    private Trajectory trajectory;

    //The robot's x and y position and angle
    private Odometry odometry;


    //Variable used to calculate linear and angular velocity
    private double lastTheta, nextTheta;
    private double k, thetaError, sinThetaErrorOverThetaError;
    private double desiredAngularVelocity, linearVelocity, angularVelocity;
    private double odometryError;

    //Constants
    private static final double EPSILON = 0.00001;
    private static final double TWO_PI = 2 * Math.PI;

    //Variable for holding velocity for robot to drive on
    private DriveSignal driveSignal;
    private double left, right;

    public RamseteFollower(String trajectoryName){
        trajectory = Pathfinder.readFromFile(new File("/home/lvuser/trajectories/" + trajectoryName + "_source_detailed.traj"));
        segmentIndex = 0;
        odometry = Odometry.INSTANCE;

        driveSignal = new DriveSignal();
    }

    public DriveSignal getNextDriveSignal(){
        if(isFinished()){
            driveSignal.setLeft(0);
            driveSignal.setRight(0);

            return driveSignal;
        }

        left = 0;
        right = 0;

        current = trajectory.get(segmentIndex);

        desiredAngularVelocity = calculateDesiredAngular();

        linearVelocity = calculateLinearVelocity(current.x, current.y, current.heading, current.velocity, desiredAngularVelocity);
        angularVelocity = calculateAngularVelocity(current.x, current.y, current.heading, current.velocity, desiredAngularVelocity);

        left = (-(angularVelocity * Constants.DRIVETRAIN_WHEELBASE) + (2 * linearVelocity)) / 2;
        right = ((angularVelocity * Constants.DRIVETRAIN_WHEELBASE) + (2 * linearVelocity)) / 2;

        driveSignal.setLeft(left);
        driveSignal.setRight(right);

        segmentIndex++;

        return driveSignal;
    }

    private double calculateDesiredAngular(){
        if(segmentIndex < trajectory.length() - 1){
            lastTheta = trajectory.get(segmentIndex).heading;
            nextTheta = trajectory.get(segmentIndex + 1).heading;
            return (nextTheta - lastTheta) / trajectory.get(segmentIndex).dt;
        }else{
            return 0;
        }
    }

    private double calculateLinearVelocity(double desiredX, double desiredY, double desiredTheta, double desiredLinearVelocity, double desiredAngularVelocity){
        k = calculateK(desiredLinearVelocity, desiredAngularVelocity);
        thetaError = boundHalfRadians(desiredTheta - odometry.getTheta());
        odometryError = calculateOdometryError(odometry.getTheta(), desiredX, odometry.getX(), desiredY, odometry.getY());
        return (desiredLinearVelocity * Math.cos(thetaError)) + (k * odometryError);
    }

    private double calculateAngularVelocity(double desiredX, double desiredY, double desiredTheta, double desiredLinearVelocity, double desiredAngularVelocity){
        k = calculateK(desiredLinearVelocity, desiredAngularVelocity);
        thetaError = boundHalfRadians(desiredTheta - odometry.getTheta());

        if(Math.abs(thetaError) < EPSILON){
            //This is for the limit as sin(x)/x approaches zero
            sinThetaErrorOverThetaError = 1;
        }else{
            sinThetaErrorOverThetaError = Math.sin(thetaError)/thetaError;
        }

        odometryError = calculateOdometryError(odometry.getTheta(), desiredX, odometry.getX(), desiredY, odometry.getY());

        return desiredAngularVelocity + (b * desiredLinearVelocity * sinThetaErrorOverThetaError * odometryError) + (k * thetaError);
    }

    private double calculateOdometryError(double theta, double desiredX, double x, double desiredY, double y){
        return (Math.cos(theta) * (desiredX - x)) + (Math.sin(theta) * (desiredY - y));
    }

    private double calculateK(double desiredLinearVelocity, double desiredAngularVelocity){
        return 2 * zeta * Math.sqrt(Math.pow(desiredAngularVelocity, 2) + (b * Math.pow(desiredLinearVelocity, 2)));
    }

    private double boundHalfRadians(double radians){
        while (radians >= Math.PI) radians -= TWO_PI;
        while (radians < -Math.PI) radians += TWO_PI;
        return radians;
    }

    public void setInitialOdometry(){
        odometry.setX(trajectory.get(0).x);
        odometry.setY(trajectory.get(0).y);
        odometry.setTheta(trajectory.get(0).heading);
    }

    public boolean isFinished(){
        return segmentIndex == trajectory.length();
    }
}
