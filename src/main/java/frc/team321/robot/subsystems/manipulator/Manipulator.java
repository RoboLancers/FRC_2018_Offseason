package frc.team321.robot.subsystems.manipulator;

public class Manipulator {

    private Intake intake;
    private IntakePivot intakePivot;
    private LinearSlide linearSlide;

    public Manipulator(){
        intake = Intake.getInstance();
        intakePivot = IntakePivot.getInstance();
        linearSlide = LinearSlide.getInstance();
    }

    public Intake getIntake() {
        return intake;
    }

    public IntakePivot getIntakePivot() {
        return intakePivot;
    }

    public LinearSlide getLinearSlide() {
        return linearSlide;
    }
}
