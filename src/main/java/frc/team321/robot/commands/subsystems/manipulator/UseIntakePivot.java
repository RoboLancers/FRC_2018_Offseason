package frc.team321.robot.commands.subsystems.manipulator;

import frc.team321.robot.Robot;
import edu.wpi.first.wpilibj.command.Command;

public class UseIntakePivot extends Command {

    public UseIntakePivot() {
        requires(Robot.manipulator.getIntakePivot());
    }

    @Override
    protected void execute() {
        if(Robot.oi.flightController.farBottom.get()){
            Robot.manipulator.getIntakePivot().setUp();
        }else{
            Robot.manipulator.getIntakePivot().setDown();
        }
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

}