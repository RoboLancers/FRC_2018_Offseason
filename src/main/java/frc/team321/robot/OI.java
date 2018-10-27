package frc.team321.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team321.robot.commands.autonomous.modes.CenterSwitchAuto;
import frc.team321.robot.commands.autonomous.modes.ScaleThenSwitch;
import frc.team321.robot.commands.autonomous.subroutine.*;
import frc.team321.robot.commands.autonomous.subroutine.SameSideScaleAuto;
import frc.team321.robot.commands.autonomous.subroutine.SideSwitchAuto;
import frc.team321.robot.commands.subsystems.manipulator.ResetEncoders;
import frc.team321.robot.commands.subsystems.manipulator.UseIntakePivot;
import frc.team321.robot.commands.subsystems.manipulator.UseLinearSlidePosition;
import frc.team321.robot.subsystems.drivetrain.Drivetrain;
import frc.team321.robot.subsystems.manipulator.LinearSlide;
import frc.team321.robot.subsystems.misc.Camera;
import frc.team321.robot.subsystems.misc.Sensors;
import frc.team321.robot.utilities.enums.IntakePivotState;
import frc.team321.robot.utilities.enums.LinearSlidePosition;
import frc.team321.robot.utilities.motion.Odometry;
import frc.team321.robot.utilities.RobotUtil;
import frc.team321.robot.utilities.controllers.FlightController;
import frc.team321.robot.utilities.controllers.XBoxController;

import java.util.Arrays;

public class OI {

    public XBoxController xBoxController;
    public FlightController flightController;

    public static NetworkTable liveDashboardTable = NetworkTableInstance.getDefault().getTable("Live Dashboard");

    private SendableChooser<String> chooser;

    private static final String[] autonomousModes = {
            "Center Switch Auto",
            "Scale Then Switch Left",
            "Scale Then Switch Right",
            "Side Switch Left",
            "Side Switch Right",
            "Same Side Scale Left Auto",
            "Same Side Scale Right Auto",
            "Test Velocity"
    };

    private static OI instance;

    private OI(){
        xBoxController = new XBoxController(0);
        flightController = new FlightController(1);

        flightController.farBottom.whileHeld(new UseIntakePivot(IntakePivotState.UP));
        flightController.farBottom.whenReleased(new UseIntakePivot(IntakePivotState.DOWN));

        flightController.innerTop.whenPressed(new UseLinearSlidePosition(LinearSlidePosition.SCALE));
        flightController.innerMiddle.whenPressed(new UseLinearSlidePosition(LinearSlidePosition.SWITCH));
        flightController.innerBottom.whenPressed(new UseLinearSlidePosition(LinearSlidePosition.BOTTOM));

        flightController.topLeft.whenPressed(new ResetEncoders());

        chooser = new SendableChooser<>();
        putAutoModes();

        Camera.getInstance().start();
    }

    /**
     * Updates the SmartDashboard
     */
    void updateDashboardValues(){
        SmartDashboard.putNumber("Linear Slide Encoder Count", LinearSlide.getInstance().getEncoderCount());
        SmartDashboard.putNumber("Linear Slide Encoder Speed", LinearSlide.getInstance().getEncoderVelocity());
        SmartDashboard.putNumber("Left Encoder", Drivetrain.getInstance().getLeft().getEncoderCount());
        SmartDashboard.putNumber("Right Encoder", Drivetrain.getInstance().getRight().getEncoderCount());
        SmartDashboard.putNumber("Left Encoder Feet", RobotUtil.encoderTicksToFeets(Drivetrain.getInstance().getLeft().getEncoderCount()));
        SmartDashboard.putNumber("Right Encoder Feet", RobotUtil.encoderTicksToFeets(Drivetrain.getInstance().getRight().getEncoderCount()));
        SmartDashboard.putNumber("Left Encoder Speed", Drivetrain.getInstance().getLeft().getVelocity());
        SmartDashboard.putNumber("Right Encoder Speed", Drivetrain.getInstance().getRight().getVelocity());
        SmartDashboard.putNumber("Hall Effect Sensor", Sensors.getInstance().hallEffect.getAverageValue());

        SmartDashboard.putNumber("Joystick Forward", xBoxController.getLeftYAxisValue());

        SmartDashboard.putNumber("Odometry X", Odometry.getInstance().getX());
        SmartDashboard.putNumber("Odometry Y", Odometry.getInstance().getY());
        SmartDashboard.putNumber("Odometry Theta", Odometry.getInstance().getTheta());

        liveDashboardTable.getEntry("Robot X").setNumber(Odometry.getInstance().getX());
        liveDashboardTable.getEntry("Robot Y").setNumber(Odometry.getInstance().getY());
        liveDashboardTable.getEntry("Robot Heading").setNumber(Odometry.getInstance().getTheta());
    }

    /**
     * Puts autonomous modes in SmartDashboard
     */
    private void putAutoModes(){
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
        if(mode == null){
            return new DoNothingAndReset();
        }

        switch(mode){
            case "Center Switch Auto":
                return new CenterSwitchAuto();
            case "ScaleThenSwitchLeft":
                return new ScaleThenSwitch(true);
            case "Scale Then Switch Right":
                return new ScaleThenSwitch(false);
            case "Side Switch Left":
                return new SideSwitchAuto(true);
            case "Side Switch Right":
                return new SideSwitchAuto(false);
            case "Same Side Scale Left Auto":
                return new SameSideScaleAuto(true);
            case "Same Side Scale Right Auto":
                return new SameSideScaleAuto(false);
            case "Test Velocity":
                return new TestVelocity();
            default:
                return new DoNothingAndReset();
        }
    }

    public synchronized static OI getInstance() {
        if(instance == null){
            instance = new OI();
        }

        return instance;
    }
}
