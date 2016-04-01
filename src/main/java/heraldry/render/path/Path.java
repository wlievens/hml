package heraldry.render.path;

import heraldry.render.Box;
import heraldry.render.Point;
import heraldry.render.RenderShape;
import heraldry.render.paint.Color;
import heraldry.render.paint.Paint;
import heraldry.util.GeometryUtils;
import lombok.Getter;

import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Getter
public final class Path
{
    private final List<PathStep> steps;

    private final boolean closed;

    public Path(PathStep... steps)
    {
        this(Arrays.asList(steps));
    }

    public Path(List<PathStep> steps)
    {
        this(steps, true);
    }

    public Path(List<PathStep> steps, boolean closed)
    {
        if (steps.isEmpty())
        {
            throw new IllegalArgumentException();
        }
        this.steps = steps;
        this.closed = closed;
    }

    public RenderShape render(Paint fillPaint, Color border, String label)
    {
        return RenderShape.create(this, fillPaint, border, label);
    }

    @Override
    public String toString()
    {
        return "Path" + steps.toString();
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

    public double getLength()
    {
        return steps.stream().mapToDouble(PathStep::getLength).sum();
    }

    public Path inverse()
    {
        List<PathStep> steps = this.steps.stream().map(PathStep::inverse).collect(toList());
        Collections.reverse(steps);
        return new Path(steps);
    }

    public Point sample(double position)
    {
        if (position < 0)
        {
            throw new IllegalArgumentException();
        }
        if (position > 1)
        {
            throw new IllegalArgumentException();
        }
        double length = getLength();
        double pos = position * length;
        double accumulator = 0;
        for (PathStep step : steps)
        {
            double stepLength = step.getLength();
            if (pos <= accumulator + stepLength)
            {
                double ratio = (pos - accumulator) / stepLength;
                return step.sample(ratio);
            }
            accumulator += stepLength;
        }
        throw new IllegalArgumentException();
    }
}
