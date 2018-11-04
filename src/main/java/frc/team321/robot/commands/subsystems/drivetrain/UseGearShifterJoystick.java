package frc.team321.robot.commands.subsystems.drivetrain;

import frc.team321.robot.OI;
import edu.wpi.first.wpilibj.command.Command;
import frc.team321.robot.subsystems.drivetrain.Drivetrain;
import frc.team321.robot.subsystems.drivetrain.GearShifter;

public class UseGearShifterJoystick extends Command {

    public UseGearShifterJoystick() {
        requires(GearShifter.getInstance());
    }

    @Override
    protected void execute() {
        if(OI.getInstance().xBoxController.leftLancerTrigger.get()){
            GearShifter.getInstance().setHighGear();
        }else{
            GearShifter.getInstance().setLowGear();
        }
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

}