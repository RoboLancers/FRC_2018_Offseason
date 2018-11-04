package frc.team321.robot.commands.autonomous.subroutine;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team321.robot.commands.subsystems.manipulator.UseIntake;
import frc.team321.robot.commands.subsystems.manipulator.UseLinearSlidePosition;
import frc.team321.robot.utilities.enums.RobotStartingSide;
import frc.team321.robot.utilities.enums.IntakePower;
import frc.team321.robot.utilities.enums.LinearSlidePosition;
import frc.team321.robot.utilities.enums.MotionProfileDirection;
import frc.team321.robot.utilities.motion.commands.RamsetePathFollower;
import frc.team321.robot.utilities.motion.TrajectoryUtil;
import jaci.pathfinder.Trajectory;
import openrio.powerup.MatchData;

public class SameSideScaleAuto extends CommandGroup {
    public SameSideScaleAuto(RobotStartingSide robotStartingSide){
        MatchData.OwnedSide ownedSide = MatchData.getOwnedSide(MatchData.GameFeature.SCALE);

        Trajectory sameSideScale;
        Trajectory sameSideScaleToFirstCube;
        Trajectory sameSideScaleFromFirstCube;

        if(robotStartingSide == RobotStartingSide.LEFT && ownedSide == MatchData.OwnedSide.LEFT) {
            sameSideScale = TrajectoryUtil.getTrajectoryFromName("SameSideScaleLeft");
            sameSideScaleToFirstCube = TrajectoryUtil.getTrajectoryFromName("SameSideScaleLeftToFirstCube");
            sameSideScaleFromFirstCube = TrajectoryUtil.getTrajectoryFromName("SameSideScaleLeftFromFirstCube");
        }else if(robotStartingSide == RobotStartingSide.RIGHT && ownedSide == MatchData.OwnedSide.RIGHT){
            sameSideScale = TrajectoryUtil.getTrajectoryFromName("SameSideScaleRight");
            sameSideScaleToFirstCube = TrajectoryUtil.getTrajectoryFromName("SameSideScaleRightToFirstCube");
            sameSideScaleFromFirstCube = TrajectoryUtil.getTrajectoryFromName("SameSideScaleRightFromFirstCube");
        }else{
            System.out.println("Something unrecoverable went wrong with SameSideScaleAuto");
            return;
        }

        //Run to same side
        addSequential(new SetInitialOdometry(sameSideScale));
        addParallel(new DelayCommand(new UseLinearSlidePosition(LinearSlidePosition.SCALE), 2));
        addSequential(new RamsetePathFollower(sameSideScale, MotionProfileDirection.FORWARD));

        addSequential(new UseIntake(IntakePower.OUTAKE), 0.25);
        addSequential(new UseLinearSlidePosition(LinearSlidePosition.BOTTOM));

        //Run to second cube
        addParallel(new UseIntake(IntakePower.INTAKE), 2);
        addSequential(new RamsetePathFollower(sameSideScaleToFirstCube, MotionProfileDirection.FORWARD));

        //Run back to scale
        addParallel(new UseLinearSlidePosition(LinearSlidePosition.SCALE));
        addSequential(new RamsetePathFollower(sameSideScaleFromFirstCube));

        addSequential(new UseIntake(IntakePower.OUTAKE), 0.25);
    }
}
