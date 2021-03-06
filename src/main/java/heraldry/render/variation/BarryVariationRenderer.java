package heraldry.render.variation;

import heraldry.model.Line;
import heraldry.model.Tincture;
import heraldry.render.Box;
import heraldry.render.LineRenderer;
import heraldry.render.Painter;
import heraldry.render.RenderContour;
import heraldry.render.RenderShape;
import heraldry.render.path.LinePathStep;
import heraldry.render.path.Path;
import heraldry.render.path.PathStep;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BarryVariationRenderer implements VariationRenderer
{
    @Override
    public Collection<RenderShape> render(RenderContour contour, Tincture firstTincture, Tincture secondTincture, Line line, int number, Painter painter)
    {
        Box bounds = contour.getBounds();
        List<RenderShape> list = new ArrayList<>();
        list.add(contour.render(painter.getPaint(firstTincture), null, getClass().getSimpleName() + " background"));
        double step = number == 0 ? painter.getGridPatternSize() : bounds.getHeight() / number;
        double period = painter.getLinePeriodFactor() * Math.min(bounds.getWidth(), bounds.getHeight());
        for (double y = bounds.getY1() + step; y < bounds.getY2(); y += step * 2)
        {
            List<PathStep> steps = new ArrayList<>();
            LineRenderer.line(steps, bounds.getX1(), y, bounds.getX2(), y, line, period, false, 1.0);
            steps.add(new LinePathStep(bounds.getX2(), y, bounds.getX2(), y + step));
            LineRenderer.line(steps, bounds.getX2(), y + step, bounds.getX1(), y + step, line, period, true, 1.0);
            steps.add(new LinePathStep(bounds.getX1(), y + step, bounds.getX1(), y));
            list.add(contour.clip(new Path(steps).render(painter.getPaint(secondTincture), null, "barry " + y)));
        }
        return list;
    }
}
