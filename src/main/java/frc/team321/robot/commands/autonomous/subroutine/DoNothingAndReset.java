package frc.team321.robot.commands.autonomous.subroutine;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.team321.robot.subsystems.drivetrain.Drivetrain;
import frc.team321.robot.subsystems.misc.Sensors;

public class DoNothingAndReset extends InstantCommand {
    @Override
    protected void initialize(){
        Sensors.getInstance().resetNavX();
        Drivetrain.getInstance().resetEncoders();
    }
}
