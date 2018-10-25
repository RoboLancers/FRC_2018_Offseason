package frc.team321.robot.commands.autonomous.modes;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team321.robot.commands.autonomous.subroutine.MoveRobot;
import frc.team321.robot.commands.autonomous.subroutine.PathFollower;
import frc.team321.robot.commands.autonomous.subroutine.RamsetePathFollower;
import frc.team321.robot.commands.subsystems.manipulator.UseIntake;
import frc.team321.robot.commands.subsystems.manipulator.UseIntakePivot;
import frc.team321.robot.utilities.enums.IntakePivotState;
import frc.team321.robot.utilities.enums.IntakePower;
import openrio.powerup.MatchData;

public class SideSwitchAuto extends CommandGroup {
    public SideSwitchAuto(boolean isLeft){
        MatchData.OwnedSide ownedSide = MatchData.getOwnedSide(MatchData.GameFeature.SWITCH_NEAR);

        addSequential(new UseIntakePivot(IntakePivotState.UP));

        if(isLeft && ownedSide == MatchData.OwnedSide.LEFT){
            addSequential(new RamsetePathFollower("SideSwitchLeftAuto"));
            addSequential(new UseIntake(IntakePower.OUTAKE), 1);
        }else if(!isLeft && ownedSide == MatchData.OwnedSide.RIGHT){
            addSequential(new RamsetePathFollower("SideSwitchRightAuto"));
            addSequential(new UseIntake(IntakePower.OUTAKE), 1);
        }else{
            addSequential(new MoveRobot(1, 0));
        }
    }
}
