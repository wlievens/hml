package heraldry.render.ordinary;

import heraldry.model.Line;
import heraldry.render.Box;
import heraldry.render.LineRenderer;
import heraldry.render.Painter;
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
public class FessOrdinaryRenderer implements OrdinaryRenderer
{
    private final double sizeRatio;

    @Override
    public Collection<RenderContour> render(RenderContour contour, Line line, Painter painter)
    {
        Box bounds = contour.getBounds();
        double x1 = bounds.getX1();
        double y1 = bounds.getY1();
        double x2 = bounds.getX2();
        double width = bounds.getWidth();
        double height = bounds.getHeight();
        double step = sizeRatio * painter.getOrdinaryThickness() * line.getScaleFactor() / 2;
        double midY = y1 + width / 2;
        double period = painter.getLinePeriodFactor() * Math.min(width, height);
        List<PathStep> steps = new ArrayList<>();
        LineRenderer.line(steps, x1, midY - step, x2, midY - step, line, period, false, sizeRatio);
        steps.add(new LinePathStep(x2, midY - step, x2, midY + step));
        LineRenderer.line(steps, x2, midY + step, x1, midY + step, line, period, true, sizeRatio);
        steps.add(new LinePathStep(x1, midY + step, x1, midY - step));
        return Collections.singleton(new RenderContour(new Path(steps)));
    }
}
