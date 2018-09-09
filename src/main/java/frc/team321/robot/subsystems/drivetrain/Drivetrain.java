package frc.team321.robot.subsystems.drivetrain;

import static frc.team321.robot.Constants.*;

import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team321.robot.commands.subsystems.drivetrain.UseArcadeDrive;

public class Drivetrain extends Subsystem implements PIDOutput {

    private Transmission leftTransmission, rightTransmission;
    private GearShifter gearShifter;

    public Drivetrain() {
        leftTransmission = new Transmission(false, LEFT_MASTER_MOTOR, LEFT_SLAVE_1, LEFT_SLAVE_2);
        rightTransmission = new Transmission(true, RIGHT_MASTER_MOTOR, RIGHT_SLAVE_1, RIGHT_SLAVE_2);

        gearShifter = new GearShifter();

        this.setMode(NeutralMode.Brake);
        this.resetEncoders();
    }

    public Transmission getLeft() {
        return leftTransmission;
    }

    public Transmission getRight() {
        return rightTransmission;
    }

    public void setLeft(double power) {
        leftTransmission.setPower(power * 0.95);
    }

    public void setRight(double power) {
        rightTransmission.setPower(power);
    }

    public void setMotors(double leftPower, double rightPower) {
        setLeft(leftPower);
        setRight(rightPower);
    }

    public void setAll(double power) {
        setMotors(power, power);
    }

    public void stop() {
        setAll(0);
    }

    public void setMode(NeutralMode mode) {
        leftTransmission.setMode(mode);
        rightTransmission.setMode(mode);
    }

    public void enableRamping(boolean ramp) {
        if (ramp) {
            leftTransmission.setRampRate(0.20);
            rightTransmission.setRampRate(0.20);
        } else {
            leftTransmission.setRampRate(0);
            rightTransmission.setRampRate(0);
        }
    }

    public void resetEncoders() {
        leftTransmission.resetEncoder();
        rightTransmission.resetEncoder();
    }

    public GearShifter getGearShifter() {
        return gearShifter;
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new UseArcadeDrive());
    }

    @Override
    public void pidWrite(double output){
        leftTransmission.setPower(output);
        rightTransmission.setPower(output);
    }

    public double getFeedForward(){
        final double MAX_MOTOR_OUTPUT = 1023;
        final double NATIVE_UNITS_PER_100 = DRIVETRAIN_MAX_RPM / 600 * DRIVETRAIN_ENCODER_TICKS_PER_REVOLUTION;
        return MAX_MOTOR_OUTPUT/NATIVE_UNITS_PER_100;
    }
}