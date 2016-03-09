package heraldry.render.ordinary;

import heraldry.model.Line;
import heraldry.model.Tincture;
import heraldry.render.Box;
import heraldry.render.CubicPathStep;
import heraldry.render.LinePathStep;
import heraldry.render.Painter;
import heraldry.render.PathStep;
import heraldry.render.Point;
import heraldry.render.QuadraticPathStep;
import heraldry.render.RenderShape;
import heraldry.util.MathUtils;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class BendOrdinaryRenderer implements OrdinaryRenderer
{
    private final boolean flip;
    private final double sizeRatio;

    @Override
    public Collection<RenderShape> render(Box bounds, Tincture tincture, Line line, Painter painter)
    {
        double x1 = bounds.getX1();
        double y1 = bounds.getY1();
        double x2 = bounds.getX2();
        double y2 = bounds.getY2();
        double width = bounds.getWidth();
        double height = bounds.getHeight();
        double step = sizeRatio * painter.getOrdinaryThickness() / Math.sqrt(2) * line.getScaleFactor();
        double startX = flip ? x2 : x1;
        double startY = y1;
        double size = Math.max(width, height);
        double endX = flip ? (x2 - size) : (x1 + size);
        double endY = y1 + size;
        double period = painter.getLinePeriodFactor() * Math.min(width, height);
        List<PathStep> steps = new ArrayList<>();
        Point end1 = plotLine(steps, startX, startY - step, endX, endY - step, line, period, flip);
        steps.add(new LinePathStep(end1.getX(), end1.getY(), endX, endY + step));
        Point end2 = plotLine(steps, endX, endY + step, startX, startY + step, line, period, flip);
        steps.add(new LinePathStep(end2.getX(), end2.getY(), startX, startY - step));
        return Arrays.asList(new RenderShape(steps, painter.getColor(tincture), null));
    }

    private static Point plotLine(List<PathStep> steps, double startX, double startY, double endX, double endY, Line line, double period, boolean flipped)
    {
        switch (line)
        {
            case PLAIN:
            {
                steps.add(new LinePathStep(startX, startY, endX, endY));
                return new Point(endX, endY);
            }
            case WAVY:
            case INDENTED:
            case INVECTED:
            case ENGRAILED:
            case NEBULY:
            {
                double angle = Math.atan2(endY - startY, endX - startX);
                double length = MathUtils.distance(startX, startY, endX, endY);
                double offset = 0;
                double amplitude = period / 2 * (flipped ? -1 : +1);
                boolean alternate = false;
                while (offset < length)
                {
                    double x1 = startX + Math.cos(angle) * offset;
                    double y1 = startY + Math.sin(angle) * offset;
                    double x2 = startX + Math.cos(angle) * (offset + period);
                    double y2 = startY + Math.sin(angle) * (offset + period);
                    if (line == Line.INVECTED)
                    {
                        alternate = false;
                    }
                    if (line == Line.ENGRAILED)
                    {
                        alternate = true;
                    }
                    double angleOffset = alternate ? (+Math.PI / 2) : (-Math.PI / 2);
                    double cx1 = (x1 + x2) / 2 + Math.cos(angle + angleOffset) * amplitude;
                    double cy1 = (y1 + y2) / 2 + Math.sin(angle + angleOffset) * amplitude;
                    if (line == Line.WAVY || line == Line.INVECTED || line == Line.ENGRAILED)
                    {
                        steps.add(new QuadraticPathStep(x1, y1, cx1, cy1, x2, y2));
                    }
                    else if (line == Line.NEBULY)
                    {
                        double ox = Math.cos(angle + Math.PI / 2) * amplitude;
                        double oy = Math.sin(angle + Math.PI / 2) * amplitude;
                        if (alternate)
                        {
                            double f0 = 1.30;
                            double f1 = 0.70;
                            double f2 = 0.70;
                            double f3 = 1.20;
                            double mx = (x1 + x2) / 2 - ox * f0;
                            double my = (y1 + y2) / 2 - oy * f0;
                            double mx1 = mx - Math.cos(angle) * period * f1;
                            double my1 = my - Math.sin(angle) * period * f1;
                            double mx2 = mx + Math.cos(angle) * period * f1;
                            double my2 = my + Math.sin(angle) * period * f1;
                            steps.add(new CubicPathStep(x1, y1, x1 - ox * f2, y1 - oy * f2, mx1 + ox * f2, my1 + oy * f2, mx1, my1));
                            steps.add(new CubicPathStep(mx1, my1, mx1 - ox * f3, my1 - oy * f3, mx2 - ox * f3, my2 - oy * f3, mx2, my2));
                            steps.add(new CubicPathStep(mx2, my2, mx2 + ox * f2, my2 + oy * f2, x2 - ox * f2, y2 - oy * f2, x2, y2));
                        }
                        else
                        {
                            steps.add(new CubicPathStep(x1, y1, x1 + ox, y1 + oy, x2 + ox, y2 + oy, x2, y2));
                        }
                    }
                    else if (line == Line.INDENTED)
                    {
                        steps.add(new LinePathStep(x1, y1, cx1, cy1));
                        steps.add(new LinePathStep(cx1, cy1, x2, y2));
                    }
                    else
                    {
                        throw new IllegalStateException();
                    }
                    offset += period;
                    alternate = !alternate;
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
