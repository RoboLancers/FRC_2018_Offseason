package frc.team321.robot.subsystems.drivetrain;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team321.robot.Constants;
import frc.team321.robot.commands.subsystems.drivetrain.UseArcadeDrive;
import frc.team321.robot.utilities.RobotUtil;
import frc.team321.robot.RobotMap;

public class Drivetrain extends Subsystem{

    private Transmission leftTransmission, rightTransmission;

    private static Drivetrain instance;

    private Drivetrain() {
        leftTransmission = new Transmission(false, RobotMap.LEFT_MASTER_MOTOR, RobotMap.LEFT_SLAVE_1, RobotMap.LEFT_SLAVE_2);
        rightTransmission = new Transmission(true, RobotMap.RIGHT_MASTER_MOTOR, RobotMap.RIGHT_SLAVE_1, RobotMap.RIGHT_SLAVE_2);

        this.setMode(NeutralMode.Brake);
        this.resetEncoders();

        GearShifter.getInstance();
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

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new UseArcadeDrive());
    }

    public void setVelocity(double left, double right){
        leftTransmission.getMaster().set(ControlMode.Velocity, RobotUtil.feetsToEncoderTicks(left)/10);
        rightTransmission.getMaster().set(ControlMode.Velocity, RobotUtil.feetsToEncoderTicks(right)/10);
    }

    public synchronized static Drivetrain getInstance() {
        if (instance == null) {
            instance = new Drivetrain();
        }

        return instance;
    }
}