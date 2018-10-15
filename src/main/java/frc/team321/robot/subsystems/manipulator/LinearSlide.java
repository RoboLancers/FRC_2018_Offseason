package frc.team321.robot.subsystems.manipulator;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import frc.team321.robot.RobotMap;
import frc.team321.robot.commands.subsystems.manipulator.UseLinearSlide;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team321.robot.subsystems.misc.Sensors;

public class LinearSlide extends Subsystem {

    private TalonSRX master;
    private static LinearSlide instance;

    private LinearSlide() {
        master = new TalonSRX(RobotMap.LINEAR_MASTER);

        TalonSRX slave = new TalonSRX(RobotMap.LINEAR_SLAVE);

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

    /**
     * Helper method to stop the linear slides
     */
    public void stop() {
        move(0);
    }

    /**
     * Sets power to the linear slide
     * @param power The power to set the linear slide at
     */
    public void move(double power) {
        if(power == 0) {
            master.set(ControlMode.PercentOutput, -0.05);
        }else {
            master.set(ControlMode.PercentOutput, power);
        }
    }

    /**
     * Gets the linear slide encoder's count
     * @return The linear slide encoder count
     */
    public double getEncoder() {
        if (Sensors.isLinearSlideAtGround()) {
            this.resetEncoder();
        }
        return master.getSelectedSensorPosition(0);
    }

    /**
     * Resets linear slide's encoder
     */
    public void resetEncoder() {
        master.setSelectedSensorPosition(0, 0, 0);
    }

    /**
     * Checks power against safe conditions to move
     * @param power Power to move at
     * @return whether or not it's safe to move
     */
    public boolean isSafeToMove(double power) {
        return (Sensors.isLinearSlideFullyExtended() && power > 0)
                || (Sensors.isLinearSlideAtGround() && power < 0);
    }

    public synchronized static LinearSlide getInstance(){
        if (instance == null) {
            instance = new LinearSlide();
        }

        return instance;
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new UseLinearSlide());
    }
}