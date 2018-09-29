package frc.team321.robot.subsystems.drivetrain;

import static frc.team321.robot.Constants.*;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team321.robot.commands.subsystems.drivetrain.UseGearShifter;

public class GearShifter extends Subsystem {

    private static DoubleSolenoid gearShifter;

    GearShifter(){
        gearShifter = new DoubleSolenoid(GEARSHIFTER_FORWARD, GEARSHIFTER_REVERSE);
        gearShifter.set(DoubleSolenoid.Value.kForward);
    }

    /**
     * Sets the drivetrain into high gear
     */
    public void setHighGear() {
        gearShifter.set(DoubleSolenoid.Value.kReverse);
    }

    /**
     * Sets the drivetrain low gear
     */
    public void setLowGear() {
        gearShifter.set(DoubleSolenoid.Value.kForward);
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new UseGearShifter());
    }
}