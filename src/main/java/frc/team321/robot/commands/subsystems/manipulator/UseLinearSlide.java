package frc.team321.robot.commands.subsystems.manipulator;

import frc.team321.robot.OI;

import edu.wpi.first.wpilibj.command.Command;
import frc.team321.robot.subsystems.manipulator.LinearSlide;
import frc.team321.robot.subsystems.misc.Sensors;

public class UseLinearSlide extends Command {

    private double power;
    private boolean useJoystick;

    public UseLinearSlide() {
        requires(LinearSlide.getInstance());
        useJoystick = true;
    }

    public UseLinearSlide(double power) {
        requires(LinearSlide.getInstance());
        this.power = power;

        useJoystick = false;
    }

    protected void initialize() {
        LinearSlide.getInstance().stop();
    }

    protected void execute() {
        if (useJoystick) {
            if ((-OI.getInstance().flightController.getYAxisValue() > 0 && Sensors.isLinearSlideFullyExtended())
                    || (-OI.getInstance().flightController.getYAxisValue() < 0 && Sensors.isLinearSlideAtGround())) {
                LinearSlide.getInstance().stop();
            } else {
                //Negative is up
                //Positive is down
                double joyVal = OI.getInstance().flightController.getYAxisValue();
                double encoderVal = LinearSlide.getInstance().getEncoder();
                double ratio;

                if (joyVal < 0) {
                    if (LinearSlide.getInstance().getEncoder() < 80000) {
                        ratio = 1;
                    } else if (LinearSlide.getInstance().getEncoder() >= 80000 &&
                            LinearSlide.getInstance().getEncoder() <= 90000) {
                        ratio = 0.375 * Math.cos(Math.PI * (encoderVal - 70000.0 / 20000.0)) + 0.625;
                    } else {
                        ratio = 0.25;
                    }
                } else if (joyVal > 0) {
                    if (LinearSlide.getInstance().getEncoder() > 20000) {
                        ratio = 1;
                    } else if (LinearSlide.getInstance().getEncoder() >= 10000 &&
                            LinearSlide.getInstance().getEncoder() <= 30000) {
                        ratio = -0.375 * Math.sin(Math.PI * (encoderVal / 20000.0)) + 0.625;
                    } else {
                        ratio = 0.25;
                    }
                } else {
                    ratio = 0;
                }

                LinearSlide.getInstance().move(ratio * joyVal);
            }
        } else {
            if ((power > 0 && Sensors.isLinearSlideFullyExtended())
                    || (power < 0 && Sensors.isLinearSlideAtGround())) {
                LinearSlide.getInstance().stop();
                return;
            }

            LinearSlide.getInstance().move(power);
        }
    }

    protected void interrupted() {
        end();
    }

    protected void end() {
        LinearSlide.getInstance().stop();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}