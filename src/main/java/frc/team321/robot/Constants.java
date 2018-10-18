package frc.team321.robot;

public class Constants {
    // Drivetrain Constants
    public static final int DRIVETRAIN_TIMEOUT_MS = 0;
    public static final int DRIVETRAIN_PID_SLOT_INDEX = 0;
    public static final int DRIVETRAIN_PRIMARY_PID_LOOP = 0;

    public static final int DRIVETRAIN_ENCODER_TICKS_PER_REVOLUTION = 1024;
    public static final double DRIVETRAIN_MAX_RPM = 527.34;

    public static final double DRIVETRAIN_MOTION_PROFILE_P = 0.1;
    public static final double DRIVETRAIN_MOTION_PROFILE_I = 0;
    public static final double DRIVETRAIN_MOTION_PROFILE_D = 0.1;

    public static final double DRIVETRAIN_WHEEL_DIAMETER_FEETS = 0.51;

    public static final double DRIVETRAIN_WHEELBASE = 2.25;

    public static final double DRIVETRAIN_MAX_VELOCITY = 12; //12 feet per second

    public static final double DRIVETRAIN_KV = 1/DRIVETRAIN_MAX_VELOCITY;
    public static final double DRIVETRAIN_KA = 0.0;
    public static final double DRIVETRAIN_KF = 1.05900621;
    //public static final double DRIVETRAIN_KF = 1.0333333;

    public static final double DRIVETRAIN_ROTATE_P = 0.05;
    public static final double DRIVETRAIN_KP = 2;

    public static final int DRIVETRAIN_PEAK_CURRENT_LIMIT = 80;
    public static final int DRIVETRAIN_SUSTAINED_CURRENT_LIMIT = 36;
    public static final int DRIVETRAIN_PEAK_CURRENT_TIME_LIMIT = 10; //ms

    public static final int DRIVETRAIN_VOLTAGE_COMPENSATION = 12;
    public static final int DRIVETRAIN_FILTER_WINDOW_SAMPLE = 32;
}
