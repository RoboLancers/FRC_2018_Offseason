package frc.team321.robot.subsystems.drivetrain;

import static frc.team321.robot.Constants.*;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team321.robot.commands.subsystems.drivetrain.UseArcadeDrive;
import frc.team321.robot.subsystems.misc.Sensors;
import frc.team321.robot.utilities.Odometry;
import frc.team321.robot.utilities.RobotUtil;
import jaci.pathfinder.Pathfinder;

public class Drivetrain extends Subsystem{

    private Transmission leftTransmission, rightTransmission;
    private Odometry odometry;
    private GearShifter gearShifter;

    private static Drivetrain instance;

    private Drivetrain() {
        leftTransmission = new Transmission(false, LEFT_MASTER_MOTOR, LEFT_SLAVE_1, LEFT_SLAVE_2);
        rightTransmission = new Transmission(true, RIGHT_MASTER_MOTOR, RIGHT_SLAVE_1, RIGHT_SLAVE_2);

        this.setMode(NeutralMode.Brake);
        this.resetEncoders();

        odometry = Odometry.INSTANCE;
        gearShifter = GearShifter.getInstance();

        Notifier odometryNotifier = new Notifier(() -> {
            odometry.setCurrentEncoderPosition(leftTransmission.getEncoderCount() + rightTransmission.getEncoderCount() / 2.0);
            odometry.setDeltaPosition(RobotUtil.encoderTicksToFeets(odometry.getCurrentEncoderPosition() - odometry.getLastPosition()));
            odometry.setTheta(Math.toRadians(Pathfinder.boundHalfDegrees(Sensors.getAngle())));

            odometry.addX(Math.cos(odometry.getTheta()) * odometry.getDeltaPosition());
            odometry.addY(Math.sin(odometry.getTheta()) * odometry.getDeltaPosition());

            odometry.setLastPosition(odometry.getCurrentEncoderPosition());
        });

        odometryNotifier.startPeriodic(0.01);
    }

    /**
     * Gets left transmission
     * @return the left transmission
     */
    public Transmission getLeft() {
        return leftTransmission;
    }

    /**
     * Gets right transmission
     * @return the right transmission
     */
    public Transmission getRight() {
        return rightTransmission;
    }

    /**
     * Sets left transmission's power
     * @param power The power to set the left transmissions
     */
    public void setLeft(double power) {
        leftTransmission.setPower(power);
    }

    /**
     * Sets right transmission's power
     * @param power The power to set the left transmission
     */
    public void setRight(double power) {
        rightTransmission.setPower(power);
    }

    /**
     * Helper method to set both left and right power
     * @param leftPower The power to set left transmission
     * @param rightPower The power to set right transmission
     */
    public void setMotors(double leftPower, double rightPower) {
        setLeft(leftPower);
        setRight(rightPower);
    }

    /**
     * Helper method to set both transmissions
     * @param power The power to set both transmissions at
     */
    public void setAll(double power) {
        setMotors(power, power);
    }

    /**
     * Helper method to stop the drivetrain
     */
    public void stop() {
        setAll(0);
    }

    /**
     * Sets the brake mode on the transmissions
     * @param mode The mode to set the transmissions at
     */
    public void setMode(NeutralMode mode) {
        leftTransmission.setMode(mode);
        rightTransmission.setMode(mode);
    }

    /**
     * Enable/Disable ramping on the drivetrain
     * @param ramp Whether or not the drivetrain should have ramping
     */
    public void enableRamping(boolean ramp) {
        if (ramp) {
            leftTransmission.setRampRate(0.30);
            rightTransmission.setRampRate(0.30);
        } else {
            leftTransmission.setRampRate(0);
            rightTransmission.setRampRate(0);
        }
    }

    /**
     * Resets drivetrain's encoders
     */
    public void resetEncoders() {
        leftTransmission.resetEncoder();
        rightTransmission.resetEncoder();
    }

    public GearShifter getGearShifter(){
        return gearShifter;
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new UseArcadeDrive());
    }

    /**
     * Theoretically calculates and returns the FeedForward
     * @return the FeedForward of our robot
     */
    public double getFeedForward(){
        final double MAX_MOTOR_OUTPUT = 1023;
        final double NATIVE_UNITS_PER_100 = DRIVETRAIN_MAX_RPM / 600 * DRIVETRAIN_ENCODER_TICKS_PER_REVOLUTION;
        return MAX_MOTOR_OUTPUT/NATIVE_UNITS_PER_100;
    }

    public void setVelocity(double left, double right){
        leftTransmission.getMaster().set(ControlMode.Velocity, RobotUtil.feetsToEncoderTicks(left)/10);
        rightTransmission.getMaster().set(ControlMode.Velocity, RobotUtil.feetsToEncoderTicks(left)/10);
    }

    public synchronized static Drivetrain getInstance() {
        if (instance == null) {
            instance = new Drivetrain();
        }

        return instance;
    }
}