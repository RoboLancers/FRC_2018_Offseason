package frc.team321.robot.subsystems.manipulator;

public enum IntakePower {
    INTAKE(0.87), OUTAKE(-0.7), STOP(0);

    public double power;

    IntakePower(double power){
        this.power = power;
    }
}