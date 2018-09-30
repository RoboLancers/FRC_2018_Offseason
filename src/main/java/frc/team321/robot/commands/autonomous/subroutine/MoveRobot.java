package frc.team321.robot.commands.autonomous.subroutine;

import static frc.team321.robot.Constants.*;

import edu.wpi.first.wpilibj.command.Command;
import frc.team321.robot.Robot;
import frc.team321.robot.subsystems.misc.Sensors;
import frc.team321.robot.utilities.RobotUtil;

public class MoveRobot extends Command {

    double leftPower, rightPower;

    boolean useAngle;
    private double angle, power, error;

    public MoveRobot(double power, double angle){
        requires(Robot.drivetrain);

        this.angle = angle;
        this.power = power;
    }

    @Override
    protected void initialize(){
        Robot.drivetrain.stop();
        Sensors.resetNavX();
    }

    @Override
    protected void execute(){
        error = Sensors.getAngle() - angle;
        leftPower = RobotUtil.range(power + (error * DRIVETRAIN_PID_P), 1);
        rightPower = RobotUtil.range(power - (error * DRIVETRAIN_PID_P), 1);

        Robot.drivetrain.setLeft(leftPower);
        Robot.drivetrain.setRight(rightPower);
    }

    @Override
    protected boolean isFinished(){
        return false;
    }

    @Override
    protected void end(){
        Robot.drivetrain.stop();
    }

    @Override
    protected void interrupted(){
        end();
    }
}
