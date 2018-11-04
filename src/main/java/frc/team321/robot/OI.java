package frc.team321.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team321.robot.commands.autonomous.modes.CenterSwitchAuto;
import frc.team321.robot.commands.autonomous.modes.FullScale;
import frc.team321.robot.commands.autonomous.modes.ScaleThenSwitch;
import frc.team321.robot.commands.autonomous.subroutine.*;
import frc.team321.robot.commands.autonomous.subroutine.SameSideScaleAuto;
import frc.team321.robot.commands.autonomous.modes.SideSwitchAuto;
import frc.team321.robot.commands.subsystems.manipulator.ResetEncoders;
import frc.team321.robot.commands.subsystems.manipulator.UseIntakePivot;
import frc.team321.robot.commands.subsystems.manipulator.UseLinearSlidePosition;
import frc.team321.robot.subsystems.drivetrain.Drivetrain;
import frc.team321.robot.subsystems.manipulator.LinearSlide;
import frc.team321.robot.utilities.enums.IntakePivotState;
import frc.team321.robot.utilities.enums.LinearSlidePosition;
import frc.team321.robot.utilities.enums.RobotStartingSide;
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
            "Same Side Scale Left",
            "Same Side Scale Right",
            "Full Scale Left",
            "Full Scale Right",
            "Baseline"
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
    }

    /**
     * Updates the SmartDashboard
     */
    void updateDashboardValues(){
        SmartDashboard.putNumber("Linear Slide Encoder Count", LinearSlide.getInstance().getEncoderCount());
        SmartDashboard.putNumber("Left Encoder Feet", RobotUtil.encoderTicksToFeets(Drivetrain.getInstance().getLeft().getEncoderCount()));
        SmartDashboard.putNumber("Right Encoder Feet", RobotUtil.encoderTicksToFeets(Drivetrain.getInstance().getRight().getEncoderCount()));
        SmartDashboard.putNumber("Left Encoder Speed", Drivetrain.getInstance().getLeft().getVelocity());
        SmartDashboard.putNumber("Right Encoder Speed", Drivetrain.getInstance().getRight().getVelocity());

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
            case "Scale Then Switch Left":
                return new ScaleThenSwitch(RobotStartingSide.LEFT);
            case "Scale Then Switch Right":
                return new ScaleThenSwitch(RobotStartingSide.RIGHT);
            case "Side Switch Left":
                return new SideSwitchAuto(RobotStartingSide.LEFT);
            case "Side Switch Right":
                return new SideSwitchAuto(RobotStartingSide.RIGHT);
            case "Same Side Scale Left":
                return new SameSideScaleAuto(RobotStartingSide.LEFT);
            case "Same Side Scale Right":
                return new SameSideScaleAuto(RobotStartingSide.RIGHT);
            case "Full Scale Left":
                return new FullScale(RobotStartingSide.LEFT);
            case "Full Scale Right":
                return new FullScale(RobotStartingSide.RIGHT);
            case "Baseline":
                return new MoveRobot(1, 0);
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
