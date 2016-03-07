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
}
