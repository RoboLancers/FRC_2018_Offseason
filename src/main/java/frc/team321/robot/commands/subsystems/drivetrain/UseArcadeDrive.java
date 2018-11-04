package frc.team321.robot.commands.subsystems.drivetrain;

import frc.team321.robot.OI;
import edu.wpi.first.wpilibj.command.Command;
import frc.team321.robot.subsystems.drivetrain.Drivetrain;
import frc.team321.robot.subsystems.manipulator.LinearSlide;

@SuppressWarnings("FieldCanBeLocal")
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
        if(LinearSlide.getInstance().getEncoderCount() > 50000) {
            if (!OI.getInstance().xBoxController.rightLancerTrigger.get()) {
                throttle = OI.getInstance().xBoxController.getLeftYAxisValue() * 0.5;
                rotate = OI.getInstance().xBoxController.getRightXAxisValue() * 0.5;
            } else {
                throttle = OI.getInstance().xBoxController.getLeftYAxisValue();
                rotate = OI.getInstance().xBoxController.getRightXAxisValue();
            }
        }else{
            if (!OI.getInstance().xBoxController.rightLancerTrigger.get()) {
                throttle = OI.getInstance().xBoxController.getLeftYAxisValue() * 0.75;
                rotate = OI.getInstance().xBoxController.getRightXAxisValue() * 0.75;
            } else {
                throttle = OI.getInstance().xBoxController.getLeftYAxisValue();
                rotate = OI.getInstance().xBoxController.getRightXAxisValue();
            }
        }

        Drivetrain.getInstance().setLeft(throttle + rotate);
        Drivetrain.getInstance().setRight(throttle - rotate);
    }

    @Override
    protected void end() {
        Drivetrain.getInstance().stop();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

}