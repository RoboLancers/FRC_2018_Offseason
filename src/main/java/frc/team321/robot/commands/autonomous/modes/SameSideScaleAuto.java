package frc.team321.robot.commands.autonomous.modes;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team321.robot.commands.autonomous.subroutine.PathFollower;
import frc.team321.robot.commands.subsystems.manipulator.UseIntake;
import frc.team321.robot.commands.subsystems.manipulator.UseLinearSlidePosition;
import frc.team321.robot.utilities.enums.IntakePower;
import frc.team321.robot.utilities.enums.LinearSlidePosition;
import openrio.powerup.MatchData;

public class SameSideScaleAuto extends CommandGroup {
    public SameSideScaleAuto(boolean isLeft){
        MatchData.OwnedSide ownedSide = MatchData.getOwnedSide(MatchData.GameFeature.SCALE);

        if(isLeft && ownedSide == MatchData.OwnedSide.LEFT) {
            addSequential(new PathFollower("SameSideScaleLeftAuto"));
        }else if(!isLeft && ownedSide == MatchData.OwnedSide.RIGHT){
            addSequential(new PathFollower("SameSideScaleRightAuto"));
        }

        addSequential(new UseLinearSlidePosition(LinearSlidePosition.SCALE));
        addSequential(new UseIntake(IntakePower.OUTAKE), 1);
        addSequential(new UseLinearSlidePosition(LinearSlidePosition.BOTTOM));
    }
}
