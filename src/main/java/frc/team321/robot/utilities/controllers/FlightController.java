package frc.team321.robot.utilities.controllers;

import edu.wpi.first.wpilibj.buttons.Button;

@SuppressWarnings("FieldCanBeLocal")
public class FlightController extends BaseController {

    private final int STICK_X_ID = 0;
    private final int STICK_Y_ID = 1;
    private final int STICK_TURN_ID = 2;
    private final int RUDDER_ID = 3;
    private final int TRIGGER_ID = 1;
    private final int SHOOTER_ID = 2;
    private final int BOTTOM_LEFT_ID = 3;
    private final int BOTTOM_RIGHT_ID = 4;
    private final int TOP_LEFT_ID = 5;
    private final int TOP_RIGHT_ID = 6;
    private final int FAR_TOP_ID = 7;
    private final int INNER_TOP_ID = 8;
    private final int FAR_MIDDLE_ID = 9;
    private final int INNER_MIDDLE_ID = 10;
    private final int FAR_BOTTOM_ID = 11;
    private final int INNER_BOTTOM_ID = 12;

    public Button trigger, shooter,
            bottomLeft, bottomRight, topLeft, topRight,
            farTop, innerTop, farMiddle, innerMiddle, farBottom, innerBottom;

    private double tolerance = 0.25;

    public FlightController(int port) {
        super(port);
        trigger = this.buttons[TRIGGER_ID];
        shooter = this.buttons[SHOOTER_ID];
        bottomLeft = this.buttons[BOTTOM_LEFT_ID];
        bottomRight = this.buttons[BOTTOM_RIGHT_ID];
        topLeft = this.buttons[TOP_LEFT_ID];
        topRight = this.buttons[TOP_RIGHT_ID];
        farTop = this.buttons[FAR_TOP_ID];
        innerTop = this.buttons[INNER_TOP_ID];
        farMiddle = this.buttons[FAR_MIDDLE_ID];
        innerMiddle = this.buttons[INNER_MIDDLE_ID];
        farBottom = this.buttons[FAR_BOTTOM_ID];
        innerBottom = this.buttons[INNER_BOTTOM_ID];
    }

    public double getRawXAxisValue() {
        return this.joystick.getRawAxis(STICK_X_ID);
    }

    public double getRawYAxisValue() {
        return this.joystick.getRawAxis(STICK_Y_ID);
    }

    public double getRawRotateAxisValue() {
        return this.joystick.getRawAxis(STICK_TURN_ID);
    }

    public double getXAxisValue() {
        if (Math.abs(this.joystick.getRawAxis(STICK_X_ID)) > tolerance) {
            return this.joystick.getRawAxis(STICK_X_ID);
        } else {
            return 0;
        }
    }

    public double getYAxisValue() {
        if (Math.abs(this.joystick.getRawAxis(STICK_Y_ID)) > tolerance) {
            return this.joystick.getRawAxis(STICK_Y_ID);
        } else {
            return 0;
        }
    }

    public double getRotateAxisValue() {
        if (Math.abs(this.joystick.getRawAxis(STICK_TURN_ID)) > tolerance) {
            return this.joystick.getRawAxis(STICK_TURN_ID);
        } else {
            return 0;
        }
    }

    public double getRudderAxis() {
        return (this.joystick.getRawAxis(RUDDER_ID) + 1) / 4 + 0.5;
    }
}