package frc.team321.robot.utilities;

import static frc.team321.robot.Constants.*;
import frc.team321.robot.Robot;

public class RobotUtil {
    /**
     * Limits value to a specified minimum and maximum
     *
     * @param value The value to check
     * @param min   The lower bound
     * @param max   The upper bound
     * @return Returns a number that is between min and max inclusive
     */
    public static double range(double value, double min, double max) {
        return Math.min(max, Math.max(min, value));
    }

    /**
     * Helper method for case where min and max are same number but one has a
     * negative sign
     *
     * @param value The value to limit
     * @param max   The maximum value the number can take
     * @return Returns a number that is between -max and -max inclusive
     * @see RobotUtil#range(double, double, double)
     */
    public static double range(double value, double max) {
        return range(value, -max, max);
    }

    /**
     * Floors a number to a specific number of decimal places
     *
     * @param num    The number to floor
     * @param places How many decimal places to use
     * @return Returns the number floored
     */
    public static double floor(double num, int places) {
        double multiplier = 1;

        for (int x = 0; x < places; x++) {
            multiplier *= 10;
        }

        num *= multiplier;

        return (int) num / multiplier;
    }

    /**
     * Keep the sign when squaring
     *
     * @param num The number to square
     * @return The number squared including signs
     */
    public static double squareKeepSign(double num) {
        return num < 0 ? -(num * num) : (num * num);
    }

    /**
     * Keep the sign when square rooting
     *
     * @param num The number to square root
     * @return The number square rooted including signs
     */
    public static double sqrtKeepSign(double num) {
        return num < 0 ? -(Math.sqrt(-num)) : Math.sqrt(num);
    }

    public static double feetsToEncoderTicks(double feet){
        return (feet * DRIVETRAIN_ENCODER_TICKS_PER_REVOLUTION) / (Math.PI * DRIVETRAIN_WHEEL_DIAMETER_FEETS);
    }

    public static double encoderTicksToFeets(double ticks){
        return (ticks / DRIVETRAIN_ENCODER_TICKS_PER_REVOLUTION) * (Math.PI * DRIVETRAIN_WHEEL_DIAMETER_FEETS);
    }

    public static double feetToMeters(double val) {
        return 100 * inchesToCentimeters(val / 12);
    }

    public static double inchesToCentimeters(double val) {
        return val * 0.393700787;
    }
}
