package frc.team321.motion;

import frc.team321.robot.Constants;
import frc.team321.robot.utilities.Odometry;
import frc.team321.robot.utilities.RamseteFollower;
import frc.team321.robot.utilities.Twist2D;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;

import java.util.ArrayList;

public class RamseteTest {
    public static void main(String[] args){
        System.loadLibrary("pathfinderjava");
        ArrayList<Double> xDataFollower = new ArrayList<>();
        ArrayList<Double> yDataFollower = new ArrayList<>();
        ArrayList<Double> xDataPath = new ArrayList<>();
        ArrayList<Double> yDataPath = new ArrayList<>();

        Waypoint[] waypointArray = {
                new Waypoint(-1.0, -1.0, 0.0),
                new Waypoint(10.0, -1.0, 0.0)
        };

        Odometry odometry = Odometry.getInstance();
        double freq = 50.0;
        double dt = 1/freq;
        double time = 0.0;

        Trajectory trajectory = Pathfinder.generate(waypointArray,
                new Trajectory.Config(
                        Trajectory.FitMethod.HERMITE_CUBIC,
                        Trajectory.Config.SAMPLES_HIGH,
                        dt,
                        Constants.DRIVETRAIN_MAX_VELOCITY,
                        12,
                        60
                ));

        RamseteFollower ramseteFollower = new RamseteFollower(trajectory);
        ramseteFollower.setInitialOdometry();

        while(!ramseteFollower.isFinished()){
            Twist2D twist2D = ramseteFollower.getTwist();
            double heading = twist2D.dtheta * dt;
            double pos = twist2D.dx * dt;
            double dx = pos * Math.cos(odometry.getTheta() + heading);
            double dy = pos * Math.sin(odometry.getTheta() + heading);

            odometry.addX(dx);
            odometry.addY(dy);
            odometry.setTheta(odometry.getTheta() + heading);

            xDataFollower.add(odometry.getX());
            yDataFollower.add(odometry.getY());
            xDataPath.add(ramseteFollower.currentSegment().x);
            yDataPath.add(ramseteFollower.currentSegment().y);

            time += dt;

            System.out.println("Generating: " + time);
            System.out.println("Odometry: " + odometry);
        }

        System.out.println("Finish generating path and follower");

        XYChart chart = new XYChartBuilder()
                .width(800)
                .height(600)
                .title("Ramsete Test")
                .xAxisTitle("X Position")
                .yAxisTitle("Y Position")
                .build();

        chart.addSeries("Path", xDataPath, yDataPath);
        chart.addSeries("Follower", xDataFollower, yDataFollower);

        System.out.println("Created Chart");

        new SwingWrapper<>(chart).displayChart();

//        try {
//            Thread.sleep(1000000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }
}
