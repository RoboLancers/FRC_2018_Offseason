package frc.team321.robot.utilities.controllers;

import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.buttons.Button;

@SuppressWarnings("FieldCanBeLocal")
public class XBoxController extends BaseController {

    private static int LEFT_X_ID = 0;
    private static int LEFT_Y_ID = 1;
    private static int LT_ID = 2;
    private static int RT_ID = 3;
    private static int RIGHT_X_ID = 4;
    private static int RIGHT_Y_ID = 5;
    private static int A_ID = 1;
    private static int B_ID = 2;
    private static int X_ID = 3;
    private static int Y_ID = 4;
    private static int LB_ID = 5;
    private static int RB_ID = 6;
    private static int SELECT_ID = 7;
    private static int START_ID = 8;
    private static int LEFT_JOY_BTN_ID = 9;
    private static int RIGHT_JOY_BTN_ID = 10;
    private double tolerance = 0.15;

    public Button A, B, X, Y, leftBumper, rightBumper, select, start, leftJoyBtn, rightJoyBtn;
    public LancerTrigger leftLancerTrigger, rightLancerTrigger;

    public XBoxController(int port) {
        super(port);

        A = this.buttons[A_ID];
        B = this.buttons[B_ID];
        X = this.buttons[X_ID];
        Y = this.buttons[Y_ID];
        leftBumper = this.buttons[LB_ID];
        rightBumper = this.buttons[RB_ID];
        select = this.buttons[SELECT_ID];
        start = this.buttons[START_ID];
        leftJoyBtn = this.buttons[LEFT_JOY_BTN_ID];
        rightJoyBtn = this.buttons[RIGHT_JOY_BTN_ID];

        leftLancerTrigger = new LancerTrigger(this.joystick, LT_ID, 0.10);
        rightLancerTrigger = new LancerTrigger(this.joystick, RT_ID, 0.10);
    }

    public double getLeftTriggerValue() {
        if (Math.abs(this.joystick.getRawAxis(LT_ID)) > tolerance) {
            return getRawLeftTriggerValue();
        } else {
            return 0;
        }
    }

    public double getRawLeftTriggerValue() {
        return this.joystick.getRawAxis(LT_ID);
    }

    public double getRightTriggerValue() {
        if (Math.abs(this.joystick.getRawAxis(RT_ID)) > tolerance) {
            return getRawRightTriggerValue();
        } else {
            return 0;
        }
    }

    public double getRawRightTriggerValue() {
        return this.joystick.getRawAxis(LT_ID);
    }

    public double getLeftYAxisValue() {
        if (Math.abs(this.joystick.getRawAxis(LEFT_Y_ID)) > tolerance) {
            return -getRawLeftYAxisValue();
        } else {
            return 0;
        }
    }

    public double getRawLeftYAxisValue() {
        return this.joystick.getRawAxis(LEFT_Y_ID);
    }

    public double getRightYAxisValue() {
        if (Math.abs(this.joystick.getRawAxis(RIGHT_Y_ID)) > tolerance) {
            return -getRawRightYAxisValue();
        } else {
            return 0;
        }
    }

    public double getRawRightYAxisValue() {
        return this.joystick.getRawAxis(RIGHT_Y_ID);
    }

    public double getLeftXAxisValue() {
        if (Math.abs(this.joystick.getRawAxis(LEFT_X_ID)) > tolerance) {
            return this.joystick.getRawAxis(LEFT_X_ID);
        } else {
            return 0;
        }
    }

    public double getRawLeftXAxisValue() {
        return this.joystick.getRawAxis(LEFT_X_ID);
    }

    public double getRightXAxisValue() {
        if (Math.abs(this.joystick.getRawAxis(RIGHT_X_ID)) > tolerance) {
            return this.joystick.getRawAxis(RIGHT_X_ID);
        } else {
            return 0;
        }
    }

    public double getRawRightXAxisValue() {
        return this.joystick.getRawAxis(RIGHT_X_ID);
    }

    public void setRumble(boolean rumble) {
        if (rumble) {
            this.joystick.setRumble(RumbleType.kRightRumble, 1);
            this.joystick.setRumble(RumbleType.kLeftRumble, 1);
        } else {
            this.joystick.setRumble(RumbleType.kRightRumble, 0);
            this.joystick.setRumble(RumbleType.kLeftRumble, 0);
        }
    }

    public void setRumble(double rumblePower) {
        this.joystick.setRumble(RumbleType.kRightRumble, Math.abs(rumblePower));
        this.joystick.setRumble(RumbleType.kLeftRumble, Math.abs(rumblePower));
    }
}
