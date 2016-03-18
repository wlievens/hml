package heraldry.render.ordinary;

import heraldry.model.Line;
import heraldry.render.Box;
import heraldry.render.LineRenderer;
import heraldry.render.Painter;
import heraldry.render.PathStep;
import heraldry.render.Point;
import heraldry.render.RenderContour;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class BilletOrdinaryRenderer implements OrdinaryRenderer
{
    @Override
    public Collection<RenderContour> render(RenderContour contour, Line line, Painter painter)
    {
        Box bounds = contour.getBounds();
        double margin = painter.getOrdinaryThickness() / 2;
        Point center = bounds.getFessPoint();
        double cx = center.getX();
        double cy = center.getY();
        double x1 = cx - margin;
        double x2 = cx + margin;
        double y1 = cy - margin * 3;
        double y2 = cy + margin * 3;
        double period = painter.getLinePeriodFactor() * Math.min(bounds.getWidth(), bounds.getHeight());

        List<PathStep> steps = new ArrayList<>();
        LineRenderer.line(steps, x1, y1, x2, y1, line, period, false, 1.0);
        LineRenderer.line(steps, x2, y1, x2, y2, line, period, false, 1.0);
        LineRenderer.line(steps, x2, y2, x1, y2, line, period, false, 1.0);
        LineRenderer.line(steps, x1, y2, x1, y1, line, period, false, 1.0);
        return Arrays.asList(new RenderContour(steps));
    }
}
