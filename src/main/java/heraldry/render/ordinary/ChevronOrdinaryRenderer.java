package heraldry.render.ordinary;

import heraldry.model.Line;
import heraldry.render.Box;
import heraldry.render.LinePathStep;
import heraldry.render.LineRenderer;
import heraldry.render.Painter;
import heraldry.render.PathStep;
import heraldry.render.RenderContour;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class ChevronOrdinaryRenderer implements OrdinaryRenderer
{
    private final boolean inverted;
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
        double midX = (x1 + x2) / 2;
        double midY = width < height ? (y1 + width / 2) : bounds.lerpY(0.5);
        double y2 = midY + midX - x1;
        double step = sizeRatio * painter.getOrdinaryThickness() / Math.sqrt(2) * line.getScaleFactor();
        double period = painter.getLinePeriodFactor() * Math.min(width, height);
        List<PathStep> steps = new ArrayList<>();
        if (inverted)
        {
            steps.add(new LinePathStep(x1, y1, x1 + step, y1));
            LineRenderer.line(steps, x1 + step, y1, midX, midY - step, line, period, inverted, sizeRatio);
            LineRenderer.line(steps, midX, midY - step, x2 - step, y1, line, period, inverted, sizeRatio);
            steps.add(new LinePathStep(x2 - step, y1, x2, y1));
            steps.add(new LinePathStep(x2, y1, x2, y1 + step));
            LineRenderer.line(steps, x2, y1 + step, midX, midY + step, line, period, inverted, sizeRatio);
            LineRenderer.line(steps, midX, midY + step, x1, y1 + step, line, period, inverted, sizeRatio);
            steps.add(new LinePathStep(x1, y1 + step, x1, y1));
        }
        else
        {
            LineRenderer.line(steps, x1, y2 - step, midX, midY - step, line, period, inverted, sizeRatio);
            LineRenderer.line(steps, midX, midY - step, x2, y2 - step, line, period, inverted, sizeRatio);
            steps.add(new LinePathStep(x2, y2 - step, x2, y2 + step));
            LineRenderer.line(steps, x2, y2 + step, midX, midY + step, line, period, inverted, sizeRatio);
            LineRenderer.line(steps, midX, midY + step, x1, y2 + step, line, period, inverted, sizeRatio);
            steps.add(new LinePathStep(x1, y2 + step, x1, y2 - step));
        }
        return Collections.singleton(new RenderContour(steps));
    }
}
