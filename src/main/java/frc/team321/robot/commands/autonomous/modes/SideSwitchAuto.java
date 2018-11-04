package frc.team321.robot.commands.autonomous.modes;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team321.robot.commands.autonomous.subroutine.SetInitialOdometry;
import frc.team321.robot.utilities.motion.commands.RamsetePathFollower;
import frc.team321.robot.commands.subsystems.manipulator.UseIntake;
import frc.team321.robot.commands.subsystems.manipulator.UseIntakePivot;
import frc.team321.robot.utilities.enums.IntakePivotState;
import frc.team321.robot.utilities.enums.IntakePower;
import frc.team321.robot.utilities.enums.MotionProfileDirection;
import frc.team321.robot.utilities.enums.RobotStartingSide;
import frc.team321.robot.utilities.motion.TrajectoryUtil;
import jaci.pathfinder.Trajectory;
import openrio.powerup.MatchData;

public class SideSwitchAuto extends CommandGroup {
    public SideSwitchAuto(RobotStartingSide robotStartingSide){
        MatchData.OwnedSide ownedSide = MatchData.getOwnedSide(MatchData.GameFeature.SWITCH_NEAR);

        addSequential(new UseIntakePivot(IntakePivotState.UP));

        if(ownedSide == MatchData.OwnedSide.LEFT && robotStartingSide == RobotStartingSide.LEFT){
            Trajectory sideSwitchLeft = TrajectoryUtil.getTrajectoryFromName("SideSwitchLeft");
            addSequential(new SetInitialOdometry(sideSwitchLeft));
            addSequential(new RamsetePathFollower(sideSwitchLeft, MotionProfileDirection.FORWARD));
            addSequential(new UseIntake(IntakePower.OUTAKE), 1);
        }else if(ownedSide == MatchData.OwnedSide.RIGHT && robotStartingSide == RobotStartingSide.RIGHT){
            Trajectory sideSwitchRight = TrajectoryUtil.getTrajectoryFromName("SideSwitchRight");
            addSequential(new SetInitialOdometry(sideSwitchRight));
            addSequential(new RamsetePathFollower(sideSwitchRight, MotionProfileDirection.FORWARD));
            addSequential(new UseIntake(IntakePower.OUTAKE), 1);
        }else{
            addSequential(new RunBaseline(robotStartingSide));
        }
    }
}
