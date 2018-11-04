package frc.team321.robot.subsystems.misc;

import edu.wpi.first.wpilibj.CameraServer;

public class Camera {

    private static Camera instance;

    private CameraServer cameraServer;

    private Camera(){
        cameraServer = CameraServer.getInstance();
    }

    public void start(){
        cameraServer.startAutomaticCapture();
    }

    public synchronized static Camera getInstance() {
        if(instance == null){
            instance = new Camera();
        }

        return instance;
    }
}
