package heraldry.render.variation;

import heraldry.model.Line;
import heraldry.model.Tincture;
import heraldry.render.Box;
import heraldry.render.LinePathStep;
import heraldry.render.LineRenderer;
import heraldry.render.Painter;
import heraldry.render.PathStep;
import heraldry.render.RenderContour;
import heraldry.render.RenderShape;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class BendyVariationRenderer implements VariationRenderer
{
    private final boolean flipX;

    @Override
    public Collection<RenderShape> render(RenderContour contour, Tincture firstTincture, Tincture secondTincture, Line line, Painter painter)
    {
        List<RenderShape> list = new ArrayList<>();
        list.add(new RenderShape(contour.getSteps(), painter.getColor(firstTincture), null));
        Box bounds = contour.getBounds();
        double x1 = bounds.getX1();
        double x2 = bounds.getX2();
        double width = bounds.getWidth();
        double height = bounds.getHeight();
        double step = painter.getOrdinaryThickness() / Math.sqrt(2) * line.getScaleFactor();
        double interval = step * 4;
        for (double y = bounds.getY1() - width; y <= bounds.getY2(); y += interval)
        {
            double startX = flipX ? x2 : x1;
            double startY = y;
            double size = Math.max(width, height);
            double endX = flipX ? (x2 - size) : (x1 + size);
            double endY = y + size;
            double period = painter.getLinePeriodFactor() * Math.min(width, height);

            List<PathStep> steps = new ArrayList<>();
            LineRenderer.plotLine(steps, startX, startY - step, endX, endY - step, line, period, flipX);
            steps.add(new LinePathStep(endX, endY - step, endX, endY + step));
            LineRenderer.plotLine(steps, endX, endY + step, startX, startY + step, line, period, flipX);
            steps.add(new LinePathStep(startX, startY + step, startX, startY - step));

            list.addAll(contour.clip(new RenderShape(steps, painter.getColor(secondTincture), null)));
        }

        return list;
    }
}