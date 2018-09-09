package frc.team321.robot.commands.autonomous.subroutine;

import com.ctre.phoenix.motion.MotionProfileStatus;
import com.ctre.phoenix.motion.SetValueMotionProfile;
import com.ctre.phoenix.motion.TrajectoryPoint;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.command.Command;
import frc.team321.robot.Robot;
import frc.team321.robot.utilities.RobotUtil;
import frc.team321.robot.utilities.enums.MotionProfileDirection;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;

import static frc.team321.robot.Constants.*;

import java.io.File;

public class MoveOnPath extends Command {
    private TalonSRX left, right;

    private int trajectorySize;

    private Trajectory trajectoryLeft, trajectoryRight;

    private MotionProfileStatus statusLeft, statusRight;
    private static Notifier trajectoryProcessor;

    private boolean isRunning;
    private int direction;

    public MoveOnPath(String name, MotionProfileDirection motionProfileDirection){
        requires(Robot.drivetrain);

        left = Robot.drivetrain.getLeft().getMaster();
        right = Robot.drivetrain.getRight().getMaster();

        if(motionProfileDirection == MotionProfileDirection.BACKWARD){
            direction = -1;
        }else{
            direction = 1;
        }

        trajectoryLeft = Pathfinder.readFromFile(new File("/home/lvuser/trajectories/" + name + "_left_detailed.traj"));
        trajectoryRight = Pathfinder.readFromFile(new File("/home/lvuser/trajectories/" + name + "_right_detailed.traj"));

        if(trajectoryProcessor == null){
            trajectoryProcessor = new Notifier(() -> {
                left.processMotionProfileBuffer();
                right.processMotionProfileBuffer();
            });
        }

        statusLeft = new MotionProfileStatus();
        statusRight = new MotionProfileStatus();

        if(trajectoryLeft != null){
            trajectorySize = trajectoryLeft.length();
        }else{
            trajectorySize = 0;
            end();
        }
    }

    @Override
    protected void initialize(){
        reset();
        configurePID(DRIVETRAIN_MOTION_PROFILE_P, DRIVETRAIN_MOTION_PROFILE_I, DRIVETRAIN_MOTION_PROFILE_D, Robot.drivetrain.getFeedForward());

        left.changeMotionControlFramePeriod(10);
        right.changeMotionControlFramePeriod(10);

        fillTopBuffer();

        trajectoryProcessor.startPeriodic(0.005);
    }

    @Override
    protected void execute(){
        left.getMotionProfileStatus(statusLeft);
        right.getMotionProfileStatus(statusRight);

        if(!isRunning && statusLeft.btmBufferCnt >= 5 && statusRight.btmBufferCnt >= 5){
            setMotionProfileMode(SetValueMotionProfile.Enable);
            isRunning = true;
        }
    }

    @Override
    protected boolean isFinished(){
        //Only finish if we are originally running and if both talons reach their last point

        return isRunning &&
                statusLeft.activePointValid && statusLeft.isLast &&
                statusRight.activePointValid && statusRight.isLast;
    }

    @Override
    protected void end(){
        trajectoryProcessor.stop();

        left.setStatusFramePeriod(StatusFrameEnhanced.Status_1_General, 10, DRIVETRAIN_TIMEOUT_MS);
        right.setStatusFramePeriod(StatusFrameEnhanced.Status_1_General, 10, DRIVETRAIN_TIMEOUT_MS);

        Robot.drivetrain.stop();
    }

    private void fillTopBuffer(){
        for(int i = 0; i < trajectorySize; i++){
            TrajectoryPoint trajectoryPointLeft = new TrajectoryPoint();
            TrajectoryPoint trajectoryPointRight = new TrajectoryPoint();

            double currentPositionLeft = trajectoryLeft.segments[i].position * direction;
            double currentPositionRight = trajectoryLeft.segments[i].position * direction;

            double velocityLeft = trajectoryLeft.segments[i].velocity;
            double velocityRight = trajectoryRight.segments[i].velocity;

            boolean isLastPointLeft = trajectorySize == i + 1;
            boolean isLastPointRight = trajectorySize == i + 1;
            boolean isFirst = i == 0;

            trajectoryPointLeft.position = RobotUtil.feetsToEncoderTicks(currentPositionLeft);
            trajectoryPointRight.position = RobotUtil.feetsToEncoderTicks(currentPositionRight);

            trajectoryPointLeft.velocity = RobotUtil.revsPerMinuteToTicksPerTenth(velocityLeft);
            trajectoryPointRight.velocity = RobotUtil.revsPerMinuteToTicksPerTenth(velocityRight);

            trajectoryPointLeft.profileSlotSelect0 = DRIVETRAIN_PID_SLOT_INDEX;
            trajectoryPointRight.profileSlotSelect0 = DRIVETRAIN_PID_SLOT_INDEX;

            trajectoryPointLeft.timeDur = TrajectoryPoint.TrajectoryDuration.Trajectory_Duration_20ms;
            trajectoryPointRight.timeDur = TrajectoryPoint.TrajectoryDuration.Trajectory_Duration_20ms;

            trajectoryPointLeft.zeroPos = isFirst;
            trajectoryPointRight.zeroPos = isFirst;

            trajectoryPointLeft.isLastPoint = isLastPointLeft;
            trajectoryPointRight.isLastPoint = isLastPointRight;

            left.pushMotionProfileTrajectory(trajectoryPointLeft);
            right.pushMotionProfileTrajectory(trajectoryPointRight);

        }
    }

    private void configurePID(double p, double i, double d, double f) {
        left.config_kP(DRIVETRAIN_PID_SLOT_INDEX, p, DRIVETRAIN_TIMEOUT_MS);
        right.config_kP(DRIVETRAIN_PID_SLOT_INDEX, p, DRIVETRAIN_TIMEOUT_MS);

        left.config_kI(DRIVETRAIN_PID_SLOT_INDEX, i, DRIVETRAIN_TIMEOUT_MS);
        right.config_kI(DRIVETRAIN_PID_SLOT_INDEX, i, DRIVETRAIN_TIMEOUT_MS);

        left.config_kD(DRIVETRAIN_PID_SLOT_INDEX, d, DRIVETRAIN_TIMEOUT_MS);
        right.config_kD(DRIVETRAIN_PID_SLOT_INDEX, d, DRIVETRAIN_TIMEOUT_MS);

        left.config_kF(DRIVETRAIN_PID_SLOT_INDEX, f, DRIVETRAIN_TIMEOUT_MS);
        right.config_kF(DRIVETRAIN_PID_SLOT_INDEX, f, DRIVETRAIN_TIMEOUT_MS);
    }

    private void setMotionProfileMode(SetValueMotionProfile value) {
        left.set(ControlMode.MotionProfile, value.value);
        right.set(ControlMode.MotionProfile, value.value);
    }

    private void reset() {
        // Reset flags and motion profile modes
        isRunning = false;
        setMotionProfileMode(SetValueMotionProfile.Disable);
        Robot.drivetrain.resetEncoders();

        // Clear the trajectory buffer
        left.clearMotionProfileTrajectories();
        right.clearMotionProfileTrajectories();
    }
}
