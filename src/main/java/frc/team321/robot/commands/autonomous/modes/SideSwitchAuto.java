package frc.team321.robot.commands.autonomous.modes;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team321.robot.commands.autonomous.subroutine.MoveRobot;
import frc.team321.robot.commands.autonomous.subroutine.PathFollower;
import openrio.powerup.MatchData;

public class SideSwitchAuto extends CommandGroup {
    public SideSwitchAuto(boolean isLeft){
        MatchData.OwnedSide ownedSide = MatchData.getOwnedSide(MatchData.GameFeature.SWITCH_NEAR);

        if(isLeft && ownedSide == MatchData.OwnedSide.LEFT){
            addSequential(new PathFollower("SideSwitchLeft"));
        }else if(!isLeft && ownedSide == MatchData.OwnedSide.RIGHT){
            addSequential(new PathFollower("SideSwitchRight"));
        }else{
            addSequential(new MoveRobot(1, 0));
        }
    }
}
