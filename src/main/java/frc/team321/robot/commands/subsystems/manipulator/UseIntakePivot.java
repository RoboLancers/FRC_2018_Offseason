package frc.team321.robot.commands.subsystems.manipulator;

import frc.team321.robot.OI;
import frc.team321.robot.Robot;
import edu.wpi.first.wpilibj.command.Command;
import frc.team321.robot.subsystems.manipulator.Manipulator;

public class UseIntakePivot extends Command {

    public UseIntakePivot() {
        requires(Manipulator.getInstance().getIntakePivot());
    }

    @Override
    protected void execute() {
        if(OI.getInstance().flightController.farBottom.get()){
            Manipulator.getInstance().getIntakePivot().setUp();
        }else{
            Manipulator.getInstance().getIntakePivot().setDown();
        }
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

}