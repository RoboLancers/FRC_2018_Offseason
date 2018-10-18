package frc.team321.robot.commands.autonomous.modes;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team321.robot.commands.autonomous.subroutine.PathFollower;
import frc.team321.robot.commands.subsystems.manipulator.UseIntake;
import frc.team321.robot.commands.subsystems.manipulator.UseIntakePivot;
import frc.team321.robot.commands.subsystems.manipulator.UseLinearSlide;
import frc.team321.robot.utilities.enums.IntakePivotState;
import frc.team321.robot.utilities.enums.IntakePower;
import openrio.powerup.MatchData;

public class SameSideScaleAuto extends CommandGroup {
    public SameSideScaleAuto(boolean isLeft){
        MatchData.OwnedSide ownedSide = MatchData.getOwnedSide(MatchData.GameFeature.SCALE);

        addSequential(new UseIntakePivot(IntakePivotState.UP));

        if(isLeft && ownedSide == MatchData.OwnedSide.LEFT) {
            addSequential(new PathFollower("SameSideScaleLeftAuto"));
        }else if(!isLeft && ownedSide == MatchData.OwnedSide.RIGHT){
            addSequential(new PathFollower("SameSideScaleRightAuto"));
        }

        addSequential(new UseLinearSlide(-1), 2);
        addSequential(new UseIntake(IntakePower.OUTAKE), 1);
        addSequential(new UseLinearSlide(0.5), 1.5);
    }
}
