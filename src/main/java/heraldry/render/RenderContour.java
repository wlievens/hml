package heraldry.render;

import heraldry.util.GeometryUtils;
import lombok.Getter;
import lombok.ToString;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Getter
@ToString
public final class RenderContour
{
    private final List<PathStep> steps;

    public RenderContour(List<PathStep> steps)
    {
        if (steps.isEmpty())
        {
            throw new IllegalArgumentException();
        }
        this.steps = steps;
    }

    public Point getCenter()
    {
        return getBounds().getFessPoint();
    }

    public Box getBounds()
    {
        Rectangle2D bounds = GeometryUtils.convertContourToArea(this).getBounds();
        return new Box(bounds.getX(), bounds.getY(), bounds.getMaxX(), bounds.getMaxY());
    }

    public List<RenderShape> clipAll(Collection<RenderShape> shapes)
    {
        List<RenderShape> list = new ArrayList<>();
        for (RenderShape shape : shapes)
        {
            list.addAll(clip(shape));
        }
        return list;
    }

    public List<RenderShape> clip(RenderShape shape)
    {
        if (false)
        {
            return Collections.singletonList(shape);
        }
        return GeometryUtils.clip(shape, this);
    }
}
