package frc.team321.robot.commands.autonomous.subroutine;

import edu.wpi.first.wpilibj.command.Command;
import frc.team321.robot.Constants;
import frc.team321.robot.OI;
import frc.team321.robot.subsystems.drivetrain.Drivetrain;
import frc.team321.robot.subsystems.misc.Sensors;
import frc.team321.robot.utilities.motion.DriveSignal;
import frc.team321.robot.utilities.motion.RamseteFollower;

@SuppressWarnings("FieldCanBeLocal")
public class RamsetePathFollower extends Command{

    private RamseteFollower ramseteFollower;
    private DriveSignal driveSignal;

    public RamsetePathFollower(String trajectoryName){
        requires(Drivetrain.getInstance());
        ramseteFollower = new RamseteFollower(trajectoryName);
    }

    public RamsetePathFollower(String trajectoryName, double b, double zeta){
        requires(Drivetrain.getInstance());
        ramseteFollower = new RamseteFollower(trajectoryName, b, zeta);
    }

    @Override
    protected void initialize(){
        Sensors.getInstance().resetNavX();
        ramseteFollower.setInitialOdometry();
        OI.liveDashboardTable.getEntry("Reset").setBoolean(true);
    }

    @Override
    protected void execute(){
        driveSignal = ramseteFollower.getNextDriveSignal();

        Drivetrain.getInstance().setLeft(driveSignal.getLeft()/ Constants.DRIVETRAIN_MAX_VELOCITY);
        Drivetrain.getInstance().setRight(driveSignal.getRight()/Constants.DRIVETRAIN_MAX_VELOCITY);
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
