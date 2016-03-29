package heraldry.util;

import heraldry.render.Point;

public class BezierCurve
{
    
    private static final double b1(double t)
    {
        return t * t * t;
    }
    
    private static final double b2(double t)
    {
        return 3 * t * t * (1 - t);
    }
    
    private static final double b3(double t)
    {
        return 3 * t * (1 - t) * (1 - t);
    }
    
    private static final double b4(double t)
    {
        return (1 - t) * (1 - t) * (1 - t);
    }
    
    public static final double sampleAngle(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4, double t)
    {
        double epsilon = 0.01;
        // TODO math a better way out of this?
        Point p1 = sampleXY(x1, y1, x2, y2, x3, y3, x4, y4, t - epsilon);
        Point p2 = sampleXY(x1, y1, x2, y2, x3, y3, x4, y4, t + epsilon);
        return Math.atan2(p2.getY() - p1.getY(), p2.getX() - p1.getX());
    }
    
    public static final Point sampleXY(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4, double t)
    {
        double px = x1 * b1(t) + x2 * b2(t) + x3 * b3(t) + x4 * b4(t);
        double py = y1 * b1(t) + y2 * b2(t) + y3 * b3(t) + y4 * b4(t);
        return new Point(px, py);
    }
    
}
