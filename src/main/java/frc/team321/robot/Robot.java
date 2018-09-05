package frc.team321.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.team321.robot.subsystems.drivetrain.Drivetrain;

public class Robot extends IterativeRobot {

    public static Drivetrain drivetrain;
    public static OI oi;

    private Command autonomousCommand;

    @Override
    public void robotInit() {
        drivetrain = new Drivetrain();

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