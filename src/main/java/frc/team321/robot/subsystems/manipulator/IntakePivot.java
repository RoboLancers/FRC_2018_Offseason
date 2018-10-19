package frc.team321.robot.subsystems.manipulator;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team321.robot.RobotMap;

public class IntakePivot extends Subsystem{

    private DoubleSolenoid intakePivot;
    private static IntakePivot instance;

    private IntakePivot(){
        intakePivot = new DoubleSolenoid(RobotMap.INTAKE_PIVOT_FORWARD, RobotMap.INTAKE_PIVOT_REVERSE);
    }

    /**
     * Sets intake up
     */
    public void setUp() {
        intakePivot.set(DoubleSolenoid.Value.kForward);
    }

    /**
     * Sets intake down
     */
    public void setDown() {
        intakePivot.set(DoubleSolenoid.Value.kReverse);
    }

    /**
     * Checks if intake is up
     * @return if the intake is up
     */
    public boolean isIntakeUp() {
        return intakePivot.get() == DoubleSolenoid.Value.kForward;
    }

    public synchronized static IntakePivot getInstance(){
        if (instance == null) {
            instance = new IntakePivot();
        }

        return instance;
    }

    @Override
    protected void initDefaultCommand() {

    }
}