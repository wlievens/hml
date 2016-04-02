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
import lombok.RequiredArgsConstructor;

import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public class PallDivisionRenderer implements DivisionRenderer
{
    private final boolean flipY;

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
        double y2 = cy + cx - x1;
        double y3 = bounds.getY2();
        double period = painter.getLinePeriodFactor() * Math.min(bounds.getWidth(), bounds.getHeight());

        Area remainder = contour.createArea();

        if (flipY)
        {
            List<PathStep> steps = new ArrayList<>();
            steps.add(new LinePathStep(x1, y1, x2, y1));
            LineRenderer.line(steps, x2, y1, cx, cy, line, period, false, 1.0);
            LineRenderer.line(steps, cx, cy, x1, y1, line, period, false, 1.0);
            RenderContour top = contour.clip(new RenderContour(new Path(steps)));
            remainder.subtract(top.createArea());

            steps = new ArrayList<>();
            steps.add(new LinePathStep(x2, y1, x2, y3));
            steps.add(new LinePathStep(x2, y3, cx, y3));
            LineRenderer.line(steps, cx, y3, cx, cy, line, period, false, 1.0);
            RenderContour right = contour.clip(new RenderContour(new Path(steps)));
            remainder.subtract(right.createArea());

            RenderContour left = GeometryUtils.convertAreaToContour(remainder);

            return Arrays.asList(top, right, left);
        }

        List<PathStep> steps = new ArrayList<>();
        steps.add(new LinePathStep(x1, y3, x1, y2));
        LineRenderer.line(steps, x1, y2, cx, cy, line, period, false, 1.0);
        LineRenderer.line(steps, cx, cy, x2, y2, line, period, false, 1.0);
        steps.add(new LinePathStep(x2, y2, x2, y3));
        steps.add(new LinePathStep(x2, y3, x1, y3));
        RenderContour bottom = contour.clip(new RenderContour(new Path(steps)));
        remainder.subtract(bottom.createArea());

        steps = new ArrayList<>();
        steps.add(new LinePathStep(x1, y1, cx, y1));
        LineRenderer.line(steps, cx, y1, cx, cy, line, period, false, 1.0);
        steps.add(new LinePathStep(cx, cy, cx, y3));
        steps.add(new LinePathStep(cx, y2, x1, y3));
        steps.add(new LinePathStep(x1, y3, x1, y1));
        RenderContour left = contour.clip(new RenderContour(new Path(steps)));
        remainder.subtract(left.createArea());

        RenderContour right = GeometryUtils.convertAreaToContour(remainder);

        return Arrays.asList(left, right, bottom);
    }

    @Override
    public Path getSpine(RenderContour contour)
    {
        // TODO implement
        return null;
    }
}
