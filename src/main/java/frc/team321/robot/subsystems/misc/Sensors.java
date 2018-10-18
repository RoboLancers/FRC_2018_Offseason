package frc.team321.robot.subsystems.misc;

import java.util.Arrays;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.Ultrasonic;
import frc.team321.robot.RobotMap;

public class Sensors {

    private AHRS navX;
    private Ultrasonic ultrasonic;
    private DigitalInput topTouchSensor;
    private DigitalInput bottomTouchSensor;

    private double[] ultrasonicBuffer = {0, 0, 0};

    private static Sensors instance;

    private Sensors(){
        navX = new AHRS(SerialPort.Port.kMXP);
        ultrasonic = new Ultrasonic(RobotMap.ULTRASONIC_TRIG, RobotMap.ULTRASONIC_ECHO);
        topTouchSensor = new DigitalInput(RobotMap.TOP_TOUCH_SENSOR);
        bottomTouchSensor = new DigitalInput(RobotMap.BOTTOM_TOUCH_SENSOR);

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
    public boolean isLinearSlideFullyExtended() {
        return !topTouchSensor.get();
    }

    /**
     * Checks if the linear slide is at bottom
     * @return if the linear slide is at bottom
     */
    public boolean isLinearSlideAtGround() {
        return !bottomTouchSensor.get();
    }

    /**
     * Gets the distance from ultrasonic sensor in meters
     * @return the meter from ultrasonic sensor
     */
    public double getDistanceInMeters() {
        return ultrasonic.getRangeMM() / 1000;
    }

    /**
     * Gets the heading from NavX
     * @return the heading from NavX
     */
    public double getHeading(){
        return navX.getFusedHeading();
    }

    /**
     * Resets the NavX
     */
    public void resetNavX(){
        navX.reset();
        navX.zeroYaw();
    }

    /**
     * Gets angle from NavX with CounterClockwise being positive and Clockwise being negative
     * @return the angle from NavX
     */
    public double getAngle(){
        return -navX.getAngle();
    }

    /**
     * Gets the average distance from ultrasonic
     * @return the average distance
     */
    public double getAverageDistanceInMeters() {
        for(int i = 0; i < ultrasonicBuffer.length; i++) {
            ultrasonicBuffer[i] = getDistanceInMeters();
        }

        return Arrays.stream(ultrasonicBuffer)
                .average()
                .getAsDouble();
    }

    public synchronized static Sensors getInstance(){
        if(instance == null){
            instance = new Sensors();
        }

        return instance;
    }
}