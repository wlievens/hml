package heraldry.render;

import lombok.Value;

@Value
public final class QuadraticPathStep implements PathStep
{
    private final double x1;
    private final double y1;
    private final double x2;
    private final double y2;
    private final double x3;
    private final double y3;

    @Override
    public double getMinX()
    {
        return Math.min(x1, x3);
    }

    @Override
    public double getMinY()
    {
        return Math.min(y1, y3);
    }

    @Override
    public double getMaxX()
    {
        return Math.max(x1, x3);
    }

    @Override
    public double getMaxY()
    {
        return Math.max(y1, y3);
    }

    @Override
    public QuadraticPathStep offset(double x, double y)
    {
        return new QuadraticPathStep(x1 + x, y1 + y, x2 + x, y2 + y, x3 + x, y3 + y);
    }
}
