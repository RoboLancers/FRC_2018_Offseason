package frc.team321.robot.utilities.controllers;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

abstract class BaseController {
    Joystick joystick;
    Button[] buttons;

    BaseController(int port) {
        this.joystick = new Joystick(port);
        this.initializeButtons();
    }

    private void initializeButtons() {
        buttons = new JoystickButton[13];

        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new JoystickButton(joystick, i);
        }
    }
}