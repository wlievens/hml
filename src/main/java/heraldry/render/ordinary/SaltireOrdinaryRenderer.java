package heraldry.render.ordinary;

import heraldry.model.Line;
import heraldry.model.Tincture;
import heraldry.render.Box;
import heraldry.render.LinePathStep;
import heraldry.render.LineRenderer;
import heraldry.render.Painter;
import heraldry.render.PathStep;
import heraldry.render.RenderShape;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class SaltireOrdinaryRenderer implements OrdinaryRenderer
{
    @Override
    public Collection<RenderShape> render(Box bounds, Tincture tincture, Line line, Painter painter)
    {
        double x1 = bounds.getX1();
        double y1 = bounds.getY1();
        double x2 = bounds.getX2();
        double y2 = bounds.getY2();
        double width = bounds.getWidth();
        double height = bounds.getHeight();
        double step = painter.getOrdinaryThickness() / Math.sqrt(2) * line.getScaleFactor();
        double period = painter.getLinePeriodFactor() * Math.min(width, height);
        double midX = (x1 + x2) / 2;
        List<PathStep> steps = new ArrayList<>();

        double midY = y1 + width / 2;
        steps.add(new LinePathStep(x1, y1, x1 + step, y1));
        LineRenderer.plotLine(steps, x1 + step, y1, midX, midY - step, line, period, false);
        LineRenderer.plotLine(steps, midX, midY - step, x2 - step, y1, line, period, false);
        steps.add(new LinePathStep(x2 - step, y1, x2, y1));
        LineRenderer.plotLine(steps, x2, y1, x2, y1 + step, line, period, false);
        LineRenderer.plotLine(steps, x2, y1 + step, midX + step, midY, line, period, false);
        LineRenderer.plotLine(steps, midX + step, midY, x2, y1 + width - step, line, period, false);
        steps.add(new LinePathStep(x2, y1 + width - step, x2, y1 + width + step));
        LineRenderer.plotLine(steps, x2, y1 + width + step, midX, midY + step, line, period, false);
        LineRenderer.plotLine(steps, midX, midY + step, x1, y1 + width + step, line, period, false);
        steps.add(new LinePathStep(x1, y1 + width + step, x1, y1 + width - step));
        LineRenderer.plotLine(steps, x1, y1 + width - step, midX - step, midY, line, period, false);
        LineRenderer.plotLine(steps, midX - step, midY, x1, y1 + step, line, period, false);
        steps.add(new LinePathStep(x1, y1 + step, x1, y1));

        return Arrays.asList(new RenderShape(steps, painter.getColor(tincture), null));
    }
}
