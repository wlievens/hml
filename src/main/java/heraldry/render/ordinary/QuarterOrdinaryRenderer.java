package heraldry.render.ordinary;

import heraldry.model.Line;
import heraldry.render.Box;
import heraldry.render.path.LinePathStep;
import heraldry.render.LineRenderer;
import heraldry.render.Painter;
import heraldry.render.path.Path;
import heraldry.render.path.PathStep;
import heraldry.render.RenderContour;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class QuarterOrdinaryRenderer implements OrdinaryRenderer
{
    private final double sizeRatio;

    @Override
    public Collection<RenderContour> render(RenderContour contour, Line line, Painter painter)
    {
        Box bounds = contour.getBounds();
        double step = sizeRatio * bounds.getWidth() * line.getScaleFactor();
        double x1 = bounds.getX1();
        double y1 = bounds.getY1();
        double x2 = x1 + step;
        double y2 = y1 + step;
        double width = bounds.getWidth();
        double height = bounds.getHeight();
        double period = painter.getLinePeriodFactor() * Math.min(width, height);
        List<PathStep> steps = new ArrayList<>();
        steps.add(new LinePathStep(x1, y1, x2, y1));
        LineRenderer.line(steps, x2, y1, x2, y2, line, period, false, sizeRatio);
        LineRenderer.line(steps, x2, y2, x1, y2, line, period, false, sizeRatio);
        steps.add(new LinePathStep(x1, y2, x1, y1));
        return Arrays.asList(new RenderContour(new Path(steps)));
    }
}
