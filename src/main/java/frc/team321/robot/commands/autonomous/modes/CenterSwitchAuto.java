package frc.team321.robot.commands.autonomous.modes;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team321.robot.commands.autonomous.subroutine.RamsetePathFollower;
import frc.team321.robot.commands.subsystems.manipulator.UseIntake;
import frc.team321.robot.commands.subsystems.manipulator.UseIntakePivot;
import frc.team321.robot.utilities.enums.IntakePivotState;
import frc.team321.robot.utilities.enums.IntakePower;
import frc.team321.robot.utilities.enums.MotionProfileDirection;
import openrio.powerup.MatchData;

public class CenterSwitchAuto extends CommandGroup {
    public CenterSwitchAuto(){
        MatchData.OwnedSide ownedSide = MatchData.getOwnedSide(MatchData.GameFeature.SWITCH_NEAR);

        if(ownedSide == MatchData.OwnedSide.LEFT){
            //Run to switch and come back to starting position
            addSequential(new RamsetePathFollower("CenterSwitchLeftAuto", MotionProfileDirection.FORWARD));
            addSequential(new UseIntake(IntakePower.OUTAKE), 1);
            addSequential(new RamsetePathFollower("CenterSwitchLeftAuto", MotionProfileDirection.BACKWARD));

            //Retrieve second cube
            addSequential(new UseIntakePivot(IntakePivotState.DOWN));
            addParallel(new UseIntake(IntakePower.INTAKE), 2);
            addParallel(new RamsetePathFollower("RunToCubePile", MotionProfileDirection.FORWARD));
            addSequential(new UseIntakePivot(IntakePivotState.UP));
            addSequential(new RamsetePathFollower("RunToCubePile", MotionProfileDirection.BACKWARD));

            //Go to spit out second cube
            addSequential(new RamsetePathFollower("CenterSwitchLeftAuto", MotionProfileDirection.FORWARD));
            addSequential(new UseIntake(IntakePower.OUTAKE), 1);
        }else if(ownedSide == MatchData.OwnedSide.RIGHT) {
            //Run to switch and come back to starting position
            addSequential(new RamsetePathFollower("CenterSwitchRightAuto", MotionProfileDirection.FORWARD));
            addSequential(new UseIntake(IntakePower.OUTAKE), 1);
            addSequential(new RamsetePathFollower("CenterSwitchRightAuto", MotionProfileDirection.BACKWARD));

            //Retrieve second cube
            addSequential(new UseIntakePivot(IntakePivotState.DOWN));
            addParallel(new UseIntake(IntakePower.INTAKE), 2);
            addParallel(new RamsetePathFollower("RunToCubePile", MotionProfileDirection.FORWARD));
            addSequential(new UseIntakePivot(IntakePivotState.UP));
            addSequential(new RamsetePathFollower("RunToCubePile", MotionProfileDirection.BACKWARD));

            //Go to spit out the second cube
            addSequential(new RamsetePathFollower("CenterSwitchRightAuto", MotionProfileDirection.FORWARD));
            addSequential(new UseIntake(IntakePower.OUTAKE), 1);
        }
    }
}
