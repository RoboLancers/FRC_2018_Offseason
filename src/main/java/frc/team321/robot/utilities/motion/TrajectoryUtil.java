package frc.team321.robot.utilities.motion;

import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Trajectory.Segment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class TrajectoryUtil {

    public static Trajectory reversePath(Trajectory original){
        ArrayList<Segment> segments = new ArrayList<>(Arrays.asList(original.segments));
        Collections.reverse(segments);

        double distance = segments.get(0).position;

        return new Trajectory(segments.stream()
                .map(segment -> new Segment(segment.dt, segment.x, segment.y, distance - segment.position, -segment.velocity, -segment.acceleration, -segment.jerk, segment.heading))
                .toArray(Segment[]::new));
    }
}
