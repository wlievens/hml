package heraldry.util;

import heraldry.render.Box;
import heraldry.render.RenderContour;
import heraldry.render.RenderShape;
import heraldry.render.path.CubicPathStep;
import heraldry.render.path.LinePathStep;
import heraldry.render.path.Path;
import heraldry.render.path.PathStep;
import heraldry.render.path.QuadraticPathStep;

import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class GeometryUtils
{
    public static List<Path> clip(Path path, RenderContour contour)
    {
        if (false)
        {
            return Arrays.asList(path);
        }
        Area area = convertPathToArea(path);
        area.intersect(convertContourToArea(contour));
        return convertPathIteratorToPaths(area.getPathIterator(null)).stream().collect(toList());
    }

    public static List<RenderContour> convertAreaToContours(Area area)
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
        return convertPathToArea(contour.getPath());
    }

    public static List<Path> convertPathIteratorToPaths(PathIterator it)
    {
        float[] xys = new float[6];
        List<Path> list = new ArrayList<>();
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
                    if (previousX != xys[0] || previousY != xys[1])
                    {
                        steps.add(new LinePathStep(previousX, previousY, xys[0], xys[1]));
                        previousX = xys[0];
                        previousY = xys[1];
                    }
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
                    float x2 = xys[0];
                    float y2 = xys[1];
                    float x3 = xys[2];
                    float y3 = xys[3];
                    float x4 = xys[4];
                    float y4 = xys[5];
                    if (!(previousX == x2 && previousX == x3 && previousX == x4 && previousY == y2 && previousY == y3 && previousY == y4))
                    {
                        steps.add(new CubicPathStep(previousX, previousY, x2, y2, x3, y3, x4, y4));
                        previousX = x4;
                        previousY = y4;
                    }
                    break;
                }
                case PathIterator.SEG_CLOSE:
                {
                    if (previousX != firstX || previousY != firstY)
                    {
                        steps.add(new LinePathStep(previousX, previousY, firstX, firstY));
                    }
                    list.add(new Path(steps));
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

    public static Area convertPathToArea(Path path)
    {
        if (path.getSteps().isEmpty())
        {
            return new Area();
        }
        List<PathStep> steps = path.getSteps();
        Path2D path2D = new Path2D.Double();
        for (int index = 0; index < steps.size(); index++)
        {
            PathStep step = steps.get(index);
            if (step instanceof LinePathStep)
            {
                LinePathStep line = (LinePathStep)step;
                if (index == 0)
                {
                    path2D.moveTo(line.getX1(), line.getY1());
                }
                path2D.lineTo(line.getX2(), line.getY2());
            }
            else if (step instanceof QuadraticPathStep)
            {
                QuadraticPathStep quadratic = (QuadraticPathStep)step;
                if (index == 0)
                {
                    path2D.moveTo(quadratic.getX1(), quadratic.getY1());
                }
                path2D.quadTo(quadratic.getX2(), quadratic.getY2(), quadratic.getX3(), quadratic.getY3());
            }
            else if (step instanceof CubicPathStep)
            {
                CubicPathStep cubic = (CubicPathStep)step;
                if (index == 0)
                {
                    path2D.moveTo(cubic.getX1(), cubic.getY1());
                }
                path2D.curveTo(cubic.getX2(), cubic.getY2(), cubic.getX3(), cubic.getY3(), cubic.getX4(), cubic.getY4());
            }
            else
            {
                throw new IllegalStateException();
            }
        }
        path2D.closePath();
        return new Area(path2D);
    }

    public static Area convertShapeToArea(RenderShape shape)
    {
        return convertPathToArea(shape.getPath());
    }

    public static Path polygon(double... xys)
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
        return new Path(steps);
    }

    public static Path rectangle(double x1, double y1, double x2, double y2)
    {
        return polygon(x1, y1, x2, y1, x2, y2, x1, y2);
    }

    public static List<RenderContour> subtract(RenderContour first, RenderContour second)
    {
        Area area1 = convertContourToArea(first);
        Area area2 = convertContourToArea(second);
        area1.subtract(area2);
        return convertAreaToContours(area1);
    }
}
