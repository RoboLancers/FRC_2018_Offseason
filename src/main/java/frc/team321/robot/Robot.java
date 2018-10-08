package frc.team321.robot;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.team321.robot.subsystems.drivetrain.Drivetrain;
import frc.team321.robot.subsystems.manipulator.Manipulator;
import frc.team321.robot.subsystems.misc.Camera;
import frc.team321.robot.subsystems.misc.Pneumatic;
import frc.team321.robot.subsystems.misc.Sensors;
import frc.team321.robot.utilities.Odometry;
import frc.team321.robot.utilities.RobotUtil;
import jaci.pathfinder.Pathfinder;

public class Robot extends TimedRobot {

    public static Drivetrain drivetrain;
    public static Manipulator manipulator;
    public static OI oi;
    public static Pneumatic pneumatic;
    private Odometry odometry;
    public static Camera camera;

    private Command autonomousCommand;

    @Override
    public void robotInit() {
        drivetrain = Drivetrain.getInstance();
        manipulator = new Manipulator();
        camera = new Camera();
        pneumatic = new Pneumatic();
        odometry = Odometry.getInstance();

        oi = new OI();
        oi.putAutoModes();

        Notifier odometryNotifier = new Notifier(() -> {
            odometry.setCurrentEncoderPosition((drivetrain.getLeft().getEncoderCount() + drivetrain.getRight().getEncoderCount()) / 2.0);
            odometry.setDeltaPosition(RobotUtil.encoderTicksToFeets(odometry.getCurrentEncoderPosition() - odometry.getLastPosition()));
            odometry.setTheta(Math.toRadians(Pathfinder.boundHalfDegrees(Sensors.getAngle())));

            odometry.addX(Math.cos(odometry.getTheta()) * odometry.getDeltaPosition());
            odometry.addY(Math.sin(odometry.getTheta()) * odometry.getDeltaPosition());

            odometry.setLastPosition(odometry.getCurrentEncoderPosition());

            OI.liveDashboardTable.getEntry("Robot X").setNumber(odometry.getX());
            OI.liveDashboardTable.getEntry("Robot Y").setNumber(odometry.getY());
            OI.liveDashboardTable.getEntry("Robot Heading").setNumber(odometry.getTheta());
        });

        odometryNotifier.startPeriodic(0.01);
    }

    @Override
    public void autonomousInit() {
        drivetrain.enableRamping(false);
        drivetrain.getGearShifter().setLowGear();
        drivetrain.resetEncoders();
        drivetrain.setMode(NeutralMode.Brake);

        autonomousCommand = oi.getAutoCommand(oi.getAutoMode());

        if(autonomousCommand != null){
            autonomousCommand.start();
        }
    }

    @Override
    public void teleopInit() {
        if(autonomousCommand != null){
            autonomousCommand.cancel();
        }

        drivetrain.setMode(NeutralMode.Coast);
        drivetrain.getGearShifter().setLowGear();
        drivetrain.enableRamping(true);
    }

    @Override
    public void autonomousPeriodic(){
        Scheduler.getInstance().run();
        oi.updateDashboardValues();
    }

    @Override
    public void teleopPeriodic(){
        Scheduler.getInstance().run();
        oi.updateDashboardValues();
    }
}