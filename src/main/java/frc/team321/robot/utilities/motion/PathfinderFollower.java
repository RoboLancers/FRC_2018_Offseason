package frc.team321.robot.utilities.motion;

import edu.wpi.first.wpilibj.command.Command;
import frc.team321.robot.Constants;
import frc.team321.robot.subsystems.drivetrain.Drivetrain;
import frc.team321.robot.subsystems.misc.Sensors;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.followers.EncoderFollower;

import java.io.File;

import static frc.team321.robot.Constants.*;

@SuppressWarnings("FieldCanBeLocal")
public class PathfinderFollower{

    private EncoderFollower leftEncoderFollower, rightEncoderFollower;
    private double leftPower, rightPower;
    private double gyroHeading, desiredHeading, angleDifference, turn;
    private Odometry odometry;
    private Trajectory leftTrajectory, rightTrajectory;
    private DriveSignal driveSignal;

    public PathfinderFollower(String trajectoryName){
        leftTrajectory = Pathfinder.readFromFile(new File("/home/lvuser/trajectories/" + trajectoryName + "/" + trajectoryName + "_left_detailed.traj"));
        rightTrajectory = Pathfinder.readFromFile(new File("/home/lvuser/trajectories/" + trajectoryName + "/" + trajectoryName + "_right_detailed.traj"));

        leftEncoderFollower = new EncoderFollower(leftTrajectory);
        rightEncoderFollower = new EncoderFollower(rightTrajectory);

        leftEncoderFollower.configureEncoder(Drivetrain.getInstance().getLeft().getEncoderCount(), Constants.DRIVETRAIN_ENCODER_TICKS_PER_REVOLUTION, Constants.DRIVETRAIN_WHEEL_DIAMETER_FEETS);
        rightEncoderFollower.configureEncoder(Drivetrain.getInstance().getRight().getEncoderCount(), Constants.DRIVETRAIN_ENCODER_TICKS_PER_REVOLUTION, Constants.DRIVETRAIN_WHEEL_DIAMETER_FEETS);

        leftEncoderFollower.configurePIDVA(DRIVETRAIN_MOTION_PROFILE_P, DRIVETRAIN_MOTION_PROFILE_I, DRIVETRAIN_MOTION_PROFILE_D, DRIVETRAIN_KV, DRIVETRAIN_KA);
        rightEncoderFollower.configurePIDVA(DRIVETRAIN_MOTION_PROFILE_P, DRIVETRAIN_MOTION_PROFILE_I, DRIVETRAIN_MOTION_PROFILE_D, DRIVETRAIN_KV, DRIVETRAIN_KA);
        odometry = Odometry.getInstance();

        driveSignal = new DriveSignal();
    }

    public DriveSignal getNextDriveSignal(){
        leftPower = leftEncoderFollower.calculate(Drivetrain.getInstance().getLeft().getEncoderCount());
        rightPower = rightEncoderFollower.calculate(Drivetrain.getInstance().getRight().getEncoderCount());

        gyroHeading = Sensors.getInstance().getAngle();
        desiredHeading = Pathfinder.r2d(leftEncoderFollower.getHeading());

        angleDifference = Pathfinder.boundHalfDegrees(desiredHeading - gyroHeading);
        turn = DRIVETRAIN_ROTATE_P * -angleDifference;

        driveSignal.setLeft(leftPower + turn);
        driveSignal.setRight(rightPower - turn);

        return driveSignal;
    }

    public void setInitialOdometry(){
        odometry.setX((leftTrajectory.get(0).x + rightTrajectory.get(0).x) / 2);
        odometry.setY((leftTrajectory.get(0).y + rightTrajectory.get(0).y) / 2);
        odometry.setTheta(leftTrajectory.get(0).heading);
    }

    public EncoderFollower getLeftEncoderFollower() {
        return leftEncoderFollower;
    }

    public EncoderFollower getRightEncoderFollower() {
        return rightEncoderFollower;
    }

    public boolean isFinished(){
        return leftEncoderFollower.isFinished() && rightEncoderFollower.isFinished();
    }
}
