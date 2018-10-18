package frc.team321.motion;

import frc.team321.robot.Constants;
import frc.team321.robot.utilities.motion.Odometry;
import frc.team321.robot.utilities.motion.RamseteFollower;
import frc.team321.robot.utilities.motion.Velocity;
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

        double freq = 50.0;
        double dt = 1/freq;
        double time = 0.0;

        RamseteFollower ramseteFollower = new RamseteFollower("SideSwitchLeftAuto", 0.18, 0.9);
        ramseteFollower.setInitialOdometry();

        while(!ramseteFollower.isFinished()){
            Velocity velocity = ramseteFollower.getVelocity();
            double heading = velocity.getAngular() * dt;
            double pos = velocity.getLinear() * dt;

            Odometry.getInstance().addTheta(heading);

            double dx = pos * Math.cos(Odometry.getInstance().getTheta());
            double dy = pos * Math.sin(Odometry.getInstance().getTheta());

            Odometry.getInstance().addX(dx);
            Odometry.getInstance().addY(dy);

            xDataFollower.add(Odometry.getInstance().getX());
            yDataFollower.add(Odometry.getInstance().getY());
            xDataPath.add(ramseteFollower.currentSegment().x);
            yDataPath.add(ramseteFollower.currentSegment().y);

            time += dt;

            double left = (-(velocity.getAngular() * Constants.DRIVETRAIN_WHEELBASE) + (2 * velocity.getLinear())) / 2;
            double right = ((velocity.getAngular() * Constants.DRIVETRAIN_WHEELBASE) + (2 * velocity.getLinear())) / 2;
            System.out.println("Generating: " + time);
            System.out.println("Odometry: " + Odometry.getInstance());
            System.out.println("dx: " + dx);
            System.out.println("dy: " + dy);
            System.out.println("Left Velocity: " + left);
            System.out.println("Right Velocity: " + right);
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
    }
}
