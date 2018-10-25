package frc.team321.robot.commands.autonomous.modes;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team321.robot.commands.autonomous.subroutine.RamsetePathFollower;

class RunBaseline extends CommandGroup {
    RunBaseline(){
        addSequential(new RamsetePathFollower("CrossBaselineAuto"));
    }
}
