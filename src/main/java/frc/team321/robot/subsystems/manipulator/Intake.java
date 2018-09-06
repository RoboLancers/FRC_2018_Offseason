package frc.team321.robot.subsystems.manipulator;

import static frc.team321.robot.Constants.*;

import frc.team321.robot.commands.subsystems.manipulator.UseIntake;
import frc.team321.robot.utilities.RobotUtil;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;

public class Intake extends Subsystem {

    private TalonSRX intakeLeft, intakeRight;

    Intake() {
        intakeLeft = new TalonSRX(INTAKE_LEFT);
        intakeRight = new TalonSRX(INTAKE_RIGHT);

        intakeLeft.setNeutralMode(NeutralMode.Brake);
        intakeRight.setNeutralMode(NeutralMode.Brake);
    }

    public void setLeft(double power) {
        intakeLeft.set(ControlMode.PercentOutput, RobotUtil.range(power, 1));
    }

    public void setRight(double power) {
        intakeRight.set(ControlMode.PercentOutput, RobotUtil.range(power, 1));
    }

    public void setAll(double power) {
        setLeft(-power);
        setRight(power);
    }

    public void stop(boolean isIntaking) {
        if (isIntaking) {
            setAll(0.2);
        } else {
            setAll(0);
        }
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new UseIntake());
    }
}
