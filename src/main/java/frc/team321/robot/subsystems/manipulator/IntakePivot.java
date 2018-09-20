package frc.team321.robot.subsystems.manipulator;

import static frc.team321.robot.Constants.*;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team321.robot.commands.subsystems.manipulator.UseIntakePivot;

public class IntakePivot extends Subsystem{

    private DoubleSolenoid intakePivot;

    IntakePivot(){
        intakePivot = new DoubleSolenoid(INTAKE_PIVOT_FORWARD, INTAKE_PIVOT_REVERSE);
        intakePivot.set(DoubleSolenoid.Value.kReverse);
    }

    public void setUp() {
        intakePivot.set(DoubleSolenoid.Value.kReverse);
    }

    public void setDown() {
        intakePivot.set(DoubleSolenoid.Value.kForward);
    }

    public boolean isIntakeUp() {
        return intakePivot.get() == DoubleSolenoid.Value.kReverse;
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new UseIntakePivot());
    }
}