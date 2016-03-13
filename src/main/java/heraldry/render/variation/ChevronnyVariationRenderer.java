package heraldry.render.variation;

import heraldry.model.Line;
import heraldry.model.Tincture;
import heraldry.render.Box;
import heraldry.render.LinePathStep;
import heraldry.render.LineRenderer;
import heraldry.render.Painter;
import heraldry.render.PathStep;
import heraldry.render.RenderContour;
import heraldry.render.RenderShape;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class ChevronnyVariationRenderer implements VariationRenderer
{
    private final boolean flip;

    @Override
    public Collection<RenderShape> render(RenderContour contour, Tincture firstTincture, Tincture secondTincture, Line line, int number, Painter painter)
    {
        Box bounds = contour.getBounds();
        double x1 = bounds.getX1();
        double x2 = bounds.getX2();
        double y1 = bounds.getY1();
        double y2 = bounds.getY2();
        double width = bounds.getWidth();
        double height = bounds.getHeight();
        double spacing = 2 * painter.getOrdinaryThickness() / Math.sqrt(2) * line.getScaleFactor();
        double step = spacing / 4;
        double period = painter.getLinePeriodFactor() * Math.min(width, height);
        double midX = (x1 + x2) / 2;

        List<RenderShape> shapes = new ArrayList<>();
        shapes.add(new RenderShape(contour.getSteps(), painter.getColor(firstTincture), null));

        for (double y = y1 - width / 2; y <= y2 + width / 2; y += spacing)
        {
            List<PathStep> steps = new ArrayList<>();
            if (flip)
            {
                double midY = y + width / 2;
                LineRenderer.line(steps, x1, y - step, midX, midY - step, line, period, flip, 1.0);
                LineRenderer.line(steps, midX, midY - step, x2, y - step, line, period, flip, 1.0);
                steps.add(new LinePathStep(x2, y - step, x2, y + step));
                LineRenderer.line(steps, x2, y + step, midX, midY + step, line, period, flip, 1.0);
                LineRenderer.line(steps, midX, midY + step, x1, y + step, line, period, flip, 1.0);
                steps.add(new LinePathStep(x1, y + step, x1, y - step));
            }
            else
            {
                double midY = y - width / 2;
                steps.add(new LinePathStep(x1, y, x1, y - step));
                LineRenderer.line(steps, x1, y - step, midX, midY - step, line, period, flip, 1.0);
                LineRenderer.line(steps, midX, midY - step, x2, y - step, line, period, flip, 1.0);
                steps.add(new LinePathStep(x2, y - step, x2, y + step));
                LineRenderer.line(steps, x2, y + step, midX, midY + step, line, period, flip, 1.0);
                LineRenderer.line(steps, midX, midY + step, x1, y + step, line, period, flip, 1.0);
                steps.add(new LinePathStep(x1, y + step, x1, y));
            }
            shapes.addAll(contour.clip(new RenderShape(steps, painter.getColor(secondTincture), null)));
        }

        return shapes;
    }
}
