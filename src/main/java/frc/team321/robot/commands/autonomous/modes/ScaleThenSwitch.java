package frc.team321.robot.commands.autonomous.modes;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team321.robot.commands.autonomous.subroutine.SameSideScaleAuto;
import frc.team321.robot.utilities.enums.RobotStartingSide;
import openrio.powerup.MatchData;

public class ScaleThenSwitch extends CommandGroup {
    public ScaleThenSwitch(RobotStartingSide robotStartingSide) {
        MatchData.OwnedSide switchOwnedSide = MatchData.getOwnedSide(MatchData.GameFeature.SWITCH_NEAR);
        MatchData.OwnedSide scaleOwnedSide = MatchData.getOwnedSide(MatchData.GameFeature.SCALE);

        if((scaleOwnedSide == MatchData.OwnedSide.LEFT && robotStartingSide == RobotStartingSide.LEFT) || (scaleOwnedSide == MatchData.OwnedSide.RIGHT && robotStartingSide == RobotStartingSide.RIGHT)){
            addSequential(new SameSideScaleAuto(robotStartingSide));
        }else if((switchOwnedSide == MatchData.OwnedSide.LEFT && robotStartingSide == RobotStartingSide.LEFT) || (switchOwnedSide == MatchData.OwnedSide.RIGHT && robotStartingSide == RobotStartingSide.RIGHT)){
            addSequential(new SideSwitchAuto(robotStartingSide));
        }else{
            addSequential(new RunBaseline(robotStartingSide));
        }
    }
}
