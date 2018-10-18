package frc.team321.robot.commands.autonomous.subroutine;

import static frc.team321.robot.Constants.*;

import edu.wpi.first.wpilibj.command.Command;
import frc.team321.robot.Robot;
import frc.team321.robot.subsystems.drivetrain.Drivetrain;
import frc.team321.robot.subsystems.misc.Sensors;
import frc.team321.robot.utilities.RobotUtil;

@SuppressWarnings("FieldCanBeLocal")
public class MoveRobot extends Command {

    private double leftPower, rightPower;
    private double angle, power, error;

    public MoveRobot(double power, double angle){
        requires(Drivetrain.getInstance());

        this.angle = angle;
        this.power = power;
    }

    @Override
    protected void initialize(){
        Drivetrain.getInstance().stop();
        Sensors.getInstance().resetNavX();
    }

    @Override
    protected void execute(){
        error = Sensors.getInstance().getAngle() - angle;
        leftPower = RobotUtil.range(power + (error * DRIVETRAIN_KP), 1);
        rightPower = RobotUtil.range(power - (error * DRIVETRAIN_KP), 1);

        Drivetrain.getInstance().setLeft(leftPower);
        Drivetrain.getInstance().setRight(rightPower);
    }

    @Override
    protected boolean isFinished(){
        return false;
    }

    @Override
    protected void end(){
        Drivetrain.getInstance().stop();
    }
}
