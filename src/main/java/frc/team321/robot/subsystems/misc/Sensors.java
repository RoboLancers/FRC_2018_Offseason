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

    /**
     * Checks if the linear slide is at top
     * @return if the linear slide is at top
     */
    public static boolean isLinearSlideFullyExtended() {
        return !topTouchSensor.get();
    }

    /**
     * Checks if the linear slide is at bottom
     * @return if the linear slide is at bottom
     */
    public static boolean isLinearSlideAtGround() {
        return !bottomTouchSensor.get();
    }

    /**
     * Gets the distance from ultrasonic sensor in meters
     * @return the meter from ultrasonic sensor
     */
    public static double getDistanceInMeters() {
        return ultrasonic.getRangeMM() / 1000;
    }

    /**
     * Gets the heading from NavX
     * @return the heading from NavX
     */
    public static double getHeading(){
        return navX.getFusedHeading();
    }

    /**
     * Resets the NavX
     */
    public static void resetNavX(){
        navX.reset();
        navX.zeroYaw();
    }

    /**
     * Gets angle from NavX with CounterClockwise being positive and Clockwise being negative
     * @return the angle from NavX
     */
    public static double getAngle(){
        return -navX.getAngle();
    }

    /**
     * Gets the average distance from ultrasonic
     * @return the average distance
     */
    public static double getAverageDistanceInMeters() {
        for(int i = 0; i < ultrasonicBuffer.length; i++) {
            ultrasonicBuffer[i] = getDistanceInMeters();
        }

        return Arrays.stream(ultrasonicBuffer)
                .average()
                .getAsDouble();
    }
}