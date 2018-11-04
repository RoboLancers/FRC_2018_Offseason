package frc.team321.robot.commands.subsystems.drivetrain;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team321.robot.subsystems.drivetrain.Drivetrain;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import java.util.ArrayList;

public class DrivetrainVelocityCharacterizationCommand extends Command {
    private double leftVoltage, rightVoltage;
    private double leftVIntercept, rightVIntercept;

    private SimpleRegression regression;

    private ArrayList<Double> leftVoltages;
    private ArrayList<Double> leftVelocities;

    private ArrayList<Double> rightVoltages;
    private ArrayList<Double> rightVelocities;

    private Timer timer;

    private double currentTime;

    public DrivetrainVelocityCharacterizationCommand(){
        requires(Drivetrain.getInstance());

        regression = new SimpleRegression();

        leftVoltages = new ArrayList<>();
        leftVelocities = new ArrayList<>();

        rightVoltages = new ArrayList<>();
        rightVelocities = new ArrayList<>();

        timer = new Timer();
    }

    @Override
    protected void initialize(){
        leftVoltage = 0;
        leftVIntercept = 0;

        rightVoltage = 0;
        rightVIntercept = 0;

        rightVoltages.clear();
        rightVelocities.clear();

        leftVoltages.clear();
        leftVelocities.clear();

        regression.clear();

        currentTime = 0;

        timer.start();
    }

    @Override
    protected void execute(){
        if(timer.get() > currentTime + 1) {
            currentTime = timer.get();

            if (Drivetrain.getInstance().getLeft().getVelocity() > 0.01) {
                if (leftVIntercept == 0.0) {
                    leftVIntercept = leftVoltage;
                }

                leftVoltages.add(leftVoltage);
                leftVelocities.add(Drivetrain.getInstance().getLeft().getVelocity());
            }

            if (Drivetrain.getInstance().getRight().getVelocity() > 0.01) {
                if (rightVIntercept == 0.0) {
                    rightVIntercept = rightVoltage;
                }

                rightVoltages.add(rightVoltage);
                rightVelocities.add(Drivetrain.getInstance().getRight().getVelocity());
            }

            leftVoltage += 0.25;
            rightVoltage += 0.25;

            Drivetrain.getInstance().setLeft(leftVoltage / 12.0);
            Drivetrain.getInstance().setRight(rightVoltage / 12.0);
        }
    }

    @Override
    protected void end(){
        Drivetrain.getInstance().stop();

        for(int i = 0; i < leftVoltages.size(); i++){
            regression.addData(leftVoltages.get(i), leftVelocities.get(i));
        }

        SmartDashboard.putNumber("Left kV", 1/regression.getSlope());
        SmartDashboard.putNumber("Left kS", leftVIntercept);
        SmartDashboard.putNumber("Left Linearity", regression.getRSquare());

        regression.clear();

        for(int i = 0; i < rightVoltages.size(); i++){
            regression.addData(rightVoltages.get(i), rightVelocities.get(i));
        }

        SmartDashboard.putNumber("Right kV", 1/regression.getSlope());
        SmartDashboard.putNumber("Right kS", leftVIntercept);
        SmartDashboard.putNumber("Right Linearity", regression.getRSquare());
    }

    @Override
    protected boolean isFinished() {
        return leftVoltage > 12.0 && rightVoltage > 12.0;
    }
}
