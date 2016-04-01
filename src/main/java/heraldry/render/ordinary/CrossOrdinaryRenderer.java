package heraldry.render.ordinary;

import heraldry.model.Line;
import heraldry.render.Box;
import heraldry.render.LineRenderer;
import heraldry.render.Painter;
import heraldry.render.Point;
import heraldry.render.RenderContour;
import heraldry.render.path.LinePathStep;
import heraldry.render.path.Path;
import heraldry.render.path.PathStep;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class CrossOrdinaryRenderer implements OrdinaryRenderer
{
    private final double sizeRatio;

    @Override
    public Collection<RenderContour> render(RenderContour contour, Line line, Painter painter)
    {
        Box bounds = contour.getBounds();
        Point center = bounds.getFessPoint();
        double step = sizeRatio * painter.getOrdinaryThickness() * 0.5;
        double centerX = center.getX();
        double centerY = center.getY();
        double y1 = bounds.getY1();
        double x1 = bounds.getX1();
        double x2 = bounds.getX2();
        double y2 = bounds.getY2();
        double width = bounds.getWidth();
        double height = bounds.getHeight();
        double period = painter.getLinePeriodFactor() * Math.min(width, height);

        List<PathStep> steps = new ArrayList<>();
        steps.add(new LinePathStep(centerX - step, y1, centerX + step, y1));
        LineRenderer.line(steps, centerX + step, y1, centerX + step, centerY - step, line, period, false, sizeRatio);
        LineRenderer.line(steps, centerX + step, centerY - step, x2, centerY - step, line, period, false, sizeRatio);
        steps.add(new LinePathStep(x2, centerY - step, x2, centerY + step));
        LineRenderer.line(steps, x2, centerY + step, centerX + step, centerY + step, line, period, false, sizeRatio);
        LineRenderer.line(steps, centerX + step, centerY + step, centerX + step, y2, line, period, false, sizeRatio);
        steps.add(new LinePathStep(centerX + step, y2, centerX - step, y2));
        LineRenderer.line(steps, centerX - step, y2, centerX - step, centerY + step, line, period, false, sizeRatio);
        LineRenderer.line(steps, centerX - step, centerY + step, x1, centerY + step, line, period, false, sizeRatio);
        steps.add(new LinePathStep(x1, centerY + step, x1, centerY - step));
        LineRenderer.line(steps, x1, centerY - step, centerX - step, centerY - step, line, period, false, sizeRatio);
        LineRenderer.line(steps, centerX - step, centerY - step, centerX - step, y1, line, period, false, sizeRatio);
        return Collections.singleton(new RenderContour(new Path(steps)));
    }
}
