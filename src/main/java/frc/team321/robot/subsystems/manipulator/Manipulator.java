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

    /**
     * Returns the linear slide
     * @return the linear slide
     */
    public LinearSlide getLinearSlide() {
        return linearSlide;
    }

    /**
     * Returns the intake
     * @return The intake
     */
    public Intake getIntake() {
        return intake;
    }

    /**
     * Returns the intake pivot
     * @return the intake pivot
     */
    public IntakePivot getIntakePivot() {
        return intakePivot;
    }
}