package frc.team321.robot.commands.autonomous.modes;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team321.robot.commands.autonomous.subroutine.PathFollower;

public class RunBaseline extends CommandGroup {
    public RunBaseline(){
        addSequential(new PathFollower("CrossBaselineAuto"));
    }
}
