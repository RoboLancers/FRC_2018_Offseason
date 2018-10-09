package frc.team321.robot.commands.subsystems.drivetrain;

import frc.team321.robot.OI;
import frc.team321.robot.Robot;
import edu.wpi.first.wpilibj.command.Command;
import frc.team321.robot.subsystems.drivetrain.Drivetrain;

public class UseArcadeDrive extends Command {

    private double throttle, rotate;

    public UseArcadeDrive() {
        requires(Drivetrain.getInstance());
    }

    @Override
    protected void initialize() {
        Drivetrain.getInstance().stop();
    }

    @Override
    protected void execute() {
        throttle = OI.getInstance().xBoxController.getLeftYAxisValue();
        rotate = OI.getInstance().xBoxController.getRightXAxisValue();

        Drivetrain.getInstance().setLeft(throttle + rotate);
        Drivetrain.getInstance().setRight(throttle - rotate);
    }

    @Override
    protected void end() {
        Drivetrain.getInstance().stop();
    }

    @Override
    protected void interrupted() {
        Drivetrain.getInstance().stop();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

}