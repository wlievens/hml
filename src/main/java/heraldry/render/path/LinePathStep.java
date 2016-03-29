package heraldry.render.path;

import heraldry.render.Point;
import heraldry.util.MathUtils;
import lombok.Value;

import static heraldry.util.MathUtils.lerp;

@Value
public final class LinePathStep implements PathStep
{
    private final double x1;
    private final double y1;
    private final double x2;
    private final double y2;

    @Override
    public double getMinX()
    {
        return Math.min(x1, x2);
    }

    @Override
    public double getMinY()
    {
        return Math.min(y1, y2);
    }

    @Override
    public double getMaxX()
    {
        return Math.max(x1, x2);
    }

    @Override
    public double getMaxY()
    {
        return Math.max(y1, y2);
    }

    @Override
    public double getStartX()
    {
        return x1;
    }

    @Override
    public double getStartY()
    {
        return y1;
    }

    @Override
    public double getEndX()
    {
        return x2;
    }

    @Override
    public double getEndY()
    {
        return y2;
    }

    @Override
    public LinePathStep offset(double x, double y)
    {
        return new LinePathStep(x1 + x, y1 + y, x2 + x, y2 + y);
    }

    @Override
    public LinePathStep inverse()
    {
        return new LinePathStep(x2, y2, x1, y1);
    }

    @Override
    public double getLength()
    {
        return MathUtils.distance(x1, y1, x2, y2);
    }

    @Override
    public Point sample(double ratio)
    {
        return new Point(lerp(ratio, x1, x2), lerp(ratio, y1, y2));
    }
}
