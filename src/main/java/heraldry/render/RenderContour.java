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
        Rectangle2D bounds = GeometryUtils.convertContourToArea(this).getBounds2D();
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

    public boolean isRectangle()
    {
        if (steps.size() != 4)
        {
            return false;
        }
        if (steps.stream().filter(step -> step instanceof LinePathStep).count() != 4)
        {
            return false;
        }
        // TODO there may be a cleaner way ...
        Box bounds = getBounds();
        long countX11 = steps.stream().filter(step -> ((LinePathStep)step).getX1() == bounds.getX1()).count();
        long countX21 = steps.stream().filter(step -> ((LinePathStep)step).getX2() == bounds.getX1()).count();
        long countX12 = steps.stream().filter(step -> ((LinePathStep)step).getX1() == bounds.getX2()).count();
        long countX22 = steps.stream().filter(step -> ((LinePathStep)step).getX2() == bounds.getX2()).count();
        long countY11 = steps.stream().filter(step -> ((LinePathStep)step).getY1() == bounds.getY1()).count();
        long countY21 = steps.stream().filter(step -> ((LinePathStep)step).getY2() == bounds.getY1()).count();
        long countY12 = steps.stream().filter(step -> ((LinePathStep)step).getY1() == bounds.getY2()).count();
        long countY22 = steps.stream().filter(step -> ((LinePathStep)step).getY2() == bounds.getY2()).count();
        return (countX11 + countX21) == 4 && (countX12 + countX22) == 4 && (countY11 + countY21) == 4 && (countY12 + countY22) == 4;
    }
}
