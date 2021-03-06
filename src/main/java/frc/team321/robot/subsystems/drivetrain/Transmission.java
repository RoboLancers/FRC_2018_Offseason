package frc.team321.robot.subsystems.drivetrain;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import frc.team321.robot.Constants;
import frc.team321.robot.utilities.RobotUtil;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import frc.team321.robot.utilities.enums.DrivetrainSide;

/**
 * Transmission represents one side of the drivetrain
 *
 * @see Drivetrain
 */
public class Transmission {

    private TalonSRX master, slave1, slave2;

    /**
     * Initializes all the motors
     *
     * @param side Drivetrain side
     * @param ports The ports of that side of the drivetrain
     */
    Transmission(DrivetrainSide side, int... ports) {
        master = new TalonSRX(ports[0]);
        slave1 = new TalonSRX(ports[1]);
        slave2 = new TalonSRX(ports[2]);

        slave1.follow(master);
        slave2.follow(master);

        master.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, Constants.DRIVETRAIN_PID_SLOT_INDEX, Constants.TIMEOUT_MS);

        if (side == DrivetrainSide.RIGHT) {
            master.setSensorPhase(false);

            master.setInverted(true);
            slave2.setInverted(true);

            master.config_kF(Constants.DRIVETRAIN_PID_SLOT_INDEX, Constants.DRIVETRAIN_RIGHT_KF, Constants.TIMEOUT_MS);
            slave1.config_kF(Constants.DRIVETRAIN_PID_SLOT_INDEX, Constants.DRIVETRAIN_RIGHT_KF, Constants.TIMEOUT_MS);
            slave2.config_kF(Constants.DRIVETRAIN_PID_SLOT_INDEX, Constants.DRIVETRAIN_RIGHT_KF, Constants.TIMEOUT_MS);
        } else {
            slave2.setInverted(true);

            master.config_kF(Constants.DRIVETRAIN_PID_SLOT_INDEX, Constants.DRIVETRAIN_LEFT_KF, Constants.TIMEOUT_MS);
            slave1.config_kF(Constants.DRIVETRAIN_PID_SLOT_INDEX, Constants.DRIVETRAIN_LEFT_KF, Constants.TIMEOUT_MS);
            slave2.config_kF(Constants.DRIVETRAIN_PID_SLOT_INDEX, Constants.DRIVETRAIN_LEFT_KF, Constants.TIMEOUT_MS);
        }

        master.config_kP(Constants.DRIVETRAIN_PID_SLOT_INDEX, Constants.DRIVETRAIN_KP, Constants.TIMEOUT_MS);
        slave1.config_kP(Constants.DRIVETRAIN_PID_SLOT_INDEX, Constants.DRIVETRAIN_KP, Constants.TIMEOUT_MS);
        slave2.config_kP(Constants.DRIVETRAIN_PID_SLOT_INDEX, Constants.DRIVETRAIN_KP, Constants.TIMEOUT_MS);

        master.configPeakCurrentLimit(Constants.DRIVETRAIN_PEAK_CURRENT_LIMIT, Constants.TIMEOUT_MS);
        slave1.configPeakCurrentLimit(Constants.DRIVETRAIN_PEAK_CURRENT_LIMIT, Constants.TIMEOUT_MS);
        slave2.configPeakCurrentLimit(Constants.DRIVETRAIN_PEAK_CURRENT_LIMIT, Constants.TIMEOUT_MS);

        master.configPeakCurrentDuration(Constants.DRIVETRAIN_PEAK_CURRENT_TIME_LIMIT, Constants.TIMEOUT_MS);
        slave1.configPeakCurrentDuration(Constants.DRIVETRAIN_PEAK_CURRENT_TIME_LIMIT, Constants.TIMEOUT_MS);
        slave2.configPeakCurrentDuration(Constants.DRIVETRAIN_PEAK_CURRENT_TIME_LIMIT, Constants.TIMEOUT_MS);

        master.configContinuousCurrentLimit(Constants.DRIVETRAIN_SUSTAINED_CURRENT_LIMIT, Constants.TIMEOUT_MS);
        slave1.configContinuousCurrentLimit(Constants.DRIVETRAIN_SUSTAINED_CURRENT_LIMIT, Constants.TIMEOUT_MS);
        slave2.configContinuousCurrentLimit(Constants.DRIVETRAIN_SUSTAINED_CURRENT_LIMIT, Constants.TIMEOUT_MS);

        master.enableCurrentLimit(true);
        slave1.enableCurrentLimit(true);
        slave2.enableCurrentLimit(true);

        master.configVoltageCompSaturation(Constants.DRIVETRAIN_VOLTAGE_COMPENSATION, Constants.TIMEOUT_MS);
        slave1.configVoltageCompSaturation(Constants.DRIVETRAIN_VOLTAGE_COMPENSATION, Constants.TIMEOUT_MS);
        slave2.configVoltageCompSaturation(Constants.DRIVETRAIN_VOLTAGE_COMPENSATION, Constants.TIMEOUT_MS);

        master.configVoltageMeasurementFilter(Constants.DRIVETRAIN_FILTER_WINDOW_SAMPLE, Constants.TIMEOUT_MS);
        slave1.configVoltageMeasurementFilter(Constants.DRIVETRAIN_FILTER_WINDOW_SAMPLE, Constants.TIMEOUT_MS);
        slave2.configVoltageMeasurementFilter(Constants.DRIVETRAIN_FILTER_WINDOW_SAMPLE, Constants.TIMEOUT_MS);

        master.enableVoltageCompensation(true);
        slave1.enableVoltageCompensation(true);
        slave2.enableVoltageCompensation(true);

        master.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, 10, Constants.TIMEOUT_MS);
        master.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, Constants.TIMEOUT_MS);
    }

    /**
     * Sets brake mode
     *
     * @param mode Mode to set the motors to
     */
    void setMode(NeutralMode mode) {
        master.setNeutralMode(mode);
        slave1.setNeutralMode(mode);
        slave2.setNeutralMode(mode);
    }

    /**
     * Sets power to this side of the drivetrain
     *
     * @param power The power to set it to. Will be checked to make sure it is between
     *              -1 and 1
     */
    void setPower(double power) {
        master.set(ControlMode.PercentOutput, RobotUtil.range(power, 1), DemandType.ArbitraryFeedForward, 0.1);
    }

    /**
     * Gets current encoder value
     *
     * @return Returns the encoder value on the master talon
     */
    public int getEncoderCount() {
        return master.getSelectedSensorPosition(0);
    }

    public int getRawVelocity(){
        return master.getSelectedSensorVelocity(0);
    }

    public double getVelocity(){
        return RobotUtil.encoderTicksToFeets(getRawVelocity()) * 10;
    }

    /**
     * Zero out the encoder
     */
    void resetEncoder() {
        master.setSelectedSensorPosition(0, 0, 0);
    }

    /**
     * Add a ramp rate to prevent instantaneous change in velocity
     *
     * @param rampRate Amount of time in seconds to go from 0 to full power
     */
    void setRampRate(double rampRate) {
        master.configOpenloopRamp(rampRate, 0);
        slave1.configOpenloopRamp(rampRate, 0);
        slave2.configOpenloopRamp(rampRate, 0);
    }

    /**
     * Returns the master Talon
     * @return Returns the master Talon
     */
    public TalonSRX getMaster(){
        return master;
    }
}