package frc.team321.robot.utilities;

public class Odometry {
    private volatile double x, y, theta;
    private volatile double currentEncoderPosition, lastPosition, deltaPosition;

    private static Odometry instance;

    private Odometry(){
        this.x = 0;
        this.y = 0;
        this.theta = 0;

        this.currentEncoderPosition = 0;
        this.lastPosition = 0;
        this.deltaPosition = 0;
    }

    public synchronized static Odometry getInstance(){
        if(instance == null){
            instance = new Odometry();
        }

        return instance;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getTheta() {
        return theta % (2 * Math.PI);
    }

    public synchronized void setX(double x){
        this.x = x;
    }

    public synchronized void setY(double y){
        this.y = y;
    }

    public synchronized void addX(double x){
        this.x += x;
    }

    public synchronized void addY(double y){
        this.y += y;
    }

    public synchronized void setTheta(double theta){
        this.theta = theta;
    }

    public double getCurrentEncoderPosition() {
        return currentEncoderPosition;
    }

    public double getLastPosition() {
        return lastPosition;
    }

    public double getDeltaPosition() {
        return deltaPosition;
    }

    public synchronized void setCurrentEncoderPosition(double currentEncoderPosition) {
        this.currentEncoderPosition = currentEncoderPosition;
    }

    public synchronized void setLastPosition(double lastPosition) {
        this.lastPosition = lastPosition;
    }

    public synchronized void setDeltaPosition(double deltaPosition) {
        this.deltaPosition = deltaPosition;
    }

    public String toString(){
        return "X Position: " + x + " Y Position: " + y + " Heading: " + theta;
    }
}
