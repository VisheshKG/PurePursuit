package purepursuit.algorithm;

import purepursuit.com.company.ComputerDebugging;
import purepursuit.com.company.FloatPoint;
import purepursuit.com.company.Range;
import purepursuit.org.opencv.core.Point;

import static purepursuit.com.company.Robot.*;
import static purepursuit.RobotUtilities.MovementVars.*;

import java.util.ArrayList;



public class RobotMovement {

    public static void followCurve(ArrayList<CurvePoint> allPoints, double followAngle) {

        CurvePoint followMe = getFollowPathPoint(allPoints, new Point(worldXPosition, worldYPosition),
                allPoints.get(0).followDistance); // ToDo we should not be always using the followDistance of first path point

        // display a line from Robot to followMe point on the debug client
        ComputerDebugging.sendKeyPoint(new FloatPoint(followMe.x, followMe.y));
        ComputerDebugging.sendLine(new FloatPoint(worldXPosition, worldYPosition), new FloatPoint(followMe.x, followMe.y));

        goToPosition(followMe.x, followMe.y, followMe.moveSpeed, followAngle, followMe.turnSpeed);
    }

    public static CurvePoint getFollowPathPoint(ArrayList<CurvePoint> pathPoints, Point robotLocation, double followRadius) {
        CurvePoint followMe = new CurvePoint(pathPoints.get(0));

        for (int i = 0; i < pathPoints.size() - 1; i++) {
            CurvePoint startLine = pathPoints.get(i);
            CurvePoint endLine = pathPoints.get(i + 1);

            // display complete path on the debug client
            ComputerDebugging.sendKeyPoint(new FloatPoint(endLine.x, endLine.y));
            ComputerDebugging.sendLine(new FloatPoint(startLine.x, startLine.y), new FloatPoint(endLine.x, endLine.y));

            ArrayList<Point> intersections = MathFunctions.lineCircleIntersection(robotLocation, followRadius,
                    startLine.toPoint(), endLine.toPoint());

            double closestAngle = 10000000;
            for (Point thisIntersection: intersections) {
                double angle = Math.atan2(thisIntersection.x - worldXPosition, thisIntersection.y - worldYPosition);
                double deltaAngle = Math.abs(MathFunctions.angleWrap(angle - worldAngle_rad));

                if (deltaAngle < closestAngle) {
                    closestAngle = deltaAngle;
                    followMe.setPoint(thisIntersection);
                }
            }
        }
        return followMe;
    }

    public static void goToPosition(double x, double y, double movementSpeed, double preferredAngle, double turnSpeed) {


        // TODO: need the starting position of the robot
        double distanceToTarget = Math.hypot(x - worldXPosition, y - worldYPosition);

        // The angle to get to correct vector (from 0)
        // TODO: need the starting position of the robot
        double absoluteAngleToTarget = Math.atan2(y - worldYPosition, x - worldXPosition);

        // The relative angle from the robot to the point
        // TODO: need the current angle of the robot
        double relativeAngletoPoint = MathFunctions.angleWrap(absoluteAngleToTarget - (worldAngle_rad - Math.toRadians(90)));


        double relativeXtoPoint = Math.cos(relativeAngletoPoint) * distanceToTarget;
        double relativeYtoPoint = Math.sin(relativeAngletoPoint) * distanceToTarget;


        // Normalize the power so that the robot is moving at a consistent rate
        // No matter how big the vector is, power is from 0 to 1, but same ratio as x/y to total
        double movementXPower = relativeXtoPoint /  (Math.abs(relativeXtoPoint) + Math.abs(relativeYtoPoint));
        double movementYPower = relativeYtoPoint / (Math.abs(relativeXtoPoint) + Math.abs(relativeYtoPoint));

        // Set the Power
        // TODO: incorporate robot + setting power (see robot.java in tutorial git)
        movement_x = movementXPower * movementSpeed;
        movement_y = movementYPower * movementSpeed;

        // Creates an angle for the robot to turn to starting at relativeAngletoPoint
        double relativeTurnAngle = relativeAngletoPoint - Math.toRadians(180) + preferredAngle;
        movement_turn = Range.clip(relativeTurnAngle / Math.toRadians(30), -1.0, 1.0) * turnSpeed;

        if (distanceToTarget < 10) {
            movement_turn = 0;
        }

    }

}
