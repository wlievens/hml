package heraldry.render.path;

import heraldry.render.Point;
import heraldry.util.MathUtils;

public abstract class AbstractBezierPathStep implements PathStep
{
    @Override
    public double getLength()
    {
        if (length != null)
        {
            return length;
        }
        int samples = 100;
        double length = 0;
        double prevX = getStartX();
        double prevY = getStartY();
        for (int n = 1; n < samples; ++n)
        {
            double t = n / (double)samples;
            Point point = sample(t);
            double x = point.getX();
            double y = point.getY();
            length += MathUtils.distance(prevX, prevY, x, y);
            prevX = x;
            prevY = y;
        }
        this.length = length;
        return length;
    }

    private transient Double length;
}
