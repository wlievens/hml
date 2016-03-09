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
public class BendOrdinaryRenderer extends AbstractOrdinaryRenderer
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
        double startX = flip ? x2 : x1;
        double startY = y1;
        double size = Math.max(width, height);
        double endX = flip ? (x2 - size) : (x1 + size);
        double endY = y1 + size;
        double period = painter.getLinePeriodFactor() * Math.min(width, height);
        List<PathStep> steps = new ArrayList<>();
        plotLine(steps, startX, startY - step, endX, endY - step, line, period, flip);
        steps.add(new LinePathStep(endX, endY - step, endX, endY + step));
        plotLine(steps, endX, endY + step, startX, startY + step, line, period, flip);
        steps.add(new LinePathStep(startX, startY + step, startX, startY - step));
        return Arrays.asList(new RenderShape(steps, painter.getColor(tincture), null));
    }
}
