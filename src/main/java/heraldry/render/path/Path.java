package heraldry.render.path;

import heraldry.render.Box;
import heraldry.util.GeometryUtils;
import lombok.Getter;

import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.List;

@Getter
public final class Path
{

    private final List<PathStep> steps;

    public Path(PathStep... steps)
    {
        this(Arrays.asList(steps));
    }

    public Path(List<PathStep> steps)
    {
        if (steps.isEmpty())
        {
            throw new IllegalArgumentException();
        }
        this.steps = steps;
    }

    public Box getBounds()
    {
        Rectangle2D bounds = GeometryUtils.convertPathToArea(this).getBounds2D();
        return new Box(bounds.getX(), bounds.getY(), bounds.getMaxX(), bounds.getMaxY());
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

    public int getStepCount()
    {
        return steps.size();
    }

    public PathStep getStep(int index)
    {
        return steps.get(index);
    }

    @Override
    public String toString()
    {
        return "Path" + steps.toString();
    }
}
