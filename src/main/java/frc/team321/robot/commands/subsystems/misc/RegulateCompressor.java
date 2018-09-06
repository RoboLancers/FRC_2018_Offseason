package frc.team321.robot.commands.subsystems.misc;

import frc.team321.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class RegulateCompressor extends Command {

    public RegulateCompressor() {
        requires(Robot.pneumatic);
    }

    @Override
    protected void execute() {
        Robot.pneumatic.regulateCompressor();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}