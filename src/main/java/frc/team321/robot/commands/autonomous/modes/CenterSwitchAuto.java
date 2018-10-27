package frc.team321.robot.commands.autonomous.modes;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team321.robot.commands.autonomous.subroutine.RamsetePathFollower;
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

        String trajectoryName = ownedSide == MatchData.OwnedSide.LEFT ? "CenterSwitchLeftAuto" : "CenterSwitchRightAuto";

        Trajectory centerSwitch = TrajectoryUtil.getTrajectoryFromName(trajectoryName);
        Trajectory runToCubePile = TrajectoryUtil.getTrajectoryFromName("RunToCubePile");

        //Set up initial odometry for Ramsete
        addSequential(new SetInitialOdometry(centerSwitch));

        //Run to switch and come back to starting position
        addSequential(new RamsetePathFollower(centerSwitch, MotionProfileDirection.FORWARD));
        addSequential(new UseIntake(IntakePower.OUTAKE), 1);
        addParallel(new UseIntakePivot(IntakePivotState.DOWN));
        addSequential(new RamsetePathFollower(centerSwitch, MotionProfileDirection.BACKWARD));

        //Retrieve second cube
        addParallel(new UseIntake(IntakePower.INTAKE), 4);
        addSequential(new RamsetePathFollower(runToCubePile, MotionProfileDirection.FORWARD));
        addSequential(new UseIntakePivot(IntakePivotState.UP));
        addSequential(new RamsetePathFollower(runToCubePile, MotionProfileDirection.BACKWARD));

        //Go to spit out the second cube
        addSequential(new RamsetePathFollower(centerSwitch, MotionProfileDirection.FORWARD));
        addSequential(new UseIntake(IntakePower.OUTAKE), 1);
    }
}
