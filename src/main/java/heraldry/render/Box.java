package heraldry.render;

import heraldry.render.path.Path;
import heraldry.util.GeometryUtils;
import heraldry.util.MathUtils;
import lombok.ToString;
import lombok.Value;

@Value
@ToString
public final class Box
{
    private final double x1;
    private final double y1;
    private final double x2;
    private final double y2;

    public static Box around(double centerX, double centerY, double width, double height)
    {
        return new Box(centerX - width / 2, centerY - height / 2, centerX + width / 2, centerY + height / 2);
    }

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

    public boolean contains(double x1, double y1, double x2, double y2)
    {
        return contains(x1, y1) && contains(x2, y2);
    }

    public Path toPath()
    {
        return GeometryUtils.rectangle(x1, y1, x2, y2);
    }

    public double prelX(double x)
    {
        return (x - x1) / (x2 - x1);
    }

    public double prelY(double y)
    {
        return (y - y1) / (y2 - y1);
    }

    public boolean intersects(Box box)
    {
        return !(this.x2 < box.x1 || this.x1 > box.x2 || this.y2 < box.y1 || this.y1 > box.y2);
    }

    public double getCenterX()
    {
        return (x1 + x2) / 2;
    }

    public double getCenterY()
    {
        return (y1 + y2) / 2;
    }
}
