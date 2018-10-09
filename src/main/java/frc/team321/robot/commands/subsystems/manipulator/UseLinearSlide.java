package frc.team321.robot.commands.subsystems.manipulator;

import frc.team321.robot.OI;
import frc.team321.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import frc.team321.robot.subsystems.manipulator.Manipulator;
import frc.team321.robot.subsystems.misc.Sensors;

public class UseLinearSlide extends Command {

    private double power;
    private boolean useJoystick;

    public UseLinearSlide() {
        requires(Manipulator.getInstance().getLinearSlide());
        useJoystick = true;
    }

    public UseLinearSlide(double power) {
        requires(Manipulator.getInstance().getLinearSlide());
        this.power = power;

        useJoystick = false;
    }

    protected void initialize() {
        Manipulator.getInstance().getLinearSlide().stop();
    }

    protected void execute() {
        if (useJoystick) {
            if ((-OI.getInstance().flightController.getYAxisValue() > 0 && Sensors.isLinearSlideFullyExtended())
                    || (-OI.getInstance().flightController.getYAxisValue() < 0 && Sensors.isLinearSlideAtGround())) {
                Manipulator.getInstance().getLinearSlide().stop();
            } else {
                //Negative is up
                //Positive is down
                double joyVal = OI.getInstance().flightController.getYAxisValue();
                double encoderVal = Manipulator.getInstance().getLinearSlide().getEncoder();
                double ratio;

                if (joyVal < 0) {
                    if (Manipulator.getInstance().getLinearSlide().getEncoder() < 80000) {
                        ratio = 1;
                    } else if (Manipulator.getInstance().getLinearSlide().getEncoder() >= 80000 &&
                            Manipulator.getInstance().getLinearSlide().getEncoder() <= 90000) {
                        ratio = 0.375 * Math.cos(Math.PI * (encoderVal - 70000.0 / 20000.0)) + 0.625;
                    } else {
                        ratio = 0.25;
                    }
                } else if (joyVal > 0) {
                    if (Manipulator.getInstance().getLinearSlide().getEncoder() > 20000) {
                        ratio = 1;
                    } else if (Manipulator.getInstance().getLinearSlide().getEncoder() >= 10000 &&
                            Manipulator.getInstance().getLinearSlide().getEncoder() <= 30000) {
                        ratio = -0.375 * Math.sin(Math.PI * (encoderVal / 20000.0)) + 0.625;
                    } else {
                        ratio = 0.25;
                    }
                } else {
                    ratio = 0;
                }

                Manipulator.getInstance().getLinearSlide().move(ratio * joyVal);
            }
        } else {
            if ((power > 0 && Sensors.isLinearSlideFullyExtended())
                    || (power < 0 && Sensors.isLinearSlideAtGround())) {
                Manipulator.getInstance().getLinearSlide().stop();
                return;
            }

            Manipulator.getInstance().getLinearSlide().move(power);
        }
    }

    protected void interrupted() {
        end();
    }

    protected void end() {
        Manipulator.getInstance().getLinearSlide().stop();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}