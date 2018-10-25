package frc.team321.robot.commands.autonomous.subroutine;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team321.robot.Constants;
import frc.team321.robot.OI;
import frc.team321.robot.subsystems.drivetrain.Drivetrain;
import frc.team321.robot.subsystems.misc.Sensors;
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

    public RamsetePathFollower(String trajectoryName){
        this(trajectoryName, MotionProfileDirection.FORWARD);
    }

    public RamsetePathFollower(String trajectoryName, MotionProfileDirection direction){
        requires(Drivetrain.getInstance());
        ramseteFollower = new RamseteFollower(trajectoryName, direction);
    }

    public RamsetePathFollower(String trajectoryName, double b, double zeta){
        this(trajectoryName, b, zeta, MotionProfileDirection.FORWARD);
    }

    private RamsetePathFollower(String trajectoryName, double b, double zeta, MotionProfileDirection direction){
        requires(Drivetrain.getInstance());
        ramseteFollower = new RamseteFollower(trajectoryName, b, zeta, direction);
    }

    @Override
    protected void initialize(){
        ramseteFollower.setInitialOdometry();
        Drivetrain.getInstance().setMode(NeutralMode.Brake);
        OI.liveDashboardTable.getEntry("Reset").setBoolean(true);
    }

    @Override
    protected void execute(){
        driveSignal = ramseteFollower.getNextDriveSignal();

        Drivetrain.getInstance().setVelocity(driveSignal.getLeft(), driveSignal.getRight());

        current = ramseteFollower.currentSegment();

        OI.liveDashboardTable.getEntry("Path X").setNumber(current.x);
        OI.liveDashboardTable.getEntry("Path Y").setNumber(current.y);

        SmartDashboard.putNumber("Path Left Wheel Velocity", driveSignal.getLeft());
        SmartDashboard.putNumber("Path Right Wheel Velocity", driveSignal.getRight());

        SmartDashboard.putNumber("Robot Left Velocity", RobotUtil.encoderTicksToFeets(Drivetrain.getInstance().getLeft().getVelocity()) * 10);
        SmartDashboard.putNumber("Robot Right Velocity", RobotUtil.encoderTicksToFeets(Drivetrain.getInstance().getRight().getVelocity()) * 10);
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
