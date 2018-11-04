package frc.team321.robot.commands.autonomous.modes;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team321.robot.commands.autonomous.subroutine.SetInitialOdometry;
import frc.team321.robot.utilities.enums.RobotStartingSide;
import frc.team321.robot.utilities.motion.commands.RamsetePathFollower;
import frc.team321.robot.utilities.enums.MotionProfileDirection;
import frc.team321.robot.utilities.motion.TrajectoryUtil;
import jaci.pathfinder.Trajectory;

class RunBaseline extends CommandGroup {
    RunBaseline(RobotStartingSide robotStartingSide){
        Trajectory crossBaseline = TrajectoryUtil.getTrajectoryFromName(robotStartingSide == RobotStartingSide.LEFT ? "CrossBaselineLeft" : "CrossBaselineRight");
        addSequential(new SetInitialOdometry(crossBaseline));
        addSequential(new RamsetePathFollower(crossBaseline, MotionProfileDirection.FORWARD));
    }
}
