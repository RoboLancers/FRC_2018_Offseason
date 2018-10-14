package frc.team321.robot.commands.subsystems.manipulator;

import frc.team321.robot.OI;
import edu.wpi.first.wpilibj.command.Command;
import frc.team321.robot.subsystems.manipulator.IntakePivot;

public class UseIntakePivot extends Command {

    public UseIntakePivot() {
        requires(IntakePivot.getInstance());
    }

    @Override
    protected void execute() {
        if(OI.getInstance().flightController.farBottom.get() && !IntakePivot.getInstance().isIntakeUp()){
            IntakePivot.getInstance().setUp();
        }else{
            IntakePivot.getInstance().setDown();
        }
    }

    @Override
    protected void end(){
        IntakePivot.getInstance().setDown();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

}