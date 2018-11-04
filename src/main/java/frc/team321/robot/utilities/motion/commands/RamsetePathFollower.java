package frc.team321.robot.utilities.motion.commands;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team321.robot.OI;
import frc.team321.robot.subsystems.drivetrain.Drivetrain;
import frc.team321.robot.utilities.RobotUtil;
import frc.team321.robot.utilities.enums.MotionProfileDirection;
import frc.team321.robot.utilities.motion.DriveSignal;
import frc.team321.robot.utilities.motion.RamseteFollower;
import jaci.pathfinder.Trajectory;

@SuppressWarnings("FieldCanBeLocal")
public class RamsetePathFollower extends Command{

    private RamseteFollower ramseteFollower;
    private DriveSignal driveSignal;
    private Trajectory.Segment current;

    public RamsetePathFollower(Trajectory trajectory){
        this(trajectory, MotionProfileDirection.FORWARD);
    }

    public RamsetePathFollower(Trajectory trajectory, MotionProfileDirection direction){
        requires(Drivetrain.getInstance());
        ramseteFollower = new RamseteFollower(trajectory, direction);
    }

    public RamsetePathFollower(Trajectory trajectory, double b, double zeta, MotionProfileDirection direction){
        requires(Drivetrain.getInstance());
        ramseteFollower = new RamseteFollower(trajectory, b, zeta, direction);
    }

    @Override
    protected void initialize(){
        Drivetrain.getInstance().setMode(NeutralMode.Brake);
    }

    @Override
    protected void execute(){
        driveSignal = ramseteFollower.getNextDriveSignal();
        current = ramseteFollower.currentSegment();

        Drivetrain.getInstance().setVelocity(driveSignal.getLeft(), driveSignal.getRight(), current.acceleration);

        OI.liveDashboardTable.getEntry("Path X").setNumber(current.x);
        OI.liveDashboardTable.getEntry("Path Y").setNumber(current.y);

        SmartDashboard.putNumber("Path Left Wheel Velocity", driveSignal.getLeft());
        SmartDashboard.putNumber("Path Right Wheel Velocity", driveSignal.getRight());

        SmartDashboard.putNumber("Robot Left Velocity", Drivetrain.getInstance().getLeft().getVelocity());
        SmartDashboard.putNumber("Robot Right Velocity", Drivetrain.getInstance().getRight().getVelocity());
    }

    @Override
    protected void end(){
        Drivetrain.getInstance().stop();
    }

    @Override
    protected boolean isFinished(){
        return ramseteFollower.isFinished();
    }
}
