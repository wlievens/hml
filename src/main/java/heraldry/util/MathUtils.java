package heraldry.util;

public class MathUtils
{
    public static double lerp(double x, double a, double b)
    {
        return a + (b - a) * x;
    }

    public static double distance(double x1, double y1, double x2, double y2)
    {
        double dx = x1 - x2;
        double dy = y1 - y2;
        return Math.sqrt(dx * dx + dy * dy);
    }
}
