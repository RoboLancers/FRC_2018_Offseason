package frc.team321.robot.utilities.enums;

public enum IntakePower {
    PASSIVE(0.1), INTAKE(0.87), OUTAKE(-0.7), STOP(0);

    public double power;

    IntakePower(double power){
        this.power = power;
    }
}