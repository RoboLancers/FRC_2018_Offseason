package frc.team321.robot.commands.autonomous.subroutine;

import static frc.team321.robot.Constants.*;

import edu.wpi.first.wpilibj.command.Command;
import frc.team321.robot.Robot;
import frc.team321.robot.subsystems.drivetrain.Drivetrain;
import frc.team321.robot.subsystems.misc.Sensors;
import frc.team321.robot.utilities.RobotUtil;
import frc.team321.robot.utilities.motion.Odometry;
import jaci.pathfinder.Pathfinder;

@SuppressWarnings("FieldCanBeLocal")
public class MoveRobot extends Command {

    private double leftPower, rightPower;
    private double angle, power, error;
    private boolean useGyro;

    public MoveRobot(double power){
        requires(Drivetrain.getInstance());

        this.power = power;
        useGyro = false;
    }

    public MoveRobot(double power, double angle){
        requires(Drivetrain.getInstance());

        this.angle = angle;
        this.power = power;
        useGyro = true;
    }

    @Override
    protected void initialize(){
        Drivetrain.getInstance().stop();
        Sensors.getInstance().resetNavX();
    }

    @Override
    protected void execute(){
        if(useGyro) {
            error = Pathfinder.r2d(Odometry.getInstance().getTheta()) - angle;
            leftPower = RobotUtil.range(power + (error * DRIVETRAIN_ROTATE_P), 1);
            rightPower = RobotUtil.range(power - (error * DRIVETRAIN_ROTATE_P), 1);

            Drivetrain.getInstance().setLeft(leftPower);
            Drivetrain.getInstance().setRight(rightPower);
        }else{
            Drivetrain.getInstance().setAll(power);
        }
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
