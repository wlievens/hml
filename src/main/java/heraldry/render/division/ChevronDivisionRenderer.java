package heraldry.render.division;

import heraldry.model.Line;
import heraldry.render.Box;
import heraldry.render.LinePathStep;
import heraldry.render.LineRenderer;
import heraldry.render.Painter;
import heraldry.render.PathStep;
import heraldry.render.Point;
import heraldry.render.RenderContour;
import heraldry.util.CollectionUtils;
import heraldry.util.GeometryUtils;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public class ChevronDivisionRenderer implements DivisionRenderer
{
    private final boolean flipY;

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
        double size = Math.max(bounds.getWidth(), bounds.getHeight());
        double period = painter.getLinePeriodFactor() * Math.min(bounds.getWidth(), bounds.getHeight());

        List<PathStep> steps = new ArrayList<>();

        if (flipY)
        {
            steps.add(new LinePathStep(x1, y1, x2, y1));
            LineRenderer.line(steps, x2, y1, cx, cy, line, period, false, 1.0);
            LineRenderer.line(steps, cx, cy, x1, y1, line, period, false, 1.0);

            RenderContour top = CollectionUtils.single(contour.clip(new RenderContour(steps)));
            RenderContour bottom = CollectionUtils.single(GeometryUtils.subtract(contour, top));
            return Arrays.asList(bottom, top);
        }

        LineRenderer.line(steps, cx, cy, cx + size, cy + size, line, period, false, 1.0);
        steps.add(new LinePathStep(cx + size, cy + size, cx - size, cy + size));
        LineRenderer.line(steps, cx - size, cy + size, cx, cy, line, period, false, 1.0);

        RenderContour bottom = CollectionUtils.single(contour.clip(new RenderContour(steps)));
        RenderContour top = CollectionUtils.single(GeometryUtils.subtract(contour, bottom));
        return Arrays.asList(top, bottom);
    }
}
