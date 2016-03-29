package heraldry.render.ordinary;

import heraldry.model.Line;
import heraldry.render.Box;
import heraldry.render.LineRenderer;
import heraldry.render.Painter;
import heraldry.render.RenderContour;
import heraldry.render.path.LinePathStep;
import heraldry.render.path.Path;
import heraldry.render.path.PathStep;
import heraldry.render.path.PathString;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class ChevronOrdinaryRenderer implements OrdinaryRenderer
{
    private final boolean flipY;
    private final double sizeRatio;

    @Override
    public Collection<RenderContour> render(RenderContour contour, Line line, Painter painter)
    {
        Box bounds = contour.getBounds();
        double x1 = bounds.getX1();
        double x2 = bounds.getX2();
        double width = bounds.getWidth();
        double height = bounds.getHeight();
        double y1 = bounds.getY1();
        double cx = (x1 + x2) / 2;
        double cy = width < height ? (y1 + width / 2) : bounds.lerpY(0.5);
        double y2 = cy + cx - x1;
        double step = sizeRatio * painter.getOrdinaryThickness() / Math.sqrt(2) * line.getScaleFactor();
        double period = painter.getLinePeriodFactor() * Math.min(width, height);

        if (flipY)
        {
            List<PathStep> steps = new ArrayList<>();
            LineRenderer.line(steps, x1, y1 - step, cx, cy - step, line, period, flipY, sizeRatio);
            LineRenderer.line(steps, cx, cy - step, x2, y1 - step, line, period, flipY, sizeRatio);
            steps.add(new LinePathStep(x2, y1 - step, x2, y1 + step));
            LineRenderer.line(steps, x2, y1 + step, cx, cy + step, line, period, flipY, sizeRatio);
            LineRenderer.line(steps, cx, cy + step, x1, y1 + step, line, period, flipY, sizeRatio);
            steps.add(new LinePathStep(x1, y1 + step, x1, y1 - step));

            List<PathStep> spine = new ArrayList<>();
            spine.add(new LinePathStep(x1, y1, cx, cy));
            spine.add(new LinePathStep(cx, cy, x2, y1));

            return Collections.singleton(new RenderContour(new Path(steps), new PathString(spine)));
        }

        List<PathStep> steps = new ArrayList<>();
        LineRenderer.line(steps, x1, y2 - step, cx, cy - step, line, period, flipY, sizeRatio);
        LineRenderer.line(steps, cx, cy - step, x2, y2 - step, line, period, flipY, sizeRatio);
        steps.add(new LinePathStep(x2, y2 - step, x2, y2 + step));
        LineRenderer.line(steps, x2, y2 + step, cx, cy + step, line, period, flipY, sizeRatio);
        LineRenderer.line(steps, cx, cy + step, x1, y2 + step, line, period, flipY, sizeRatio);
        steps.add(new LinePathStep(x1, y2 + step, x1, y2 - step));

        List<PathStep> spine = new ArrayList<>();
        spine.add(new LinePathStep(x1, y2, cx, cy));
        spine.add(new LinePathStep(cx, cy, x2, y2));

        return Collections.singleton(new RenderContour(new Path(steps), new PathString(spine)));
    }
}
