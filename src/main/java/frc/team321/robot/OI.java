package frc.team321.robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team321.robot.commands.autonomous.modes.DoNothingAndReset;
import frc.team321.robot.utilities.controllers.XBoxController;

import java.util.Arrays;

public class OI {

    public XBoxController xBoxController;

    private SendableChooser<String> chooser;

    private static String[] autonomousModes = {

    };

    public OI(){
        xBoxController = new XBoxController(0);

        chooser = new SendableChooser<>();
    }


    /**
     * Puts autonomous modes in SmartDashboard
     */
    public void putAutoModes(){
        Arrays.stream(autonomousModes).forEach(mode -> chooser.addObject(mode, mode));
        SmartDashboard.putData("Autonomous Mode", chooser);
    }

    /**
     * Gets the selected autonomous mode
     * @return Returns the selected autonomous mode as a String
     */
    public String getAutoMode(){
        return chooser.getSelected();
    }


    /**
     * Gets Command from the name of Autonomous
     * @param mode The autonomous mode to get
     * @return Returns the Command associated with the Autonomous
     */
    public Command getAutoCommand(String mode){
        switch(mode){
            default:
                return new DoNothingAndReset();
        }
    }
}
