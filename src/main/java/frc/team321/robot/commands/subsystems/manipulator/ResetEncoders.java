package frc.team321.robot.commands.subsystems.manipulator;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.team321.robot.subsystems.manipulator.LinearSlide;

public class ResetEncoders extends InstantCommand {
    @Override
    protected void initialize(){
        LinearSlide.getInstance().resetEncoder();
    }
}
