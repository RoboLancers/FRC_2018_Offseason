package frc.team321.robot.subsystems.manipulator;

public class Manipulator {

    private Intake intake;
    private IntakePivot intakePivot;
    private LinearSlide linearSlide;

    private static Manipulator instance;

    private Manipulator(){
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

    public synchronized static Manipulator getInstance() {
        if (instance == null) {
            instance = new Manipulator();
        }

        return instance;
    }
}
