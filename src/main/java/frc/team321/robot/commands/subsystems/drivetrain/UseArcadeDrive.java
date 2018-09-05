package frc.team321.robot.commands.subsystems.drivetrain;

import frc.team321.robot.Robot;
import edu.wpi.first.wpilibj.command.Command;

public class UseArcadeDrive extends Command {

    private double throttle, rotate;

    public UseArcadeDrive() {
        requires(Robot.drivetrain);
    }

    @Override
    protected void initialize() {
        Robot.drivetrain.stop();
    }

    @Override
    protected void execute() {
        throttle = Robot.oi.xBoxController.getLeftYAxisValue();
        rotate = Robot.oi.xBoxController.getRightXAxisValue();

        Robot.drivetrain.setLeft(throttle + rotate);
        Robot.drivetrain.setRight(throttle - rotate);
    }

    @Override
    protected void end() {
        Robot.drivetrain.stop();
    }

    @Override
    protected void interrupted() {
        Robot.drivetrain.stop();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

}