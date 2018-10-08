package frc.team321.robot;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.team321.robot.subsystems.drivetrain.Drivetrain;
import frc.team321.robot.subsystems.manipulator.Manipulator;
import frc.team321.robot.subsystems.misc.Camera;
import frc.team321.robot.subsystems.misc.Pneumatic;

public class Robot extends TimedRobot {

    public static Drivetrain drivetrain;
    public static Manipulator manipulator;
    public static OI oi;
    public static Pneumatic pneumatic;
    public static Camera camera;

    private Command autonomousCommand;

    @Override
    public void robotInit() {
        camera = new Camera();

        oi = new OI();
        oi.putAutoModes();
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