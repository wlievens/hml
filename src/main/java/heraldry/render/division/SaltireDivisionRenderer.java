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

public class SaltireDivisionRenderer implements DivisionRenderer
{
    @Override
    public List<RenderContour> render(RenderContour contour, Line line, Painter painter)
    {
        Box bounds = contour.getBounds();
        Point center = bounds.getFessPoint();
        double x1 = bounds.getX1();
        double x2 = bounds.getX2();
        double y1 = bounds.getY1();
        double y2 = bounds.getY2();
        double cx = center.getX();
        double cy = center.getY();
        double size = cx - x1;
        double period = painter.getLinePeriodFactor() * Math.min(bounds.getWidth(), bounds.getHeight());

        RenderContour remainder = contour;
        List<PathStep> steps;

        steps = new ArrayList<>();
        steps.add(new LinePathStep(x1, y1, x2, y1));
        LineRenderer.line(steps, x2, y1, cx, cy, line, period, false, 1.0);
        LineRenderer.line(steps, cx, cy, x1, y1, line, period, false, 1.0);
        RenderContour top = remainder.clip(new RenderContour(steps)).get(0);
        remainder = GeometryUtils.subtract(remainder, top).get(0);

        steps = new ArrayList<>();
        steps.add(new LinePathStep(x2, y1, x2, cy + size));
        LineRenderer.line(steps, x2, cy + size, cx, cy, line, period, false, 1.0);
        LineRenderer.line(steps, cx, cy, x2, y1, line, period, false, 1.0);
        RenderContour right = remainder.clip(new RenderContour(steps)).get(0);
        remainder = GeometryUtils.subtract(remainder, right).get(0);

        steps = new ArrayList<>();
        LineRenderer.line(steps, cx, cy, x2, cy + size, line, period, false, 1.0);
        steps.add(new LinePathStep(x2, cy + size, x2, cy + size));
        if (cy + size < y2)
        {
            steps.add(new LinePathStep(x2, cy + size, x2, y2));
            steps.add(new LinePathStep(x2, y2, x1, y2));
            steps.add(new LinePathStep(x1, y2, x1, cy + size));
        }
        LineRenderer.line(steps, x1, cy + size, cx, cy, line, period, false, 1.0);
        RenderContour bottom = remainder.clip(new RenderContour(steps)).get(0);
        remainder = GeometryUtils.subtract(remainder, bottom).get(0);

        RenderContour left = remainder;

        return Arrays.asList(top, right, bottom, left);
    }
}
