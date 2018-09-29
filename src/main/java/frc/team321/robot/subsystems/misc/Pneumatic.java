package frc.team321.robot.subsystems.misc;

import frc.team321.robot.Constants;
import frc.team321.robot.commands.subsystems.misc.RegulateCompressor;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Pneumatic extends Subsystem {

    private Compressor compressor;

    public Pneumatic(){
        compressor = new Compressor(Constants.COMPRESSOR);
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
}