package frc.team321.robot.commands.autonomous.modes;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team321.robot.commands.autonomous.subroutine.PathFollower;
import frc.team321.robot.commands.subsystems.manipulator.UseIntake;
import frc.team321.robot.commands.subsystems.manipulator.UseIntakePivot;
import frc.team321.robot.utilities.enums.IntakePivotState;
import frc.team321.robot.utilities.enums.IntakePower;
import openrio.powerup.MatchData;

public class CenterSwitchAuto extends CommandGroup {
    public CenterSwitchAuto(){
        MatchData.OwnedSide ownedSide = MatchData.getOwnedSide(MatchData.GameFeature.SWITCH_NEAR);

        addSequential(new UseIntakePivot(IntakePivotState.UP));

        if(ownedSide == MatchData.OwnedSide.LEFT){
            addSequential(new PathFollower("CenterSwitchLeftAuto"));
            addSequential(new UseIntake(IntakePower.OUTAKE), 1);
        }else if(ownedSide == MatchData.OwnedSide.RIGHT) {
            addSequential(new PathFollower("CenterSwitchRightAuto"));
            addSequential(new UseIntake(IntakePower.OUTAKE), 1);
        }else {
            addSequential(new PathFollower("CenterSwitchRightAuto"));
        }
    }
}
