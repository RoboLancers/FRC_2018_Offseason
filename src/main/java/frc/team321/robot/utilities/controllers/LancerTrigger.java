package frc.team321.robot.utilities.controllers;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Trigger;
import edu.wpi.first.wpilibj.command.Command;

public class LancerTrigger extends Trigger {

    private Joystick joystick;
    private int triggerPort;
    private double tolerance;

    LancerTrigger (Joystick joystick, int triggerPort, double tolerance){
        this.joystick = joystick;
        this.triggerPort = triggerPort;
        this.tolerance = tolerance;
    }

    public boolean get() {
        return joystick.getRawAxis(triggerPort) >= tolerance;
    }

    public void whileHeld(final Command command) {
        whileActive(command);
    }

    public void whenPressed(final Command command) {
        whenActive(command);
    }
}