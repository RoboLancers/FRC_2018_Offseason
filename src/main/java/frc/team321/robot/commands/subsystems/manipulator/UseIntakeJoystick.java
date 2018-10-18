package frc.team321.robot.commands.subsystems.manipulator;

import edu.wpi.first.wpilibj.command.Command;
import frc.team321.robot.OI;
import frc.team321.robot.subsystems.manipulator.Intake;
import frc.team321.robot.utilities.enums.IntakePower;

public class UseIntakeJoystick extends Command {

    private double power;

    public UseIntakeJoystick(){
        requires(Intake.getInstance());
    }

    @Override
    protected void initialize() {
        Intake.getInstance().stop();
    }

    @Override
    protected void execute() {
        //Structured like this since we want the driver controller to have priority over the intake
        //Shows the priority of controls
        if (OI.getInstance().xBoxController.leftBumper.get()) {
            power = IntakePower.INTAKE.power;
        } else if (OI.getInstance().xBoxController.rightBumper.get()){
            power = IntakePower.OUTAKE.power;
        } else if (Math.abs(OI.getInstance().flightController.getRotateAxisValue()) > 0.5){
            //We only want the flight controller to outtake slowly hence the negative and the 0.25
            power = -Math.abs(OI.getInstance().flightController.getRotateAxisValue()) * 0.25;
        } else {
            power = 0;
        }

        OI.getInstance().xBoxController.setRumble(power > 0);
        Intake.getInstance().setAll(power);
    }

    @Override
    protected void end() {
        Intake.getInstance().stop();
        OI.getInstance().xBoxController.setRumble(false);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
