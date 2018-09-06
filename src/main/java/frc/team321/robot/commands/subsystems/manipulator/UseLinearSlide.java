package frc.team321.robot.commands.subsystems.manipulator;

import frc.team321.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import frc.team321.robot.subsystems.misc.Sensors;

public class UseLinearSlide extends Command {

    private double power;
    private boolean useJoystick;

    public UseLinearSlide() {
        requires(Robot.manipulator.getLinearSlide());
        useJoystick = true;
    }

    public UseLinearSlide(double power) {
        requires(Robot.manipulator.getLinearSlide());
        this.power = power;

        useJoystick = false;
    }

    protected void initialize() {
        Robot.manipulator.getLinearSlide().stop();
    }

    protected void execute() {
        if (useJoystick) {
            if ((-Robot.oi.flightController.getYAxisValue() > 0 && Sensors.isLinearSlideFullyExtended())
                    || (-Robot.oi.flightController.getYAxisValue() < 0 && Sensors.isLinearSlideAtGround())) {
                Robot.manipulator.getLinearSlide().stop();
            } else {
                //Negative is up
                //Positive is down
                double joyVal = Robot.oi.flightController.getYAxisValue();
                double encoderVal = Robot.manipulator.getLinearSlide().getEncoder();
                double ratio;

                if (joyVal < 0) {
                    if (Robot.manipulator.getLinearSlide().getEncoder() < 80000) {
                        ratio = 1;
                    } else if (Robot.manipulator.getLinearSlide().getEncoder() >= 80000 &&
                            Robot.manipulator.getLinearSlide().getEncoder() <= 90000) {
                        ratio = 0.375 * Math.cos(Math.PI * (encoderVal - 70000.0 / 20000.0)) + 0.625;
                    } else {
                        ratio = 0.25;
                    }
                } else if (joyVal > 0) {
                    if (Robot.manipulator.getLinearSlide().getEncoder() > 20000) {
                        ratio = 1;
                    } else if (Robot.manipulator.getLinearSlide().getEncoder() >= 10000 &&
                            Robot.manipulator.getLinearSlide().getEncoder() <= 30000) {
                        ratio = -0.375 * Math.sin(Math.PI * (encoderVal / 20000.0)) + 0.625;
                    } else {
                        ratio = 0.25;
                    }
                } else {
                    ratio = 0;
                }

                Robot.manipulator.getLinearSlide().move(ratio * joyVal);
            }
        } else {
            if ((power > 0 && Sensors.isLinearSlideFullyExtended())
                    || (power < 0 && Sensors.isLinearSlideAtGround())) {
                Robot.manipulator.getLinearSlide().stop();
                return;
            }

            Robot.manipulator.getLinearSlide().move(power);
        }
    }

    protected void interrupted() {
        end();
    }

    protected void end() {
        Robot.manipulator.getLinearSlide().stop();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}