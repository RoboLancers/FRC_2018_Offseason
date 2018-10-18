package frc.team321.robot.utilities.motion;

public class Velocity {
    private double linear;
    private double angular;

    Velocity(double linear, double angular){
        this.linear = linear;
        this.angular = angular;
    }

    public double getLinear() {
        return linear;
    }

    public double getAngular() {
        return angular;
    }
}
