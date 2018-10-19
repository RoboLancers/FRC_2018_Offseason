package frc.team321.robot.commands.subsystems.manipulator;

import edu.wpi.first.wpilibj.command.Command;
import frc.team321.robot.subsystems.manipulator.LinearSlide;
import frc.team321.robot.subsystems.misc.Sensors;

public class UseLinearSlidePower extends Command{

    private double power;

    public UseLinearSlidePower(double power){
        requires(LinearSlide.getInstance());
        this.power = power;
    }

    @Override
    protected void execute(){
        if ((power > 0 && Sensors.getInstance().isLinearSlideFullyExtended())
                || (power < 0 && Sensors.getInstance().isLinearSlideAtGround())) {
            LinearSlide.getInstance().stop();
            return;
        }

        LinearSlide.getInstance().setPower(power);
    }

    protected void end() {
        LinearSlide.getInstance().stop();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
