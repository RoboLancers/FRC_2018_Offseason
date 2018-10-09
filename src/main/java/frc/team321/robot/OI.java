package frc.team321.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team321.robot.commands.autonomous.modes.DoNothingAndReset;
import frc.team321.robot.commands.autonomous.subroutine.PathFollower;
import frc.team321.robot.commands.autonomous.subroutine.RamsetePathFollower;
import frc.team321.robot.subsystems.drivetrain.Drivetrain;
import frc.team321.robot.subsystems.misc.Camera;
import frc.team321.robot.subsystems.misc.Sensors;
import frc.team321.robot.utilities.Odometry;
import frc.team321.robot.utilities.controllers.FlightController;
import frc.team321.robot.utilities.controllers.XBoxController;

import java.util.Arrays;

public class OI {

    public XBoxController xBoxController;
    public FlightController flightController;

    public static NetworkTable liveDashboardTable = NetworkTableInstance.getDefault().getTable("Live Dashboard");

    private SendableChooser<String> chooser;

    private static final String[] autonomousModes = {
        "Test Pathfinder with Jaci", "Ramsete Follower"
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
        SmartDashboard.putNumber("NavX Gyro", Sensors.getAngle());
        SmartDashboard.putBoolean("Top Touch Sensor", Sensors.isLinearSlideFullyExtended());
        SmartDashboard.putBoolean("Bottom Touch Sensor", Sensors.isLinearSlideAtGround());
        SmartDashboard.putNumber("Left Encoder", Drivetrain.getInstance().getLeft().getEncoderCount());
        SmartDashboard.putNumber("Right Encoder", Drivetrain.getInstance().getRight().getEncoderCount());
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
                return new PathFollower("Test");
            case "Ramsete Follower":
                return new RamsetePathFollower("Test");
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
