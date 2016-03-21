package heraldry.render.ordinary;

import heraldry.model.Line;
import heraldry.render.Box;
import heraldry.render.path.LinePathStep;
import heraldry.render.LineRenderer;
import heraldry.render.Painter;
import heraldry.render.path.PathStep;
import heraldry.render.RenderContour;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class BatonOrdinaryRenderer implements OrdinaryRenderer
{
    private final boolean flipX;

    @Override
    public Collection<RenderContour> render(RenderContour contour, Line line, Painter painter)
    {
        Box bounds = contour.getBounds();
        double margin = bounds.getWidth() * 0.25;
        double x1 = bounds.getX1() + margin;
        double x2 = bounds.getX2() - margin;
        double y1 = bounds.getY1() + margin;
        double y2 = y1 + x2 - x1;
        double step = painter.getOrdinaryThickness() / Math.sqrt(2) / 4 * line.getScaleFactor();
        double period = painter.getLinePeriodFactor() * Math.min(bounds.getWidth(), bounds.getHeight());

        List<PathStep> steps = new ArrayList<>();
        if (flipX)
        {
            LineRenderer.line(steps, x2 + step, y1 + step, x1 + step, y2 + step, line, period, flipX, 1.0);
            steps.add(new LinePathStep(x1 + step, y2 + step, x1 - step, y2 - step));
            LineRenderer.line(steps, x1 - step, y2 - step, x2 - step, y1 - step, line, period, flipX, 1.0);
            steps.add(new LinePathStep(x2 - step, y1 - step, x2 + step, y1 + step));
        }
        else
        {
            LineRenderer.line(steps, x1 + step, y1 - step, x2 + step, y2 - step, line, period, flipX, 1.0);
            steps.add(new LinePathStep(x2 + step, y2 - step, x2 - step, y2 + step));
            LineRenderer.line(steps, x2 - step, y2 + step, x1 - step, y1 + step, line, period, flipX, 1.0);
            steps.add(new LinePathStep(x1 - step, y1 + step, x1 + step, y1 - step));
        }
        return Arrays.asList(new RenderContour(steps));
    }
}
