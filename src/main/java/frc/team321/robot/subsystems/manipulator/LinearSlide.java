package frc.team321.robot.subsystems.manipulator;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import frc.team321.robot.Constants;
import frc.team321.robot.RobotMap;
import frc.team321.robot.commands.subsystems.manipulator.UseLinearSlideJoystick;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team321.robot.subsystems.misc.Sensors;

public class LinearSlide extends Subsystem {

    private TalonSRX master, slave;
    private static LinearSlide instance;

    private LinearSlide() {
        master = new TalonSRX(RobotMap.LINEAR_MASTER);

        slave = new TalonSRX(RobotMap.LINEAR_SLAVE);

        master.setNeutralMode(NeutralMode.Brake);
        slave.setNeutralMode(NeutralMode.Brake);
        slave.follow(master);

        master.configOpenloopRamp(Constants.SLIDE_OPEN_LOOP_RAMP, Constants.SLIDE_TIMEOUT_MS);
        slave.configOpenloopRamp(Constants.SLIDE_OPEN_LOOP_RAMP, Constants.SLIDE_TIMEOUT_MS);

        master.configPeakOutputForward(Constants.SLIDE_PEAK_OUTPUT, Constants.SLIDE_TIMEOUT_MS);

        master.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Constants.SLIDE_PID_SLOT_INDEX, Constants.SLIDE_TIMEOUT_MS);

        master.configNominalOutputForward(Constants.SLIDE_NOMINAL_OUTPUT, Constants.SLIDE_TIMEOUT_MS);
        slave.configNominalOutputForward(Constants.SLIDE_NOMINAL_OUTPUT, Constants.SLIDE_TIMEOUT_MS);

        master.selectProfileSlot(Constants.SLIDE_PROFILE_SLOT_INDEX, Constants.SLIDE_PID_SLOT_INDEX);

        master.config_kF(Constants.SLIDE_PID_SLOT_INDEX, Constants.SLIDE_KF, Constants.SLIDE_TIMEOUT_MS);
        slave.config_kF(Constants.SLIDE_PID_SLOT_INDEX, Constants.SLIDE_KF, Constants.SLIDE_TIMEOUT_MS);

        master.config_kP(Constants.SLIDE_PID_SLOT_INDEX, Constants.SLIDE_KP, Constants.SLIDE_TIMEOUT_MS);
        slave.config_kP(Constants.SLIDE_PID_SLOT_INDEX, Constants.SLIDE_KP, Constants.SLIDE_TIMEOUT_MS);

        master.configMotionCruiseVelocity(Constants.SLIDE_MAX_VELOCITY, Constants.SLIDE_TIMEOUT_MS);
        master.configMotionAcceleration(Constants.SLIDE_MAX_ACCELERATION, Constants.SLIDE_TIMEOUT_MS);

        master.configForwardSoftLimitThreshold(Constants.SLIDE_FORWARD_SOFT_LIMIT, Constants.SLIDE_TIMEOUT_MS);
        master.configReverseSoftLimitThreshold(Constants.SLIDE_REVERSE_SOFT_LIMIT, Constants.SLIDE_TIMEOUT_MS);
        master.configForwardSoftLimitEnable(true, Constants.SLIDE_TIMEOUT_MS);
        master.configReverseSoftLimitEnable(true, Constants.SLIDE_TIMEOUT_MS);

        master.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, Constants.SLIDE_TIMEOUT_MS);
        master.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, Constants.SLIDE_TIMEOUT_MS);

        resetEncoder();
    }

    /**
     * Helper method to stop the linear slides
     */
    public void stop() {
        setPower(0);
    }

    /**
     * Sets power to the linear slide
     * @param power The power to set the linear slide at
     */
    public void setPower(double power) {
        if(power == 0) {
            master.set(ControlMode.PercentOutput, 0.05);
        }else {
            master.set(ControlMode.PercentOutput, power);
        }
    }

    /**
     * Gets the linear slide encoder's count
     * @return The linear slide encoder count
     */
    public int getEncoderCount() {
        if (Sensors.getInstance().isLinearSlideAtGround()) {
            this.resetEncoder();
        }

        return master.getSelectedSensorPosition(0);
    }

    /**
     * Resets linear slide's encoder
     */
    public void resetEncoder() {
        master.setSelectedSensorPosition(0, 0, 0);
    }

    /**
     * Checks power against safe conditions to move
     * @param power Power to move at
     * @return whether or not it's safe to move
     */
    public boolean isSafeToMove(double power) {
        return (Sensors.getInstance().isLinearSlideFullyExtended() && power > 0)
                || (Sensors.getInstance().isLinearSlideAtGround() && power < 0);
    }

    public void setRamping(boolean ramp){
        if(ramp){
            master.configOpenloopRamp(Constants.SLIDE_OPEN_LOOP_RAMP, Constants.SLIDE_TIMEOUT_MS);
            slave.configOpenloopRamp(Constants.SLIDE_OPEN_LOOP_RAMP, Constants.SLIDE_TIMEOUT_MS);
        }else{
            master.configOpenloopRamp(0, Constants.SLIDE_TIMEOUT_MS);
            slave.configOpenloopRamp(0, Constants.SLIDE_TIMEOUT_MS);
        }
    }

    public void setPosition(int position){
        master.set(ControlMode.MotionMagic, position);
    }

    public int getError(){
        return master.getClosedLoopError(Constants.SLIDE_PID_SLOT_INDEX);
    }

    public int getTrajectoryPosition(){
        return master.getActiveTrajectoryPosition();
    }

    public int getEncoderVelocity(){
        return master.getSelectedSensorVelocity(Constants.SLIDE_PID_SLOT_INDEX);
    }

    public synchronized static LinearSlide getInstance(){
        if (instance == null) {
            instance = new LinearSlide();
        }

        return instance;
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new UseLinearSlideJoystick());
    }
}