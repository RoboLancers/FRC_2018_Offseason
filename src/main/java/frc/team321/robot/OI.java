package frc.team321.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team321.robot.commands.autonomous.modes.CenterSwitchAuto;
import frc.team321.robot.commands.autonomous.modes.DoNothingAndReset;
import frc.team321.robot.commands.autonomous.modes.SameSideScaleAuto;
import frc.team321.robot.commands.autonomous.modes.SideSwitchAuto;
import frc.team321.robot.commands.autonomous.subroutine.PathFollower;
import frc.team321.robot.commands.autonomous.subroutine.RamsetePathFollower;
import frc.team321.robot.subsystems.drivetrain.Drivetrain;
import frc.team321.robot.subsystems.manipulator.LinearSlide;
import frc.team321.robot.subsystems.misc.Camera;
import frc.team321.robot.subsystems.misc.Sensors;
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
            "Test Pathfinder with Jaci",
            "Ramsete Follower",
            "Side Switch Left",
            "Side Switch Right",
            "Center Switch Auto",
            "Same Side Scale Left Auto",
            "Same Side Scale Right Auto"
    };

    private static OI instance;

    private OI(){
        xBoxController = new XBoxController(0);
        flightController = new FlightController(1);

        chooser = new SendableChooser<>();
        putAutoModes();

        Camera camera = Camera.getInstance();
        camera.start();
    }

    /**
     * Updates the SmartDashboard
     */
    void updateDashboardValues(){
        SmartDashboard.putNumber("NavX Gyro", Sensors.getInstance().getAngle());
        SmartDashboard.putBoolean("Top Touch Sensor", Sensors.getInstance().isLinearSlideFullyExtended());
        SmartDashboard.putBoolean("Bottom Touch Sensor", Sensors.getInstance().isLinearSlideAtGround());
        SmartDashboard.putNumber("Linear Slide Encoder", LinearSlide.getInstance().getEncoder());
        SmartDashboard.putNumber("Left Encoder", Drivetrain.getInstance().getLeft().getEncoderCount());
        SmartDashboard.putNumber("Right Encoder", Drivetrain.getInstance().getRight().getEncoderCount());
        SmartDashboard.putNumber("Left Encoder Feet", RobotUtil.encoderTicksToFeets(Drivetrain.getInstance().getLeft().getEncoderCount()));
        SmartDashboard.putNumber("Right Encoder Feet", RobotUtil.encoderTicksToFeets(Drivetrain.getInstance().getRight().getEncoderCount()));
        SmartDashboard.putNumber("Left Encoder Speed", Drivetrain.getInstance().getLeft().getMaster().getSelectedSensorVelocity(0));
        SmartDashboard.putNumber("Right Encoder Speed", Drivetrain.getInstance().getRight().getMaster().getSelectedSensorVelocity(0));

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
            case "Test Pathfinder with Jaci":
                return new PathFollower("SideSwitchLeftAuto");
            case "Ramsete Follower":
                return new RamsetePathFollower("SideSwitchLeftAuto", 0.18, 0.9);
            case "Side Switch Left":
                return new SideSwitchAuto(true);
            case "Side Switch Right":
                return new SideSwitchAuto(false);
            case "Center Switch Auto":
                return new CenterSwitchAuto();
            case "Same Side Scale Left Auto":
                return new SameSideScaleAuto(true);
            case "Same Side Scale Right Auto":
                return new SameSideScaleAuto(false);
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
