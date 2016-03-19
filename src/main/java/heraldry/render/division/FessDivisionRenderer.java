package heraldry.render.division;

import heraldry.model.Line;
import heraldry.render.Box;
import heraldry.render.LinePathStep;
import heraldry.render.LineRenderer;
import heraldry.render.Painter;
import heraldry.render.PathStep;
import heraldry.render.Point;
import heraldry.render.RenderContour;
import heraldry.util.GeometryUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FessDivisionRenderer implements DivisionRenderer
{
    @Override
    public List<RenderContour> render(RenderContour contour, Line line, Painter painter)
    {
        Box bounds = contour.getBounds();
        Point center = bounds.getFessPoint();
        double x1 = bounds.getX1();
        double x2 = bounds.getX2();
        double y1 = bounds.getY1();
        double cy = center.getY();
        double period = painter.getLinePeriodFactor() * Math.min(bounds.getWidth(), bounds.getHeight());

        List<PathStep> steps = new ArrayList<>();
        steps.add(new LinePathStep(x1, y1, x2, y1));
        steps.add(new LinePathStep(x2, y1, x2, cy));
        LineRenderer.line(steps, x2, cy, x1, cy, line, period, false, 1.0);
        steps.add(new LinePathStep(x1, cy, x1, y1));

        RenderContour top = contour.clip(new RenderContour(steps)).get(0);
        RenderContour bottom = GeometryUtils.subtract(contour, top).get(0);

        return Arrays.asList(top, bottom);
    }
}
