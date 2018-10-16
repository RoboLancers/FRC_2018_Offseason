package frc.team321.robot.commands.autonomous.subroutine;

import edu.wpi.first.wpilibj.command.Command;
import frc.team321.robot.OI;
import frc.team321.robot.subsystems.drivetrain.Drivetrain;
import frc.team321.robot.subsystems.misc.Sensors;
import frc.team321.robot.utilities.DriveSignal;
import frc.team321.robot.utilities.RamseteFollower;

@SuppressWarnings("FieldCanBeLocal")
public class RamsetePathFollower extends Command{

    private RamseteFollower ramseteFollower;
    private DriveSignal driveSignal;

    public RamsetePathFollower(String trajectoryName){
        requires(Drivetrain.getInstance());
        ramseteFollower = new RamseteFollower(trajectoryName);
    }

    @Override
    protected void initialize(){
        Sensors.resetNavX();
        ramseteFollower.setInitialOdometry();
        OI.liveDashboardTable.getEntry("Reset").setBoolean(true);
    }

    @Override
    protected void execute(){
        driveSignal = ramseteFollower.getNextDriveSignal();
        Drivetrain.getInstance().setVelocity(driveSignal.getLeft(), driveSignal.getRight());
    }

    @Override
    protected boolean isFinished(){
        return ramseteFollower.isFinished();
    }

    @Override
    protected void end(){
        Drivetrain.getInstance().stop();
    }
}
