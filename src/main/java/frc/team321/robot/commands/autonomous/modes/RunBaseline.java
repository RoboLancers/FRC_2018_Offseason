package frc.team321.robot.commands.autonomous.modes;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team321.robot.commands.autonomous.subroutine.RamsetePathFollower;
import frc.team321.robot.utilities.enums.MotionProfileDirection;
import frc.team321.robot.utilities.motion.TrajectoryUtil;

public class RunBaseline extends CommandGroup {
    public RunBaseline(){
        addSequential(new RamsetePathFollower(TrajectoryUtil.getTrajectoryFromName("CrossBaselineAuto"), MotionProfileDirection.FORWARD));
    }
}
