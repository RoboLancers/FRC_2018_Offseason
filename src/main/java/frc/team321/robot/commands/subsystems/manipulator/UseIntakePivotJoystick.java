package frc.team321.robot.commands.subsystems.manipulator;

import edu.wpi.first.wpilibj.command.Command;
import frc.team321.robot.OI;
import frc.team321.robot.subsystems.manipulator.IntakePivot;

public class UseIntakePivotJoystick extends Command {

    public UseIntakePivotJoystick() {
        requires(IntakePivot.getInstance());
    }

    @Override
    protected void execute() {
        if(OI.getInstance().flightController.farBottom.get()){
            IntakePivot.getInstance().setUp();
        }else{
            IntakePivot.getInstance().setDown();
        }
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
