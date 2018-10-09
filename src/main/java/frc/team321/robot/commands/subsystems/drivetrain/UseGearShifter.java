package frc.team321.robot.commands.subsystems.drivetrain;

import frc.team321.robot.OI;
import edu.wpi.first.wpilibj.command.Command;
import frc.team321.robot.subsystems.drivetrain.Drivetrain;

public class UseGearShifter extends Command {

    public UseGearShifter() {
        requires(Drivetrain.getInstance().getGearShifter());
    }

    @Override
    protected void execute() {
        if(OI.getInstance().xBoxController.rightLancerTrigger.get()){
            Drivetrain.getInstance().getGearShifter().setLowGear();
        }
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

}