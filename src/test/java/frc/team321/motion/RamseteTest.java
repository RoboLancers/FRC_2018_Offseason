package frc.team321.motion;

import frc.team321.robot.utilities.enums.MotionProfileDirection;
import frc.team321.robot.utilities.motion.*;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;

import java.io.File;
import java.util.ArrayList;

@SuppressWarnings("SuspiciousNameCombination")
public class RamseteTest {
    public static void main(String[] args){
        System.loadLibrary("pathfinderjava");
        ArrayList<Double> xDataFollower = new ArrayList<>();
        ArrayList<Double> yDataFollower = new ArrayList<>();
        ArrayList<Double> xDataPath = new ArrayList<>();
        ArrayList<Double> yDataPath = new ArrayList<>();

        ArrayList<Double> graphTime = new ArrayList<>();

        ArrayList<Double> linearVelocity = new ArrayList<>();
        ArrayList<Double> angularVelocity = new ArrayList<>();

        ArrayList<Double> leftPower = new ArrayList<>();
        ArrayList<Double> rightPower = new ArrayList<>();

        double freq = 50.0;
        double dt = 1/freq;
        double time = 0.0;

        RamseteFollower ramseteFollower = new RamseteFollower(TrajectoryUtil.getTrajectoryFromName("CubePileToLeftSwitch"), MotionProfileDirection.FORWARD);
        Odometry.getInstance().setInitialOdometry(TrajectoryUtil.getTrajectoryFromName("CubePileToLeftSwitch"));

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

            graphTime.add(time);
            linearVelocity.add(velocity.getLinear());
            angularVelocity.add(velocity.getAngular());

            time += dt;

            DriveSignal driveSignal = ramseteFollower.getNextDriveSignal();

            leftPower.add(driveSignal.getLeft());
            rightPower.add(driveSignal.getRight());

            System.out.println("Generating: " + time);
            System.out.println("Odometry: " + Odometry.getInstance());
            System.out.println("dx: " + dx);
            System.out.println("dy: " + dy);
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

        chart = new XYChartBuilder()
                .width(800)
                .height(600)
                .title("Ramsete Test")
                .xAxisTitle("Time")
                .yAxisTitle("Velocity")
                .build();

        chart.addSeries("Linear Velocity", graphTime, linearVelocity);
        chart.addSeries("Angular Velocity", graphTime, angularVelocity);

        new SwingWrapper<>(chart).displayChart();

        chart = new XYChartBuilder()
                .width(800)
                .height(600)
                .title("Ramsete Test")
                .xAxisTitle("Time (s)")
                .yAxisTitle("Velocity (m/s)")
                .build();

        chart.addSeries("Left Velocity (m/s)", graphTime, leftPower);
        chart.addSeries("Right Velocity (m/s)", graphTime, rightPower);

        new SwingWrapper<>(chart).displayChart();
    }
}
