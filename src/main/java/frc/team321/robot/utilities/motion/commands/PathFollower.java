package frc.team321.robot.utilities.motion.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team321.robot.OI;
import frc.team321.robot.subsystems.drivetrain.Drivetrain;
import frc.team321.robot.utilities.motion.DriveSignal;
import frc.team321.robot.utilities.motion.PathfinderFollower;

@SuppressWarnings("FieldCanBeLocal")
public class PathFollower extends Command{

    private PathfinderFollower pathfinderFollower;
    private DriveSignal driveSignal;

    public PathFollower(String trajectoryName){
        requires(Drivetrain.getInstance());
        pathfinderFollower = new PathfinderFollower(trajectoryName);
    }

    @Override
    protected void initialize(){
        Drivetrain.getInstance().stop();
        OI.liveDashboardTable.getEntry("Reset").setBoolean(true);
        pathfinderFollower.setInitialOdometry();
    }

    @Override
    protected void execute(){
        driveSignal = pathfinderFollower.getNextDriveSignal();

        Drivetrain.getInstance().setLeft(driveSignal.getLeft());
        Drivetrain.getInstance().setRight(driveSignal.getRight());

        OI.liveDashboardTable.getEntry("Path X").setNumber((pathfinderFollower.getLeftEncoderFollower().getSegment().x + pathfinderFollower.getRightEncoderFollower().getSegment().x) / 2);
        OI.liveDashboardTable.getEntry("Path Y").setNumber((pathfinderFollower.getLeftEncoderFollower().getSegment().y + pathfinderFollower.getRightEncoderFollower().getSegment().y) / 2);
    }

    @Override
    protected boolean isFinished() {
        return pathfinderFollower.isFinished();
    }

    @Override
    protected void end(){
        Drivetrain.getInstance().stop();
    }
}
