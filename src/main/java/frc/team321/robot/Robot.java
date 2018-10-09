package frc.team321.robot;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.team321.robot.subsystems.drivetrain.Drivetrain;
import frc.team321.robot.subsystems.misc.Sensors;
import frc.team321.robot.utilities.Odometry;
import frc.team321.robot.utilities.RobotUtil;
import jaci.pathfinder.Pathfinder;

public class Robot extends TimedRobot {
    private static Odometry odometry;

    private Command autonomousCommand;

    @Override
    public void robotInit() {
        odometry = Odometry.getInstance();
    }

    @Override
    public void autonomousInit() {
        Drivetrain.getInstance().enableRamping(false);
        Drivetrain.getInstance().getGearShifter().setLowGear();
        Drivetrain.getInstance().setMode(NeutralMode.Brake);

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

        Drivetrain.getInstance().setMode(NeutralMode.Coast);
        Drivetrain.getInstance().getGearShifter().setLowGear();
        Drivetrain.getInstance().enableRamping(true);
    }

    @Override
    public void robotPeriodic(){
        odometry.setCurrentEncoderPosition((Drivetrain.getInstance().getLeft().getEncoderCount() + Drivetrain.getInstance().getRight().getEncoderCount()) / 2.0);
        odometry.setDeltaPosition(RobotUtil.encoderTicksToFeets(odometry.getCurrentEncoderPosition() - odometry.getLastPosition()));
        odometry.setTheta(Math.toRadians(Pathfinder.boundHalfDegrees(Sensors.getAngle())));

        odometry.addX(Math.cos(odometry.getTheta()) * odometry.getDeltaPosition());
        odometry.addY(Math.sin(odometry.getTheta()) * odometry.getDeltaPosition());

        odometry.setLastPosition(odometry.getCurrentEncoderPosition());
    }

    @Override
    public void autonomousPeriodic(){
        Scheduler.getInstance().run();
        OI.getInstance().updateDashboardValues();
    }

    @Override
    public void teleopPeriodic(){
        Scheduler.getInstance().run();
        OI.getInstance().updateDashboardValues();
    }
}