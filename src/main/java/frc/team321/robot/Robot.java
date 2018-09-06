package frc.team321.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.team321.robot.subsystems.drivetrain.Drivetrain;
import frc.team321.robot.subsystems.manipulator.Manipulator;
import frc.team321.robot.subsystems.misc.Pneumatic;

public class Robot extends IterativeRobot {

    public static Drivetrain drivetrain;
    public static Manipulator manipulator;
    public static OI oi;
    public static Pneumatic pneumatic;

    private Command autonomousCommand;

    @Override
    public void robotInit() {
        drivetrain = new Drivetrain();
        manipulator = new Manipulator();

        oi = new OI();
        oi.putAutoModes();
    }

    @Override
    public void autonomousInit() {
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
    }

    @Override
    public void robotPeriodic(){
        Scheduler.getInstance().run();
        oi.updateDashboardValues();
    }
    
    @Override
    public void autonomousPeriodic() { }

    @Override
    public void teleopPeriodic() { }
}