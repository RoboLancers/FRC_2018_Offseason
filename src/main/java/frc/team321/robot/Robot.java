package frc.team321.robot;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team321.robot.subsystems.drivetrain.Drivetrain;
import frc.team321.robot.subsystems.drivetrain.GearShifter;
import frc.team321.robot.subsystems.manipulator.Intake;
import frc.team321.robot.subsystems.manipulator.IntakePivot;
import frc.team321.robot.subsystems.manipulator.LinearSlide;
import frc.team321.robot.subsystems.misc.Camera;
import frc.team321.robot.subsystems.misc.Pneumatic;
import frc.team321.robot.subsystems.misc.Sensors;
import frc.team321.robot.utilities.GripPipeline;
import frc.team321.robot.utilities.motion.Odometry;
import frc.team321.robot.utilities.RobotUtil;
import jaci.pathfinder.Pathfinder;

public class Robot extends TimedRobot {
    private static Odometry odometry;
    private Command autonomousCommand;

    @Override
    public void robotInit() {
        LiveWindow.disableAllTelemetry();

        Drivetrain.getInstance();
        GearShifter.getInstance();

        Intake.getInstance();
        IntakePivot.getInstance();
        LinearSlide.getInstance();

        Pneumatic.getInstance();
        Sensors.getInstance();

        OI.getInstance();

        Camera.getInstance().start();

        odometry = Odometry.getInstance();
        new Notifier(() -> {
            odometry.setCurrentEncoderPosition((Drivetrain.getInstance().getLeft().getEncoderCount() + Drivetrain.getInstance().getRight().getEncoderCount()) / 2.0);
            odometry.setDeltaPosition(RobotUtil.encoderTicksToFeets(odometry.getCurrentEncoderPosition() - odometry.getLastPosition()));
            odometry.setTheta(Math.toRadians(Pathfinder.boundHalfDegrees(Sensors.getInstance().getAngle())));

            odometry.addX(Math.cos(odometry.getTheta()) * odometry.getDeltaPosition());
            odometry.addY(Math.sin(odometry.getTheta()) * odometry.getDeltaPosition());

            odometry.setLastPosition(odometry.getCurrentEncoderPosition());
        }).startPeriodic(0.01);
    }

    @Override
    public void autonomousInit() {
        Drivetrain.getInstance().enableRamping(false);
        GearShifter.getInstance().setLowGear();
        Drivetrain.getInstance().setMode(NeutralMode.Brake);
        IntakePivot.getInstance().setUp();
        Sensors.getInstance().resetNavX();

        OI.liveDashboardTable.getEntry("Reset").setBoolean(true);

        autonomousCommand = OI.getInstance().getAutoCommand(OI.getInstance().getAutoMode());

        if(autonomousCommand != null){
            autonomousCommand.start();
        }
    }

    @Override
    public void teleopInit() {
        if(autonomousCommand != null){
            autonomousCommand.cancel();
        }

        Drivetrain.getInstance().setMode(NeutralMode.Brake);
        GearShifter.getInstance().setLowGear();
        Drivetrain.getInstance().enableRamping(true);
        IntakePivot.getInstance().setDown();
    }

    @Override
    public void testInit(){
        IntakePivot.getInstance().setUp();
    }

    @Override
    public void robotPeriodic(){
        OI.getInstance().updateDashboardValues();
        Scheduler.getInstance().run();
    }
}