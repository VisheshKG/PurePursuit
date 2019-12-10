package purepursuit.algorithm;

import java.util.ArrayList;

import static purepursuit.algorithm.RobotMovement.*;

public class MyOpMode extends OpMode{

    @Override
    public void init() {

    }

    @Override
    public void loop() {
        ArrayList<CurvePoint> allPoints = new ArrayList<>();
        allPoints.add(new CurvePoint(0, 0, 1.0, 1.0, 45, 1, Math.toRadians(50), 1.0));
        allPoints.add(new CurvePoint(180, 180, 1.0, 0.3, 45, 1, Math.toRadians(50), 1.0));
        allPoints.add(new CurvePoint(220, 180, 1.0, 0.3, 50, 1, Math.toRadians(50), 1.0));
        allPoints.add(new CurvePoint(280, 50, 1.0, 0.3, 25, 1, Math.toRadians(50), 1.0));
        allPoints.add(new CurvePoint(180, 0, 1.0, 0.3, 25, 1, Math.toRadians(50), 1.0));

        followCurve(allPoints, Math.toRadians(90));
    }
}
