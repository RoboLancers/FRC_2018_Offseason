package frc.team321.robot.commands.autonomous.subroutine;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team321.robot.commands.subsystems.manipulator.UseIntake;
import frc.team321.robot.commands.subsystems.manipulator.UseLinearSlidePosition;
import frc.team321.robot.utilities.enums.IntakePower;
import frc.team321.robot.utilities.enums.LinearSlidePosition;
import frc.team321.robot.utilities.enums.MotionProfileDirection;
import frc.team321.robot.utilities.enums.RobotStartingSide;
import frc.team321.robot.utilities.motion.TrajectoryUtil;
import frc.team321.robot.utilities.motion.commands.RamsetePathFollower;
import jaci.pathfinder.Trajectory;
import openrio.powerup.MatchData;

public class OppositeSideScaleAuto extends CommandGroup {
    public OppositeSideScaleAuto(RobotStartingSide robotStartingSide){
        MatchData.OwnedSide ownedSide = MatchData.getOwnedSide(MatchData.GameFeature.SCALE);

        Trajectory oppositeSideScale;

        if(robotStartingSide == RobotStartingSide.LEFT && ownedSide == MatchData.OwnedSide.RIGHT) {
            oppositeSideScale = TrajectoryUtil.getTrajectoryFromName("OppositeSideScaleFromLeft");
        }else if(robotStartingSide == RobotStartingSide.RIGHT && ownedSide == MatchData.OwnedSide.LEFT){
            oppositeSideScale = TrajectoryUtil.getTrajectoryFromName("OppositeSideScaleFromRight");
        }else{
            System.out.println("Something unrecoverable went wrong with SameSideScaleAuto");
            return;
        }

        //Run to same side
        addSequential(new SetInitialOdometry(oppositeSideScale));
        addParallel(new DelayCommand(new UseLinearSlidePosition(LinearSlidePosition.SCALE), 2));
        addSequential(new RamsetePathFollower(oppositeSideScale, MotionProfileDirection.FORWARD));

        addSequential(new UseIntake(IntakePower.OUTAKE), 0.25);
        addSequential(new UseLinearSlidePosition(LinearSlidePosition.BOTTOM));
    }
}
