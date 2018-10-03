package frc.team321.robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team321.robot.commands.autonomous.modes.DoNothingAndReset;
import frc.team321.robot.commands.autonomous.subroutine.PathFollower;
import frc.team321.robot.subsystems.misc.Sensors;
import frc.team321.robot.utilities.controllers.FlightController;
import frc.team321.robot.utilities.controllers.XBoxController;
import frc.team321.robot.utilities.enums.MotionProfileDirection;

import java.util.Arrays;

public class OI {

    public XBoxController xBoxController;
    public FlightController flightController;

    private SendableChooser<String> chooser;

    private static final String[] autonomousModes = {
        "Test Pathfinder with CTRE", "Test Pathfinder with Jaci"
    };

    OI(){
        xBoxController = new XBoxController(0);
        flightController = new FlightController(1);

        chooser = new SendableChooser<>();
    }

    /**
     * Updates the SmartDashboard
     */
    void updateDashboardValues(){
        SmartDashboard.putNumber("NavX Gyro", Sensors.getAngle());
        SmartDashboard.putBoolean("Top Touch Sensor", Sensors.isLinearSlideFullyExtended());
        SmartDashboard.putBoolean("Bottom Touch Sensor", Sensors.isLinearSlideAtGround());
        SmartDashboard.putNumber("Left Encoder", Robot.drivetrain.getLeft().getEncoderCount());
        SmartDashboard.putNumber("Right Encoder", Robot.drivetrain.getRight().getEncoderCount());
        SmartDashboard.putNumber("Left Encoder Speed", Robot.drivetrain.getLeft().getMaster().getSelectedSensorVelocity(0));
        SmartDashboard.putNumber("Right Encoder Speed", Robot.drivetrain.getRight().getMaster().getSelectedSensorVelocity(0));
    }

    /**
     * Puts autonomous modes in SmartDashboard
     */
    void putAutoModes(){
        Arrays.stream(autonomousModes).forEach(mode -> chooser.addObject(mode, mode));
        SmartDashboard.putData("Autonomous Mode", chooser);
    }

    /**
     * Gets the selected autonomous mode
     * @return Returns the selected autonomous mode as a String
     */
    String getAutoMode(){
        return chooser.getSelected();
    }


    /**
     * Gets Command from the name of Autonomous
     * @param mode The autonomous mode to get
     * @return Returns the Command associated with the Autonomous
     */
    Command getAutoCommand(String mode){
        switch(mode){
            case "Test Pathfinder with Jaci":
                return new PathFollower("Test");
            default:
                return new DoNothingAndReset();
        }
    }
}
