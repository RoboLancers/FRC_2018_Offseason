package frc.team321.robot.commands.subsystems.drivetrain;

import frc.team321.robot.OI;
import edu.wpi.first.wpilibj.command.Command;
import frc.team321.robot.subsystems.drivetrain.Drivetrain;
import frc.team321.robot.subsystems.drivetrain.GearShifter;

public class UseGearShifter extends Command {

    public UseGearShifter() {
        requires(GearShifter.getInstance());
    }

    @Override
    protected void execute() {
        if(OI.getInstance().xBoxController.rightLancerTrigger.get()){
            GearShifter.getInstance().setHighGear();
        }
    }

    @Override
    protected void end(){
        GearShifter.getInstance().setLowGear();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

}