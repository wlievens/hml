package heraldry.render.variation;

import heraldry.model.Line;
import heraldry.model.Tincture;
import heraldry.render.Box;
import heraldry.render.path.LinePathStep;
import heraldry.render.LineRenderer;
import heraldry.render.Painter;
import heraldry.render.path.PathStep;
import heraldry.render.RenderContour;
import heraldry.render.RenderShape;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PalyVariationRenderer implements VariationRenderer
{
    @Override
    public Collection<RenderShape> render(RenderContour contour, Tincture firstTincture, Tincture secondTincture, Line line, int number, Painter painter)
    {
        Box bounds = contour.getBounds();
        List<RenderShape> list = new ArrayList<>();
        list.add(new RenderShape(contour.getSteps(), painter.getPaint(firstTincture), null));
        double step = number == 0 ? painter.getGridPatternSize() : bounds.getHeight() / number;
        double period = painter.getLinePeriodFactor() * Math.min(bounds.getWidth(), bounds.getHeight());
        double y1 = bounds.getY1();
        double y2 = bounds.getY2();
        for (double x = bounds.getX1() + step; x < bounds.getX2(); x += step * 2)
        {
            List<PathStep> steps = new ArrayList<>();
            steps.add(new LinePathStep(x, y1, x + step, y1));
            LineRenderer.line(steps, x + step, y1, x + step, y2, line, period, false, 1.0);
            steps.add(new LinePathStep(x + step, y2, x, y2));
            LineRenderer.line(steps, x, y2, x, y1, line, period, false, 1.0);
            list.addAll(contour.clip(new RenderShape(steps, painter.getPaint(secondTincture), null)));
        }
        return list;
    }
}
