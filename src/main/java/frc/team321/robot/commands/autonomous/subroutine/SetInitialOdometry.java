package frc.team321.robot.commands.autonomous.subroutine;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.team321.robot.utilities.motion.Odometry;
import jaci.pathfinder.Trajectory;

public class SetInitialOdometry extends InstantCommand {
    private Trajectory trajectory;

    public SetInitialOdometry(Trajectory trajectory){
        this.trajectory = trajectory;
    }

    @Override
    protected void initialize(){
        Odometry.getInstance().setInitialOdometry(trajectory);
    }
}
