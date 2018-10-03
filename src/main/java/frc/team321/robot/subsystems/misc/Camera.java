package frc.team321.robot.subsystems.misc;

import edu.wpi.first.wpilibj.CameraServer;

public class Camera {

    private CameraServer cameraServer = CameraServer.getInstance();

    public Camera(){
        cameraServer.startAutomaticCapture(0);
        cameraServer.startAutomaticCapture(1);
    }
}
