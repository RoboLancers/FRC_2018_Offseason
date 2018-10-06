package frc.team321.robot.commands.autonomous.subroutine;

import edu.wpi.first.wpilibj.command.Command;
import frc.team321.robot.Robot;
import frc.team321.robot.utilities.DriveSignal;
import frc.team321.robot.utilities.RamseteFollower;

@SuppressWarnings("FieldCanBeLocal")
public class RamsetePathFollower extends Command{

    private RamseteFollower ramseteFollower;
    private DriveSignal driveSignal;

    public RamsetePathFollower(String trajectoryName){
        requires(Robot.drivetrain);
        ramseteFollower = new RamseteFollower(trajectoryName);
    }

    @Override
    protected void initialize(){
        ramseteFollower.setInitialOdometry();
    }

    @Override
    protected void execute(){
        driveSignal = ramseteFollower.getNextDriveSignal();
        Robot.drivetrain.setVelocity(driveSignal.getLeft(), driveSignal.getRight());
    }

    @Override
    protected boolean isFinished(){
        return ramseteFollower.isFinished();
    }

    @Override
    protected void end(){
        Robot.drivetrain.stop();
    }

    @Override
    protected void interrupted(){
        end();
    }
}
