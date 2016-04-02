package heraldry.render.division;

import heraldry.model.Line;
import heraldry.render.Box;
import heraldry.render.LineRenderer;
import heraldry.render.Painter;
import heraldry.render.Point;
import heraldry.render.RenderContour;
import heraldry.render.path.LinePathStep;
import heraldry.render.path.Path;
import heraldry.render.path.PathStep;
import heraldry.util.GeometryUtils;

import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuarterlyDivisionRenderer implements DivisionRenderer
{
    @Override
    public List<RenderContour> render(RenderContour contour, Line line, Painter painter)
    {
        Box bounds = contour.getBounds();
        Point center = bounds.getFessPoint();
        double cx = center.getX();
        double cy = center.getY();
        double x1 = bounds.getX1();
        double x2 = bounds.getX2();
        double y1 = bounds.getY1();
        double y2 = bounds.getY2();
        double period = painter.getLinePeriodFactor() * Math.min(bounds.getWidth(), bounds.getHeight());

        Area remainder = contour.createArea();
        List<PathStep> steps;

        steps = new ArrayList<>();
        steps.add(new LinePathStep(x1, cy, x1, y1));
        steps.add(new LinePathStep(x1, y1, cx, y1));
        LineRenderer.line(steps, cx, y1, cx, cy, line, period, false, 1.0);
        LineRenderer.line(steps, cx, cy, x1, cy, line, period, false, 1.0);
        RenderContour topLeft = contour.clip(new RenderContour(new Path(steps)));
        remainder.subtract(topLeft.createArea());

        steps = new ArrayList<>();
        LineRenderer.line(steps, x2, cy, cx, cy, line, period, false, 1.0);
        LineRenderer.line(steps, cx, cy, cx, y1, line, period, false, 1.0);
        steps.add(new LinePathStep(cx, y1, x2, y1));
        steps.add(new LinePathStep(x2, y1, x2, cy));
        RenderContour topRight = contour.clip(new RenderContour(new Path(steps)));
        remainder.subtract(topRight.createArea());

        steps = new ArrayList<>();
        LineRenderer.line(steps, cx, y2, cx, cy, line, period, false, 1.0);
        LineRenderer.line(steps, cx, cy, x2, cy, line, period, false, 1.0);
        steps.add(new LinePathStep(x2, cy, x2, y2));
        steps.add(new LinePathStep(x2, y2, cx, y2));
        RenderContour bottomRight = contour.clip(new RenderContour(new Path(steps)));
        remainder.subtract(bottomRight.createArea());

        RenderContour bottomLeft = GeometryUtils.convertAreaToContour(remainder);

        return Arrays.asList(topLeft, topRight, bottomRight, bottomLeft);
    }

    @Override
    public Path getSpine(RenderContour contour)
    {
        // TODO implement
        return null;
    }
}
