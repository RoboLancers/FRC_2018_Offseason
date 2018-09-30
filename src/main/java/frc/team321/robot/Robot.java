package frc.team321.robot;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.team321.robot.subsystems.drivetrain.Drivetrain;
import frc.team321.robot.subsystems.manipulator.Manipulator;
import frc.team321.robot.subsystems.misc.GripPipeline;
import frc.team321.robot.subsystems.misc.Pneumatic;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class Robot extends TimedRobot {

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

        new Thread(() -> {
            UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
            camera.setResolution(320, 240);
            camera.setExposureManual(50);

            CvSink cvSink = CameraServer.getInstance().getVideo();
            CvSource cvSource = CameraServer.getInstance().putVideo("Cube", 320, 240);

            Mat source = new Mat();
            Mat output = new Mat();

            GripPipeline gripPipeline = new GripPipeline();

            while(!Thread.interrupted()){
                cvSink.grabFrame(source);

                if(!source.empty()){
                    gripPipeline.process(source);
                    for(int i = 0; i < gripPipeline.findContoursOutput().size(); i++){
                        Imgproc.drawContours(source, gripPipeline.findContoursOutput(), i, new Scalar(0,0,0));
                    }
                }

                cvSource.putFrame(source);
            }
        }).start();
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