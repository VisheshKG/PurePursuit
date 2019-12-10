package purepursuit.algorithm;

import purepursuit.org.opencv.core.Point;

import java.util.ArrayList;

public class MathFunctions {

    /**
     * Makes sure any angle is within the range -180 to 180
     * @param angle input angle any value typically result of addition or subtraction of angles
     * @return angle with range -180 to 180
     */
    public static double angleWrap(double angle) {
        while (angle < -Math.PI) {
            angle += (2 * Math.PI);
        }

        while (angle > Math.PI) {
            angle -= (2 * Math.PI);
        }

        return angle;
    }


    public static ArrayList<Point> lineCircleIntersection(Point circleCenter, double radius, Point linePoint1, Point linePoint2) {
        if (Math.abs(linePoint1.y - linePoint2.y) < 0.003) {
            linePoint1.y = linePoint2.y + 0.003;
        }
        if (Math.abs(linePoint1.x - linePoint2.x) < 0.003) {
            linePoint1.x = linePoint2.x + 0.003;
        }
        double m1 = (linePoint2.y - linePoint1.y) / (linePoint2.x - linePoint1.x);
        double x1 = linePoint1.x - circleCenter.x;
        double y1 = linePoint1.y - circleCenter.y;

        double quadraticA =  Math.pow(m1, 2) + 1.0;
        double quadraticB = (2.0 * m1 * y1)  - (2.0 * Math.pow(m1, 2) * x1);
        double quadraticC = (Math.pow(m1, 2) * Math.pow(x1, 2)) - (2.0 * y1 * m1 * x1) + Math.pow(y1, 2) - Math.pow(radius, 2);

        double discriminantSqRoot = Math.sqrt(Math.pow(quadraticB, 2) - (4.0 * quadraticA * quadraticC));

        ArrayList<Point> allPoints = new ArrayList<>();

        if (!Double.isNaN(discriminantSqRoot)) {
            double xRoot1 = (-quadraticB + discriminantSqRoot) / (2.0 * quadraticA);
            double yRoot1 = m1 * (xRoot1 - x1) + y1;

            xRoot1 += circleCenter.x;
            yRoot1 += circleCenter.y;

            double minX = Math.min(linePoint1.x, linePoint2.x);
            double maxX = Math.max(linePoint1.x, linePoint2.x);

            if (xRoot1 > minX && xRoot1 < maxX) {
                allPoints.add(new Point(xRoot1, yRoot1));
            }

            double xRoot2 = (-quadraticB - discriminantSqRoot) / (2.0 * quadraticA);
            double yRoot2 = m1 * (xRoot2 - x1) + y1;

            xRoot2 += circleCenter.x;
            yRoot2 += circleCenter.y;

            if (xRoot2 > minX && xRoot2 < maxX) {
                allPoints.add(new Point(xRoot2, yRoot2));
            }
        }

        return allPoints;

    }


}
