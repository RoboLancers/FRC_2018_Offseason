package frc.team321.robot.commands.subsystems.manipulator;

import frc.team321.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import frc.team321.robot.utilities.enums.IntakePower;

public class UseIntake extends Command {

    private double power;
    private boolean useJoystick;

    public UseIntake(){
        requires(Robot.manipulator.getIntake());
        useJoystick = true;
    }

    public UseIntake(double power) {
        requires(Robot.manipulator.getIntake());
        this.power = power;
        useJoystick = false;
    }

    public UseIntake(IntakePower intakePower){
        this(intakePower.power);
    }

    @Override
    protected void initialize() {
        Robot.manipulator.getIntake().stop();
    }

    @Override
    protected void execute() {
        if(useJoystick){
            //Structured like this since we want the driver controller to have priority over the intake
            //Shows the priority of controls
            if (Robot.oi.xBoxController.leftBumper.get()) {
                power = IntakePower.INTAKE.power;
            } else if (Robot.oi.xBoxController.rightBumper.get()){
                power = IntakePower.OUTAKE.power;
            } else if (Math.abs(Robot.oi.flightController.getRotateAxisValue()) > 0.5){
                //We only want the flight controller to outtake slowly hence the negative and the 0.25
                power = -Math.abs(Robot.oi.flightController.getRotateAxisValue()) * 0.25;
            } else {
                power = 0;
            }

            Robot.oi.xBoxController.setRumble(power > 0);
        }

        Robot.manipulator.getIntake().setAll(power);
    }

    @Override
    protected void interrupted() {
        end();
    }

    @Override
    protected void end() {
        Robot.manipulator.getIntake().stop();
        Robot.oi.xBoxController.setRumble(false);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}