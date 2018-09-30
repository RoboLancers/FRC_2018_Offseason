package frc.team321.robot.subsystems.manipulator;

import static frc.team321.robot.Constants.*;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import frc.team321.robot.commands.subsystems.manipulator.UseIntake;
import frc.team321.robot.utilities.RobotUtil;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.wpilibj.command.Subsystem;

public class Intake extends Subsystem {

    private WPI_TalonSRX intakeLeft, intakeRight;

    Intake() {
        intakeLeft = new WPI_TalonSRX(INTAKE_LEFT);
        intakeRight = new WPI_TalonSRX(INTAKE_RIGHT);

        intakeLeft.setNeutralMode(NeutralMode.Brake);
        intakeRight.setNeutralMode(NeutralMode.Brake);

        intakeLeft.setInverted(true);
    }

    private void setLeft(double power) {
        intakeLeft.set(ControlMode.PercentOutput, RobotUtil.range(power, 1));
    }

    private void setRight(double power) {
        intakeRight.set(ControlMode.PercentOutput, RobotUtil.range(power, 1));
    }

    public void setAll(double power) {
        setLeft(power);
        setRight(power);
    }

    public void stop() {
        setAll(0);
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new UseIntake());
    }
}
