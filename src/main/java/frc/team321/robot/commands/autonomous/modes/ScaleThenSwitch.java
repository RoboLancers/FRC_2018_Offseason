package frc.team321.robot.commands.autonomous.modes;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team321.robot.commands.autonomous.subroutine.SameSideScaleAuto;
import frc.team321.robot.commands.autonomous.subroutine.SideSwitchAuto;
import openrio.powerup.MatchData;

public class ScaleThenSwitch extends CommandGroup {
    public ScaleThenSwitch(boolean isLeft) {
        MatchData.OwnedSide switchOwnedSide = MatchData.getOwnedSide(MatchData.GameFeature.SWITCH_NEAR);
        MatchData.OwnedSide scaleOwnedSide = MatchData.getOwnedSide(MatchData.GameFeature.SCALE);

        if((scaleOwnedSide == MatchData.OwnedSide.LEFT && isLeft) || (scaleOwnedSide != MatchData.OwnedSide.RIGHT && !isLeft)){
            addSequential(new SameSideScaleAuto(isLeft));
        }else if((switchOwnedSide == MatchData.OwnedSide.LEFT && isLeft) || (switchOwnedSide == MatchData.OwnedSide.RIGHT && !isLeft)){
            addSequential(new SideSwitchAuto(isLeft));
        }else{
            addSequential(new RunBaseline());
        }
    }
}
