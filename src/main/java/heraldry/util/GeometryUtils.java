package heraldry.util;

import heraldry.render.Box;
import heraldry.render.CubicPathStep;
import heraldry.render.LinePathStep;
import heraldry.render.PathStep;
import heraldry.render.Point;
import heraldry.render.QuadraticPathStep;
import heraldry.render.RenderContour;
import heraldry.render.RenderShape;

import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class GeometryUtils
{
    public static List<RenderShape> clip(RenderShape shape, RenderContour contour)
    {
        Area area = convertShapeToArea(shape);
        area.intersect(convertContourToArea(contour));
        return convertPathIteratorToPaths(area.getPathIterator(null)).stream()
                .map(path -> new RenderShape(path, shape.getFillPaint(), shape.getBorderColor()))
                .collect(toList());
    }

    public static List<RenderContour> convertAreaToContour(Area area)
    {
        return convertPathIteratorToPaths(area.getPathIterator(null)).stream()
                .map(RenderContour::new)
                .collect(toList());
    }

    public static Area convertBoxToArea(Box box)
    {
        return new Area(new Rectangle2D.Double(box.getX1(), box.getY1(), box.getX2(), box.getY2()));
    }

    public static Area convertContourToArea(RenderContour contour)
    {
        return convertPathStepsToArea(contour.getSteps());
    }

    public static List<List<PathStep>> convertPathIteratorToPaths(PathIterator it)
    {
        float[] xys = new float[6];
        List<List<PathStep>> list = new ArrayList<>();
        List<PathStep> steps = new ArrayList<>();
        double previousX = 0;
        double previousY = 0;
        boolean first = true;
        double firstX = 0;
        double firstY = 0;
        boolean closed = false;
        while (!it.isDone())
        {
            int type = it.currentSegment(xys);
            switch (type)
            {
                case PathIterator.SEG_MOVETO:
                {
                    if (steps.size() > 0)
                    {
                        throw new IllegalStateException("MoveTo after first step not supported");
                    }
                    previousX = xys[0];
                    previousY = xys[1];
                    break;
                }
                case PathIterator.SEG_LINETO:
                {
                    steps.add(new LinePathStep(previousX, previousY, xys[0], xys[1]));
                    previousX = xys[0];
                    previousY = xys[1];
                    break;
                }
                case PathIterator.SEG_QUADTO:
                {
                    steps.add(new QuadraticPathStep(previousX, previousY, xys[0], xys[1], xys[2], xys[3]));
                    previousX = xys[2];
                    previousY = xys[3];
                    break;
                }
                case PathIterator.SEG_CUBICTO:
                {
                    steps.add(new CubicPathStep(previousX, previousY, xys[0], xys[1], xys[2], xys[3], xys[4], xys[5]));
                    previousX = xys[4];
                    previousY = xys[5];
                    break;
                }
                case PathIterator.SEG_CLOSE:
                {
                    steps.add(new LinePathStep(previousX, previousY, firstX, firstY));
                    list.add(steps);
                    steps = new ArrayList<>();
                    break;
                }
                default:
                {
                    throw new IllegalStateException();
                }
            }
            if (first)
            {
                firstX = previousX;
                firstY = previousY;
                first = false;
            }
            it.next();
        }
        return list;
    }

    public static Area convertPathStepsToArea(List<PathStep> steps)
    {
        if (steps.isEmpty())
        {
            throw new IllegalArgumentException();
        }
        Path2D path = new Path2D.Double();
        for (int index = 0; index < steps.size(); index++)
        {
            PathStep step = steps.get(index);
            if (step instanceof LinePathStep)
            {
                LinePathStep line = (LinePathStep)step;
                if (index == 0)
                {
                    path.moveTo(line.getX1(), line.getY1());
                }
                path.lineTo(line.getX2(), line.getY2());
            }
            else if (step instanceof CubicPathStep)
            {
                CubicPathStep cubic = (CubicPathStep)step;
                if (index == 0)
                {
                    path.moveTo(cubic.getX1(), cubic.getY1());
                }
                path.curveTo(cubic.getX2(), cubic.getY2(), cubic.getX3(), cubic.getY3(), cubic.getX4(), cubic.getY4());
            }
            else
            {
                throw new IllegalStateException();
            }
        }
        path.closePath();
        return new Area(path);
    }

    public static Area convertShapeToArea(RenderShape shape)
    {
        return convertPathStepsToArea(shape.getSteps());
    }

    public static Point intersection(
            double x1, double y1, double x2, double y2,
            double x3, double y3, double x4, double y4)
    {
        double d = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
        if (d == 0)
        {
            return null;
        }
        double xi = ((x3 - x4) * (x1 * y2 - y1 * x2) - (x1 - x2) * (x3 * y4 - y3 * x4)) / d;
        double yi = ((y3 - y4) * (x1 * y2 - y1 * x2) - (y1 - y2) * (x3 * y4 - y3 * x4)) / d;
        if (xi < Math.min(x1, x2) || xi > Math.max(x1, x2) || xi < Math.min(x3, x4) || xi > Math.max(x3, x4))
        {
            return null;
        }
        return new Point(xi, yi);
    }

    public static List<PathStep> polygon(double... xys)
    {
        List<PathStep> steps = new ArrayList<>();
        double previousX = xys[0];
        double previousY = xys[1];
        for (int n = 1; n < xys.length / 2; ++n)
        {
            double x = xys[n * 2 + 0];
            double y = xys[n * 2 + 1];
            steps.add(new LinePathStep(previousX, previousY, x, y));
            previousX = x;
            previousY = y;
        }
        steps.add(new LinePathStep(previousX, previousY, xys[0], xys[1]));
        return steps;
    }

    public static List<PathStep> rectangle(double x1, double y1, double x2, double y2)
    {
        return polygon(x1, y1, x2, y1, x2, y2, x1, y2);
    }
}
