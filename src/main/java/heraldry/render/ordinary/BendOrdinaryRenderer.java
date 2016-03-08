package heraldry.render.ordinary;

import heraldry.model.Line;
import heraldry.model.Tincture;
import heraldry.render.Box;
import heraldry.render.Color;
import heraldry.render.LinePathStep;
import heraldry.render.Painter;
import heraldry.render.PathStep;
import heraldry.render.Point;
import heraldry.render.QuadraticPathStep;
import heraldry.render.RenderShape;
import heraldry.util.GeometryUtils;
import heraldry.util.MathUtils;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class BendOrdinaryRenderer implements OrdinaryRenderer
{
    private final double sizeRatio;

    @Override
    public Collection<RenderShape> render(Box bounds, Tincture tincture, Line line, Painter painter)
    {
        double x1 = bounds.getX1();
        double y1 = bounds.getY1();
        double width = bounds.getWidth();
        double height = bounds.getHeight();
        double step;
        double right1;
        double right2;
        double bottom1;
        double bottom2;
        if (width > height)
        {
            step = sizeRatio * painter.getOrdinaryThickness() / Math.sqrt(2);
            bottom1 = height;
            bottom2 = height;
            right1 = height + step;
            right2 = height - step;
        }
        else
        {
            step = sizeRatio * painter.getOrdinaryThickness() / Math.sqrt(2);
            bottom1 = width - step;
            bottom2 = width + step;
            right1 = width;
            right2 = width;
        }
        double period = painter.getLinePeriod();
        List<PathStep> steps = new ArrayList<>();
        steps.add(new LinePathStep(x1, y1, x1 + step, y1));
        Point end1 = plotLine(steps, x1 + step, y1, x1 + right1, y1 + bottom1, line, period);
        steps.add(new LinePathStep(end1.getX(), end1.getY(), x1 + right2, y1 + bottom2));
        Point end2 = plotLine(steps, x1 + right2, y1 + bottom2, x1, y1 + step, line, period);
        steps.add(new LinePathStep(end2.getX(), end2.getY(), x1, y1));
        return Arrays.asList(new RenderShape(steps, painter.getColor(tincture), null),
                new RenderShape(GeometryUtils.polygon(x1 + step, y1, x1 + right1, y1 + bottom1), null, new Color(0, 1, 1)),
                new RenderShape(GeometryUtils.polygon(x1 + right2, y1 + bottom2, x1, y1 + step), null, new Color(0, 1, 1)));
    }

    private Point plotLine(List<PathStep> steps, double startX, double startY, double endX, double endY, Line line, double period)
    {
        switch (line)
        {
            case PLAIN:
            {
                steps.add(new LinePathStep(startX, startY, endX, endY));
                return new Point(endX, endY);
            }
            case INDENTED:
            {
                double stepX = period * (endX > startX ? +1 : -1);
                double stepY = period * (endY > startY ? +1 : -1);
                int n = 0;
                while (true)
                {
                    double x1 = startX + (n + 0) * stepX + 0.5 * stepX;
                    double y1 = startY + (n + 0) * stepY;
                    double x2 = startX + (n + 1) * stepX + 0.5 * stepX;
                    double y2 = startY + (n + 1) * stepY;
                    if (n == 0)
                    {
                        steps.add(new LinePathStep(startX, startY, x1, y1));
                    }
                    steps.add(new LinePathStep(x1, y1, x1, y2));
                    if (x1 < endX && y1 < endY)
                    {
                        steps.add(new LinePathStep(x1, y2, x2, y2));
                    }
                    else
                    {
                        return new Point(x1, y1);
                    }
                    ++n;
                }
            }
            case WAVY:
            {
                double angle = Math.atan2(endY - startY, endX - startX);
                double length = MathUtils.distance(startX, startY, endX, endY);
                double offset = 0;
                while (offset < length)
                {
                    double x1 = startX + Math.cos(angle) * offset;
                    double y1 = startY + Math.sin(angle) * offset;
                    double x2 = startX + Math.cos(angle) * (offset + period);
                    double y2 = startY + Math.sin(angle) * (offset + period);
                    double cx1 = (x1 + x2) / 2 + Math.cos(angle + Math.PI / 2) * period / 2;
                    double cy1 = (y1 + y2) / 2 + Math.sin(angle + Math.PI / 2) * period / 2;
                    steps.add(new QuadraticPathStep(x1, y1, cx1, cy1, x2, y2));
                    offset += period;
                }
                return new Point(endX, endY);
            }
            default:
            {
                throw new IllegalArgumentException(String.format("Line '%s' not yet implemented", line));
            }
        }
    }
}
