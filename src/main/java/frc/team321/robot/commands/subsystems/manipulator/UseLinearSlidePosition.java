package frc.team321.robot.commands.subsystems.manipulator;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team321.robot.subsystems.manipulator.LinearSlide;
import frc.team321.robot.utilities.RobotUtil;
import frc.team321.robot.utilities.enums.LinearSlidePosition;

public class UseLinearSlidePosition extends Command {

    private int position;
    private int error;

    private UseLinearSlidePosition(int position){
        requires(LinearSlide.getInstance());
        this.position = position;
    }

    public UseLinearSlidePosition(LinearSlidePosition linearSlidePosition){
        this(linearSlidePosition.position);
    }

    @Override
    protected void initialize(){
        LinearSlide.getInstance().setRamping(false);
        LinearSlide.getInstance().setPosition(position);
    }

    @Override
    protected void end(){
        LinearSlide.getInstance().setRamping(true);
    }

    @Override
    protected boolean isFinished() {
        return Math.abs(LinearSlide.getInstance().getError() - position) < 100;
    }
}
