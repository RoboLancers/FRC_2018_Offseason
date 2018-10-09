package frc.team321.robot.commands.subsystems.manipulator;

import frc.team321.robot.OI;
import frc.team321.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import frc.team321.robot.subsystems.manipulator.Manipulator;
import frc.team321.robot.utilities.enums.IntakePower;

public class UseIntake extends Command {

    private double power;
    private boolean useJoystick;

    public UseIntake(){
        requires(Manipulator.getInstance().getIntake());
        useJoystick = true;
    }

    public UseIntake(double power) {
        requires(Manipulator.getInstance().getIntake());
        this.power = power;
        useJoystick = false;
    }

    public UseIntake(IntakePower intakePower){
        this(intakePower.power);
    }

    @Override
    protected void initialize() {
        Manipulator.getInstance().getIntake().stop();
    }

    @Override
    protected void execute() {
        if(useJoystick){
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
        }

        Manipulator.getInstance().getIntake().setAll(power);
    }

    @Override
    protected void interrupted() {
        end();
    }

    @Override
    protected void end() {
        Manipulator.getInstance().getIntake().stop();
        OI.getInstance().xBoxController.setRumble(false);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}