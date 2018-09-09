package frc.team321.robot;

public class Constants {
    // TalonSRXs
    public static final int LEFT_MASTER_MOTOR = 10;
    public static final int LEFT_SLAVE_1 = 5;
    public static final int LEFT_SLAVE_2 = 12;

    public static final int RIGHT_MASTER_MOTOR = 9;
    public static final int RIGHT_SLAVE_1 = 4;
    public static final int RIGHT_SLAVE_2 = 8;

    public static final int LINEAR_MASTER = 11;
    public static final int LINEAR_SLAVE = 2;

    public static final int INTAKE_LEFT = 3;
    public static final int INTAKE_RIGHT = 7;

    // Compressor
    public static final int COMPRESSOR = 0;

    // Pneumatic
    public static final int GEARSHIFTER_FORWARD = 0;
    public static final int GEARSHIFTER_REVERSE = 1;

    public static final int INTAKE_PIVOT_FORWARD = 4;
    public static final int INTAKE_PIVOT_REVERSE = 5;

    // Sensors
    public static final int ULTRASONIC_TRIG = 0;
    public static final int ULTRASONIC_ECHO = 1;

    public static final int TOP_TOUCH_SENSOR = 8;
    public static final int BOTTOM_TOUCH_SENSOR = 9;

    // Drivetrain Constants
    public static final int DRIVETRAIN_TIMEOUT_MS = 10;
    public static final int DRIVETRAIN_PID_SLOT_INDEX = 0;
    public static final int DRIVETRAIN_PRIMARY_PID_LOOP = 0;

    public static final int DRIVETRAIN_ENCODER_TICKS_PER_REVOLUTION = 4096;
    public static final double DRIVETRAIN_MAX_RPM = 700.63;

    public static final double DRIVETRAIN_MOTION_PROFILE_P = 0.6;
    public static final double DRIVETRAIN_MOTION_PROFILE_I = 0;
    public static final double DRIVETRAIN_MOTION_PROFILE_D = 0;

    public static final double DRIVETRAIN_WHEEL_DIAMETER_INCHES = 5;
}
