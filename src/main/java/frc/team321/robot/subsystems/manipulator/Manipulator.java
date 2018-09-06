package frc.team321.robot.subsystems.manipulator;

public class Manipulator {
    private LinearSlide linearSlide;
    private Intake intake;
    private IntakePivot intakePivot;

    public Manipulator() {
        linearSlide = new LinearSlide();
        intake = new Intake();
        intakePivot = new IntakePivot();
    }

    public LinearSlide getLinearSlide() {
        return linearSlide;
    }

    public Intake getIntake() {
        return intake;
    }

    public IntakePivot getIntakePivot() {
        return intakePivot;
    }
}