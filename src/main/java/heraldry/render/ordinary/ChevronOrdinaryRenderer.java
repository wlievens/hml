package heraldry.render.ordinary;

import heraldry.model.Line;
import heraldry.model.Tincture;
import heraldry.render.*;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class ChevronOrdinaryRenderer implements OrdinaryRenderer
{
    private final boolean flip;
    private final double sizeRatio;

    @Override
    public Collection<RenderShape> render(Box bounds, Tincture tincture, Line line, Painter painter)
    {
        double x1 = bounds.getX1();
        double y1 = bounds.getY1();
        double x2 = bounds.getX2();
        double y2 = bounds.getY2();
        double width = bounds.getWidth();
        double height = bounds.getHeight();
        double step = sizeRatio * painter.getOrdinaryThickness() / Math.sqrt(2) * line.getScaleFactor();
        double period = painter.getLinePeriodFactor() * Math.min(width, height);
        double midX = (x1 + x2) / 2;
        List<PathStep> steps = new ArrayList<>();
        if (flip)
        {
            double midY = y1 + width / 2;
            steps.add(new LinePathStep(x1, y1, x1 + step, y1));
            LineRenderer.plotLine(steps, x1 + step, y1, midX, midY - step, line, period, flip);
            LineRenderer.plotLine(steps, midX, midY - step, x2 - step, y1, line, period, flip);
            steps.add(new LinePathStep(x2 - step, y1, x2, y1));
            steps.add(new LinePathStep(x2, y1, x2, y1 + step));
            LineRenderer.plotLine(steps, x2, y1 + step, midX, midY + step, line, period, flip);
            LineRenderer.plotLine(steps, midX, midY + step, x1, y1 + step, line, period, flip);
            steps.add(new LinePathStep(x1, y1 + step, x1, y1));
        }
        else
        {
            double midY = y2 - width / 2;
            LineRenderer.plotLine(steps, x1, y2 - step, midX, midY - step, line, period, flip);
            LineRenderer.plotLine(steps, midX, midY - step, x2, y2 - step, line, period, flip);
            steps.add(new LinePathStep(x2, y2 - step, x2, y2));
            steps.add(new LinePathStep(x2, y2, x2 - step, y2));
            LineRenderer.plotLine(steps, x2 - step, y2, midX, midY + step, line, period, flip);
            LineRenderer.plotLine(steps, midX, midY + step, x1 + step, y2, line, period, flip);
            steps.add(new LinePathStep(x1 + step, y2, x1, y2));
            steps.add(new LinePathStep(x1, y2, x1, y2 - step));
        }
        return Arrays.asList(new RenderShape(steps, painter.getColor(tincture), null));
    }
}
