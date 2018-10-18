package frc.team321.robot.utilities.motion;

import frc.team321.robot.Constants;
import frc.team321.robot.OI;
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
    private double b = 0.18;

    //Should be between zero and one and this increases dampening
    private double zeta = 0.7;

    //Holds what segment we are on
    private int segmentIndex;
    private Segment current;

    //The trajectory to follow
    private Trajectory trajectory;

    //Variable used to calculate linear and angular velocity
    private double lastTheta, nextTheta;
    private double k, thetaError, sinThetaErrorOverThetaError;
    private double desiredAngularVelocity, linearVelocity, angularVelocity;
    private double odometryError;

    //Constants
    private static final double EPSILON = 0.00000001;
    private static final double TWO_PI = 2 * Math.PI;

    //Variable for holding velocity for robot to drive on
    private Velocity velocity;
    private DriveSignal driveSignal;
    private double left, right;

    //Trajectory file to open
    private File trajectoryFile;

    public RamseteFollower(String trajectoryName){
        trajectoryFile = new File("/home/lvuser/trajectories/" + trajectoryName + "/" + trajectoryName + "_left_detailed.traj");

        trajectory = trajectoryFile.exists() ? Pathfinder.readFromFile(trajectoryFile) : null;

        if(trajectory == null){
            trajectoryFile = new File("C:\\Users\\brian\\OneDrive\\Projects\\FRC_2018_Offseason\\PathPlanner\\Trajectories\\" + trajectoryName + "\\" + trajectoryName + "_source_detailed.traj");
            trajectory = trajectoryFile.exists() ? Pathfinder.readFromFile(trajectoryFile): null;
        }

        if(trajectory == null){
            return;
        }

        segmentIndex = 0;

        driveSignal = new DriveSignal();
    }

    public RamseteFollower(String trajectoryName, double b, double zeta){
        this(trajectoryName);

        this.b = b;
        this.zeta = zeta;
    }

    public Velocity getVelocity(){
        if(isFinished()){
            return new Velocity(0, 0);
        }

        current = trajectory.get(segmentIndex);

        desiredAngularVelocity = calculateDesiredAngular();

        linearVelocity = calculateLinearVelocity(current.x, current.y, current.heading, current.velocity, desiredAngularVelocity);
        angularVelocity = calculateAngularVelocity(current.x, current.y, current.heading, current.velocity, desiredAngularVelocity);

        segmentIndex++;

        return new Velocity(linearVelocity, angularVelocity);
    }

    public DriveSignal getNextDriveSignal(){
        if(isFinished()){
            driveSignal.setLeft(0);
            driveSignal.setRight(0);

            return driveSignal;
        }

        left = 0;
        right = 0;

        velocity = getVelocity();

        left = (-(velocity.getAngular() * Constants.DRIVETRAIN_WHEELBASE) + (2 * velocity.getLinear())) / 2;
        right = ((velocity.getAngular() * Constants.DRIVETRAIN_WHEELBASE) + (2 * velocity.getLinear())) / 2;

        driveSignal.setLeft(left);
        driveSignal.setRight(right);

        OI.liveDashboardTable.getEntry("Path X").setNumber(current.x);
        OI.liveDashboardTable.getEntry("Path Y").setNumber(current.y);

        segmentIndex++;

        return driveSignal;
    }

    private double calculateDesiredAngular(){
        if(segmentIndex < trajectory.length() - 1){
            lastTheta = trajectory.get(segmentIndex).heading;
            nextTheta = trajectory.get(segmentIndex + 1).heading;
            return boundHalfRadians(nextTheta - lastTheta) / current.dt;
        }else{
            return 0;
        }
    }

    private double calculateLinearVelocity(double desiredX, double desiredY, double desiredTheta, double desiredLinearVelocity, double desiredAngularVelocity){
        k = calculateK(desiredLinearVelocity, desiredAngularVelocity);
        thetaError = boundHalfRadians(desiredTheta - Odometry.getInstance().getTheta());
        odometryError = (Math.cos(Odometry.getInstance().getTheta()) * (desiredX - Odometry.getInstance().getX())) + (Math.sin(Odometry.getInstance().getTheta()) * (desiredY - Odometry.getInstance().getY()));
        return (desiredLinearVelocity * Math.cos(thetaError)) + (k * odometryError);
    }

    private double calculateAngularVelocity(double desiredX, double desiredY, double desiredTheta, double desiredLinearVelocity, double desiredAngularVelocity){
        k = calculateK(desiredLinearVelocity, desiredAngularVelocity);
        thetaError = boundHalfRadians(desiredTheta - Odometry.getInstance().getTheta());

        if(Math.abs(thetaError) < EPSILON){
            //This is for the limit as sin(x)/x approaches zero
            sinThetaErrorOverThetaError = 1;
        }else{
            sinThetaErrorOverThetaError = Math.sin(thetaError)/thetaError;
        }

        odometryError = (Math.cos(Odometry.getInstance().getTheta()) * (desiredY - Odometry.getInstance().getY())) - (Math.sin(Odometry.getInstance().getTheta()) * (desiredX - Odometry.getInstance().getX()));

        return desiredAngularVelocity + (b * desiredLinearVelocity * sinThetaErrorOverThetaError * odometryError) + (k * thetaError);
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
        Odometry.getInstance().setX(trajectory.get(0).x);
        Odometry.getInstance().setY(trajectory.get(0).y);
        Odometry.getInstance().setTheta(trajectory.get(0).heading);
    }

    public Segment currentSegment(){
        return current;
    }

    public boolean isFinished(){
        return segmentIndex == trajectory.length();
    }
}
