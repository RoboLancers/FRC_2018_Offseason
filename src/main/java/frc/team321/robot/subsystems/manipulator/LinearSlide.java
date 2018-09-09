package frc.team321.robot.subsystems.manipulator;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import frc.team321.robot.Constants;
import frc.team321.robot.commands.subsystems.manipulator.UseLinearSlide;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team321.robot.subsystems.misc.Sensors;

public class LinearSlide extends Subsystem {

    private WPI_TalonSRX master;

    LinearSlide() {
        master = new WPI_TalonSRX(Constants.LINEAR_MASTER);

        WPI_TalonSRX slave = new WPI_TalonSRX(Constants.LINEAR_SLAVE);

        master.setNeutralMode(NeutralMode.Brake);
        slave.setNeutralMode(NeutralMode.Brake);
        slave.follow(master);

        master.configOpenloopRamp(0.5, 0);
        slave.configOpenloopRamp(0.5, 0);

        master.configPeakOutputForward(0.8, 0);

        master.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
        master.setSelectedSensorPosition(0, 0, 0);
        master.setSensorPhase(true);

        master.configNominalOutputForward(0.05, 0);
        slave.configNominalOutputForward(0.05, 0);

        master.setInverted(true);
        slave.setInverted(true);
    }

    public void stop() {
        move(0);
    }

    public void move(double power) {
        if(power == 0) {
            master.set(ControlMode.PercentOutput, -0.05);
        }else {
            master.set(ControlMode.PercentOutput, power);
        }
    }

    public double getEncoder() {
        if (Sensors.isLinearSlideAtGround()) {
            this.resetEncoder();
        }
        return master.getSelectedSensorPosition(0);
    }

    public void resetEncoder() {
        master.setSelectedSensorPosition(0, 0, 0);
    }

    public boolean isSafeToMove(double power) {
        return (Sensors.isLinearSlideFullyExtended() && power > 0)
                || (Sensors.isLinearSlideAtGround() && power < 0);
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new UseLinearSlide());
    }
}