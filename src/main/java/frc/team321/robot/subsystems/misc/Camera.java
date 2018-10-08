package frc.team321.robot.subsystems.misc;

import edu.wpi.first.wpilibj.CameraServer;

public class Camera {

    public Camera(){
        CameraServer cameraServer = CameraServer.getInstance();
        cameraServer.startAutomaticCapture(0);
        cameraServer.startAutomaticCapture(1);
    }
}
