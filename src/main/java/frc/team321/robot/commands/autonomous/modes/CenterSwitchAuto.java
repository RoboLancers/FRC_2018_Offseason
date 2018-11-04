package frc.team321.robot.commands.autonomous.modes;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team321.robot.utilities.motion.commands.RamsetePathFollower;
import frc.team321.robot.commands.autonomous.subroutine.SetInitialOdometry;
import frc.team321.robot.commands.subsystems.manipulator.UseIntake;
import frc.team321.robot.commands.subsystems.manipulator.UseIntakePivot;
import frc.team321.robot.utilities.enums.IntakePivotState;
import frc.team321.robot.utilities.enums.IntakePower;
import frc.team321.robot.utilities.enums.MotionProfileDirection;
import frc.team321.robot.utilities.motion.TrajectoryUtil;
import jaci.pathfinder.Trajectory;
import openrio.powerup.MatchData;

public class CenterSwitchAuto extends CommandGroup {
    public CenterSwitchAuto(){
        MatchData.OwnedSide ownedSide = MatchData.getOwnedSide(MatchData.GameFeature.SWITCH_NEAR);

        String centerSwitchTrajectoryName;
        String cubePileToSwitchTrajectoryName;

        if(ownedSide == MatchData.OwnedSide.LEFT){
            centerSwitchTrajectoryName = "CenterSwitchLeft";
            cubePileToSwitchTrajectoryName = "CubePileToLeftSwitch";
        }else{
            centerSwitchTrajectoryName = "CenterSwitchRight";
            cubePileToSwitchTrajectoryName = "CubePileToRightSwitch";
        }

        Trajectory centerSwitch = TrajectoryUtil.getTrajectoryFromName(centerSwitchTrajectoryName);
        Trajectory centerToCubePile = TrajectoryUtil.getTrajectoryFromName("CenterToCubePile");
        Trajectory cubePileToSwitch = TrajectoryUtil.getTrajectoryFromName(cubePileToSwitchTrajectoryName);

        //Set up initial odometry for Ramsete
        addSequential(new SetInitialOdometry(centerSwitch));

        //Drop off first cube
        addSequential(new RamsetePathFollower(centerSwitch, MotionProfileDirection.FORWARD));
        addSequential(new UseIntake(IntakePower.OUTAKE), 0.5);

        //Go back to starting position
        addParallel(new UseIntakePivot(IntakePivotState.DOWN));
        addSequential(new RamsetePathFollower(centerSwitch, MotionProfileDirection.BACKWARD));

        //Retrieve second cube
        addParallel(new UseIntake(IntakePower.INTAKE), 2);
        addSequential(new RamsetePathFollower(centerToCubePile, MotionProfileDirection.FORWARD));

        //Go to spit out the second cube
        addSequential(new UseIntakePivot(IntakePivotState.UP));
        addParallel(new UseIntake(IntakePower.PASSIVE), 2);
        addSequential(new RamsetePathFollower(cubePileToSwitch, MotionProfileDirection.FORWARD));
        addSequential(new UseIntake(IntakePower.OUTAKE), 0.5);
    }
}
