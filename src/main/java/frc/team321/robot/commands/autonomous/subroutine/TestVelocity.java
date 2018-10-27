package frc.team321.robot.commands.autonomous.subroutine;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team321.robot.subsystems.drivetrain.Drivetrain;
import frc.team321.robot.utilities.RobotUtil;

public class TestVelocity extends Command {
    public TestVelocity(){
        requires(Drivetrain.getInstance());
    }

    @Override
    protected void initialize(){
        Drivetrain.getInstance().setVelocity(5, 5);
    }

    @Override
    protected void execute(){
        SmartDashboard.putNumber("Robot Left Velocity", Drivetrain.getInstance().getLeft().getVelocity());
        SmartDashboard.putNumber("Robot Right Velocity", Drivetrain.getInstance().getRight().getVelocity());
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
