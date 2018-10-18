package frc.team321.robot.commands.subsystems.manipulator;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.team321.robot.subsystems.manipulator.IntakePivot;
import frc.team321.robot.utilities.enums.IntakePivotState;

public class UseIntakePivot extends InstantCommand {

    private IntakePivotState intakePivotState;

    public UseIntakePivot(IntakePivotState intakePivotState) {
        requires(IntakePivot.getInstance());
        this.intakePivotState = intakePivotState;
    }

    @Override
    protected void initialize() {
        if(intakePivotState == IntakePivotState.UP){
            IntakePivot.getInstance().setUp();
        }else{
            IntakePivot.getInstance().setDown();
        }
    }
}