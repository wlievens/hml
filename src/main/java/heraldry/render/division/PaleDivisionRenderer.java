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

public class PaleDivisionRenderer implements DivisionRenderer
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
        double period = painter.getLinePeriodFactor() * Math.min(bounds.getWidth(), bounds.getHeight());

        List<PathStep> steps = new ArrayList<>();
        steps.add(new LinePathStep(x1, y1, cx, y1));
        LineRenderer.line(steps, cx, y1, cx, y2, line, period, false, 1.0);
        steps.add(new LinePathStep(cx, y2, x1, y2));
        steps.add(new LinePathStep(x1, y2, x1, y1));

        RenderContour left = contour.clip(new RenderContour(steps)).get(0);
        RenderContour right = GeometryUtils.subtract(contour, left).get(0);

        return Arrays.asList(left, right);
    }
}
