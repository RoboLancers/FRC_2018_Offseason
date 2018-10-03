package frc.team321.robot.commands.autonomous.subroutine;

import static frc.team321.robot.Constants.*;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team321.robot.Constants;
import frc.team321.robot.Robot;
import frc.team321.robot.subsystems.misc.Sensors;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.followers.EncoderFollower;

import java.io.File;

@SuppressWarnings("FieldCanBeLocal")
public class PathFollower extends Command{

    private EncoderFollower leftEncoderFollower, rightEncoderFollower;
    private double leftPower, rightPower;
    private double gyroHeading, desiredHeading, angleDifference, turn;

    public PathFollower(String name){
        requires(Robot.drivetrain);

        Trajectory leftTrajectory = Pathfinder.readFromFile(new File("/home/lvuser/trajectories/" + name + "_left_detailed.traj"));
        Trajectory rightTrajectory = Pathfinder.readFromFile(new File("/home/lvuser/trajectories/" + name + "_right_detailed.traj"));

        leftEncoderFollower = new EncoderFollower(leftTrajectory);
        rightEncoderFollower = new EncoderFollower(rightTrajectory);

        leftEncoderFollower.configureEncoder(Robot.drivetrain.getLeft().getEncoderCount(), Constants.DRIVETRAIN_ENCODER_TICKS_PER_REVOLUTION, Constants.DRIVETRAIN_WHEEL_DIAMETER_INCHES/12);
        rightEncoderFollower.configureEncoder(Robot.drivetrain.getRight().getEncoderCount(), Constants.DRIVETRAIN_ENCODER_TICKS_PER_REVOLUTION, Constants.DRIVETRAIN_WHEEL_DIAMETER_INCHES/12);

        leftEncoderFollower.configurePIDVA(DRIVETRAIN_MOTION_PROFILE_P, DRIVETRAIN_MOTION_PROFILE_I, DRIVETRAIN_MOTION_PROFILE_D, DRIVETRAIN_KV, DRIVETRAIN_KA);
        rightEncoderFollower.configurePIDVA(DRIVETRAIN_MOTION_PROFILE_P, DRIVETRAIN_MOTION_PROFILE_I, DRIVETRAIN_MOTION_PROFILE_D, DRIVETRAIN_KV, DRIVETRAIN_KA);
    }

    @Override
    protected void initialize(){
        Robot.drivetrain.setMode(NeutralMode.Coast);
        Robot.drivetrain.stop();
        Sensors.resetNavX();
    }

    @Override
    protected void execute(){
        leftPower = leftEncoderFollower.calculate(Robot.drivetrain.getLeft().getEncoderCount());
        rightPower = rightEncoderFollower.calculate(Robot.drivetrain.getRight().getEncoderCount());

        gyroHeading = Sensors.getAngle();
        desiredHeading = Pathfinder.r2d(leftEncoderFollower.getHeading());

        angleDifference = Pathfinder.boundHalfDegrees(desiredHeading - gyroHeading);
        turn = DRIVETRAIN_ROTATE_P * -angleDifference;

        SmartDashboard.putNumber("Turn error", turn);
        SmartDashboard.putNumber("Desired Heading", desiredHeading);
        SmartDashboard.putNumber("Left Power", leftPower);

        Robot.drivetrain.setLeft(leftPower + turn);
        Robot.drivetrain.setRight(rightPower - turn);
    }

    @Override
    protected boolean isFinished() {
        return leftEncoderFollower.isFinished() && rightEncoderFollower.isFinished();
    }

    @Override
    protected void end(){
        Robot.drivetrain.stop();
    }

    @Override
    protected void interrupted(){
        end();
    }
}
