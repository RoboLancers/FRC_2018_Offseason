package frc.team321.robot.commands.autonomous.modes;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team321.robot.commands.autonomous.subroutine.OppositeSideScaleAuto;
import frc.team321.robot.commands.autonomous.subroutine.SameSideScaleAuto;
import frc.team321.robot.utilities.enums.RobotStartingSide;
import openrio.powerup.MatchData;

public class FullScale extends CommandGroup {
    public FullScale(RobotStartingSide robotStartingSide){
        MatchData.OwnedSide ownedSide = MatchData.getOwnedSide(MatchData.GameFeature.SCALE);

        if(robotStartingSide == RobotStartingSide.LEFT){
            if(ownedSide == MatchData.OwnedSide.LEFT){
                addSequential(new SameSideScaleAuto(robotStartingSide));
            }else if(ownedSide == MatchData.OwnedSide.RIGHT){
                addSequential(new OppositeSideScaleAuto(robotStartingSide));
            }
        }else if(robotStartingSide == RobotStartingSide.RIGHT){
            if(ownedSide == MatchData.OwnedSide.RIGHT){
                addSequential(new SameSideScaleAuto(robotStartingSide));
            }else if(ownedSide == MatchData.OwnedSide.LEFT){
                addSequential(new OppositeSideScaleAuto(robotStartingSide));
            }
        }
    }
}
