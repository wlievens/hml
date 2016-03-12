package heraldry.render;

import heraldry.model.Line;
import heraldry.util.MathUtils;

import java.util.List;

public class LineRenderer
{
    public static void line(List<PathStep> steps, double startX, double startY, double endX, double endY, Line line, double period, boolean flipped, double amplitudeFactor)
    {
        double distance = MathUtils.distance(startX, startY, endX, endY);
        period = distance / Math.round(distance / period);
        switch (line)
        {
            case PLAIN:
            {
                steps.add(new LinePathStep(startX, startY, endX, endY));
                break;
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
                double amplitude = (period / 2 * (flipped ? -1 : +1)) * amplitudeFactor;
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
                        //steps.add(new LinePathStep(x1, y1, cx1, cy1));
                        //steps.add(new LinePathStep(cx1, cy1, x2, y2));
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
                break;
            }
            default:
            {
                throw new IllegalArgumentException(String.format("Line '%s' not yet implemented", line));
            }
        }
    }
}
