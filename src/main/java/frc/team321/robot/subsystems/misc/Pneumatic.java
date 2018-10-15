package frc.team321.robot.subsystems.misc;

import frc.team321.robot.RobotMap;
import frc.team321.robot.commands.subsystems.misc.RegulateCompressor;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Pneumatic extends Subsystem {

    private Compressor compressor;

    private static Pneumatic instance;

    private Pneumatic(){
        compressor = new Compressor(RobotMap.COMPRESSOR);
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new RegulateCompressor());
    }

    /**
     * Regulates the compressor
     */
    public void regulateCompressor(){
        if(!compressor.getPressureSwitchValue() && !compressor.enabled()
                && isCompressorSafeToUse()){
            compressor.start();
        }else if(compressor.getPressureSwitchValue() && compressor.enabled()
                || !isCompressorSafeToUse()){
            compressor.stop();
        }
    }

    /**
     * Checks if compressor is safe to use
     * @return whether or not the compressor is safe to use
     */
    private boolean isCompressorSafeToUse(){
        return !((compressor.getCompressorCurrentTooHighFault() && !compressor.getCompressorCurrentTooHighStickyFault()) ||
                (compressor.getCompressorNotConnectedFault() && !compressor.getCompressorNotConnectedStickyFault()) ||
                (compressor.getCompressorShortedFault() && !compressor.getCompressorShortedStickyFault()));
    }

    public void stopCompressor(){
        compressor.stop();
    }

    public synchronized static Pneumatic getInstance(){
        if(instance == null){
            instance = new Pneumatic();
        }

        return instance;
    }
}