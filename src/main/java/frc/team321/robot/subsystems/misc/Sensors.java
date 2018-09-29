package frc.team321.robot.subsystems.misc;

import java.util.Arrays;

import static frc.team321.robot.Constants.*;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.Ultrasonic;

public class Sensors {

    private static AHRS navX;
    private static Ultrasonic ultrasonic;
    private static DigitalInput topTouchSensor;
    private static DigitalInput bottomTouchSensor;

    private static double[] ultrasonicBuffer = {0, 0, 0};

    static {
        navX = new AHRS(SerialPort.Port.kMXP);
        ultrasonic = new Ultrasonic(ULTRASONIC_TRIG, ULTRASONIC_ECHO);
        topTouchSensor = new DigitalInput(TOP_TOUCH_SENSOR);
        bottomTouchSensor = new DigitalInput(BOTTOM_TOUCH_SENSOR);

        navX.reset();
        navX.resetDisplacement();

        ultrasonic.setAutomaticMode(true);
        ultrasonic.setEnabled(true);
        ultrasonic.setDistanceUnits(Ultrasonic.Unit.kMillimeters);
    }

    public static boolean isLinearSlideFullyExtended() {
        return !topTouchSensor.get();
    }

    public static boolean isLinearSlideAtGround() {
        return !bottomTouchSensor.get();
    }

    public static double getDistanceInMeters() {
        return ultrasonic.getRangeMM() / 1000;
    }

    public static double getHeading(){
        return navX.getFusedHeading();
    }

    public static void resetNavX(){
        navX.reset();
        navX.zeroYaw();
    }

    public static double getAngle(){
        return navX.getAngle();
    }

    public static double getAverageDistanceInMeters() {
        for(int i = 0; i < ultrasonicBuffer.length; i++) {
            ultrasonicBuffer[i] = getDistanceInMeters();
        }

        return Arrays.stream(ultrasonicBuffer)
                .average()
                .getAsDouble();
    }
}