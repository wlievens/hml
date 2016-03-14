package heraldry.render;

import lombok.Value;

@Value
public final class CubicPathStep implements PathStep
{
    private final double x1;
    private final double y1;
    private final double x2;
    private final double y2;
    private final double x3;
    private final double y3;
    private final double x4;
    private final double y4;

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
        return x4;
    }

    @Override
    public double getEndY()
    {
        return y4;
    }

    @Override
    public double getMinX()
    {
        return Math.min(x1, x4);
    }

    @Override
    public double getMinY()
    {
        return Math.min(y1, y4);
    }

    @Override
    public double getMaxX()
    {
        return Math.max(x1, x4);
    }

    @Override
    public double getMaxY()
    {
        return Math.max(y1, y4);
    }

    @Override
    public CubicPathStep offset(double x, double y)
    {
        return new CubicPathStep(x1 + x, y1 + y, x2 + x, y2 + y, x3 + x, y3 + y, x4 + x, y4 + y);
    }

    @Override
    public CubicPathStep inverse()
    {
        return new CubicPathStep(x4, y4, x3, y3, x2, y2, x1, y1);
    }
}
