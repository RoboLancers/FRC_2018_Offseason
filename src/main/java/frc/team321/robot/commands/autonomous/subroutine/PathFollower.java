package frc.team321.robot.commands.autonomous.subroutine;

import static frc.team321.robot.Constants.*;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team321.robot.Constants;
import frc.team321.robot.OI;
import frc.team321.robot.subsystems.drivetrain.Drivetrain;
import frc.team321.robot.subsystems.misc.Sensors;
import frc.team321.robot.utilities.motion.Odometry;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.followers.EncoderFollower;

import java.io.File;

@SuppressWarnings("FieldCanBeLocal")
public class PathFollower extends Command{

    private EncoderFollower leftEncoderFollower, rightEncoderFollower;
    private double leftPower, rightPower;
    private double gyroHeading, desiredHeading, angleDifference, turn;
    private Odometry odometry;
    private Trajectory leftTrajectory, rightTrajectory;

    public PathFollower(String name){
        requires(Drivetrain.getInstance());

        leftTrajectory = Pathfinder.readFromFile(new File("/home/lvuser/trajectories/" + name + "/" + name + "_left_detailed.traj"));
        rightTrajectory = Pathfinder.readFromFile(new File("/home/lvuser/trajectories/" + name + "/" + name + "_right_detailed.traj"));

        leftEncoderFollower = new EncoderFollower(leftTrajectory);
        rightEncoderFollower = new EncoderFollower(rightTrajectory);

        leftEncoderFollower.configureEncoder(Drivetrain.getInstance().getLeft().getEncoderCount(), Constants.DRIVETRAIN_ENCODER_TICKS_PER_REVOLUTION, Constants.DRIVETRAIN_WHEEL_DIAMETER_FEETS);
        rightEncoderFollower.configureEncoder(Drivetrain.getInstance().getRight().getEncoderCount(), Constants.DRIVETRAIN_ENCODER_TICKS_PER_REVOLUTION, Constants.DRIVETRAIN_WHEEL_DIAMETER_FEETS);

        leftEncoderFollower.configurePIDVA(DRIVETRAIN_MOTION_PROFILE_P, DRIVETRAIN_MOTION_PROFILE_I, DRIVETRAIN_MOTION_PROFILE_D, DRIVETRAIN_KV, DRIVETRAIN_KA);
        rightEncoderFollower.configurePIDVA(DRIVETRAIN_MOTION_PROFILE_P, DRIVETRAIN_MOTION_PROFILE_I, DRIVETRAIN_MOTION_PROFILE_D, DRIVETRAIN_KV, DRIVETRAIN_KA);
        odometry = Odometry.getInstance();
    }

    @Override
    protected void initialize(){
        Drivetrain.getInstance().setMode(NeutralMode.Coast);
        Drivetrain.getInstance().stop();
        Sensors.getInstance().resetNavX();

        OI.liveDashboardTable.getEntry("Reset").setBoolean(true);
        setInitialOdometry();
    }

    @Override
    protected void execute(){
        leftPower = leftEncoderFollower.calculate(Drivetrain.getInstance().getLeft().getEncoderCount());
        rightPower = rightEncoderFollower.calculate(Drivetrain.getInstance().getRight().getEncoderCount());

        if(!leftEncoderFollower.isFinished() && !rightEncoderFollower.isFinished()){
            OI.liveDashboardTable.getEntry("Path X").setNumber((leftEncoderFollower.getSegment().x + rightEncoderFollower.getSegment().x) / 2);
            OI.liveDashboardTable.getEntry("Path Y").setNumber((leftEncoderFollower.getSegment().y + rightEncoderFollower.getSegment().y) / 2);
        }

        gyroHeading = Sensors.getInstance().getAngle();
        desiredHeading = Pathfinder.r2d(leftEncoderFollower.getHeading());

        angleDifference = Pathfinder.boundHalfDegrees(desiredHeading - gyroHeading);
        turn = DRIVETRAIN_ROTATE_P * -angleDifference;

        SmartDashboard.putNumber("Turn error", turn);
        SmartDashboard.putNumber("Desired Heading", desiredHeading);
        SmartDashboard.putNumber("Left Power", leftPower);

        Drivetrain.getInstance().setLeft(leftPower + turn);
        Drivetrain.getInstance().setRight(rightPower - turn);
    }

    @Override
    protected boolean isFinished() {
        return leftEncoderFollower.isFinished() && rightEncoderFollower.isFinished();
    }

    @Override
    protected void end(){
        Drivetrain.getInstance().stop();
    }

    private void setInitialOdometry(){
        odometry.setX((leftTrajectory.get(0).x + rightTrajectory.get(0).x) / 2);
        odometry.setY((leftTrajectory.get(0).y + rightTrajectory.get(0).y) / 2);
        odometry.setTheta(leftTrajectory.get(0).heading);
    }
}
