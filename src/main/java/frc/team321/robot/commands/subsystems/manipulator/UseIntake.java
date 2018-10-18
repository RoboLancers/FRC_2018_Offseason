package frc.team321.robot.commands.subsystems.manipulator;

import edu.wpi.first.wpilibj.command.Command;
import frc.team321.robot.subsystems.manipulator.Intake;
import frc.team321.robot.utilities.enums.IntakePower;

public class UseIntake extends Command {

    private double power;

    private UseIntake(double power) {
        requires(Intake.getInstance());
        this.power = power;
    }

    public UseIntake(IntakePower intakePower){
        this(intakePower.power);
    }

    @Override
    protected void initialize() {
        Intake.getInstance().stop();
    }

    @Override
    protected void execute() {
        Intake.getInstance().setAll(power);
    }

    @Override
    protected void end() {
        Intake.getInstance().stop();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}