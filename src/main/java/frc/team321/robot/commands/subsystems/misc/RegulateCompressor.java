package frc.team321.robot.commands.subsystems.misc;

import frc.team321.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import frc.team321.robot.subsystems.misc.Pneumatic;

public class RegulateCompressor extends Command {

    public RegulateCompressor() {
        requires(Pneumatic.getInstance());
    }

    @Override
    protected void execute() {
        Pneumatic.getInstance().regulateCompressor();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}