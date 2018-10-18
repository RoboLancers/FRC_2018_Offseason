package frc.team321.robot.subsystems.manipulator;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import frc.team321.robot.RobotMap;
import frc.team321.robot.commands.subsystems.manipulator.UseIntake;
import frc.team321.robot.commands.subsystems.manipulator.UseIntakeJoystick;
import frc.team321.robot.utilities.RobotUtil;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Intake extends Subsystem {

    private TalonSRX intakeLeft, intakeRight;
    private static Intake instance;

    private Intake() {
        intakeLeft = new TalonSRX(RobotMap.INTAKE_LEFT);
        intakeRight = new TalonSRX(RobotMap.INTAKE_RIGHT);

        intakeLeft.setNeutralMode(NeutralMode.Brake);
        intakeRight.setNeutralMode(NeutralMode.Brake);
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

    public synchronized static Intake getInstance(){
        if (instance == null) {
            instance = new Intake();
        }

        return instance;
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new UseIntakeJoystick());
    }
}
