package frc.team321.robot.utilities;

public class Odometry {
    private double x, y, theta;

    public Odometry(double x, double y, double theta){
        this.x = x;
        this.y = y;
        this.theta = theta;
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

    public void addX(double x){
        this.x += x;
    }

    public void addY(double y){
        this.y += y;
    }

    public void addTheta(double theta){
        this.theta += theta;
    }

    public String toString(){
        return "X Position: " + x + " Y Position: " + y + " Heading: " + theta;
    }
}
