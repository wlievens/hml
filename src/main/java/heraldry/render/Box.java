package heraldry.render;

import heraldry.render.path.PathStep;
import heraldry.util.GeometryUtils;
import heraldry.util.MathUtils;
import lombok.ToString;
import lombok.Value;

import java.util.List;

@Value
@ToString
public final class Box
{
    private final double x1;
    private final double y1;
    private final double x2;
    private final double y2;

    public double lerpX(double t)
    {
        return MathUtils.lerp(t, x1, x2);
    }

    public double lerpY(double t)
    {
        return MathUtils.lerp(t, y1, y2);
    }

    public double getWidth()
    {
        return x2 - x1;
    }

    public double getHeight()
    {
        return y2 - y1;
    }

    public Point getFessPoint()
    {
        double width = getWidth();
        double height = getHeight();
        if (width <= height)
        {
            return new Point(x1 + width / 2, y1 + width / 2);
        }
        return new Point((x1 + x2) / 2, (y1 + y2) / 2);
    }

    public boolean contains(double x, double y)
    {
        return x >= x1 && y >= y1 && x <= x2 && y <= y2;
    }

    public List<PathStep> toPath()
    {
        return GeometryUtils.rectangle(x1, y1, x2, y2);
    }
}
