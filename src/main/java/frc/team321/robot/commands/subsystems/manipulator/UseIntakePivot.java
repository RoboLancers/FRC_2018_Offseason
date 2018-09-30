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
            if(Robot.manipulator.getIntakePivot().isIntakeUp()){
                Robot.manipulator.getIntakePivot().setDown();
            }else{
                Robot.manipulator.getIntakePivot().setUp();
            }
        }
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

}