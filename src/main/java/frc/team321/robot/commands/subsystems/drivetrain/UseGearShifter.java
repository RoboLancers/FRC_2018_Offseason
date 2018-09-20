package frc.team321.robot.commands.subsystems.drivetrain;

import frc.team321.robot.Robot;
import edu.wpi.first.wpilibj.command.Command;

public class UseGearShifter extends Command {

    public UseGearShifter() {
        requires(Robot.drivetrain.getGearShifter());
    }

    @Override
    protected void execute() {
        if(Robot.oi.xBoxController.rightLancerTrigger.get()){
            Robot.drivetrain.getGearShifter().setHighGear();
        }else{
            Robot.drivetrain.getGearShifter().setLowGear();
        }
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

}