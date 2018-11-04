package frc.team321.robot.commands.autonomous.subroutine;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

class DelayCommand extends CommandGroup {
    DelayCommand(Command command, double delay){
        addSequential(new WaitCommand(delay));
        addSequential(command);
    }
}
