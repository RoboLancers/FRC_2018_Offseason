package frc.team321.robot.commands.subsystems.manipulator;

import frc.team321.robot.OI;

import edu.wpi.first.wpilibj.command.Command;
import frc.team321.robot.subsystems.manipulator.LinearSlide;
import frc.team321.robot.subsystems.misc.Sensors;

public class UseLinearSlideJoystick extends Command {

    private double joystickValue, encoderValue, ratio;

    public UseLinearSlideJoystick() {
        requires(LinearSlide.getInstance());
    }

    protected void initialize() {
        LinearSlide.getInstance().stop();
    }

    protected void execute() {
        if ((-OI.getInstance().flightController.getYAxisValue() > 0 && Sensors.getInstance().isLinearSlideFullyExtended())
                || (-OI.getInstance().flightController.getYAxisValue() < 0 && Sensors.getInstance().isLinearSlideAtGround())) {
            LinearSlide.getInstance().stop();
        } else {
            joystickValue = OI.getInstance().flightController.getYAxisValue();
            encoderValue = LinearSlide.getInstance().getEncoderCount();

            if (joystickValue < 0) {
                if (LinearSlide.getInstance().getEncoderCount() < 80000) {
                    ratio = 1;
                } else if (LinearSlide.getInstance().getEncoderCount() >= 80000 &&
                        LinearSlide.getInstance().getEncoderCount() <= 90000) {
                    ratio = 0.375 * Math.cos(Math.PI * (encoderValue - 70000.0 / 20000.0)) + 0.625;
                } else {
                    ratio = 0.25;
                }
            } else if (joystickValue > 0) {
                if (LinearSlide.getInstance().getEncoderCount() > 20000) {
                    ratio = 1;
                } else if (LinearSlide.getInstance().getEncoderCount() >= 10000 &&
                        LinearSlide.getInstance().getEncoderCount() <= 30000) {
                    ratio = -0.375 * Math.sin(Math.PI * (encoderValue / 20000.0)) + 0.625;
                } else {
                    ratio = 0.25;
                }
            } else {
                ratio = 0;
            }

            LinearSlide.getInstance().setPower(-ratio * joystickValue);
        }
    }

    protected void end() {
        LinearSlide.getInstance().stop();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}