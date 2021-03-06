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
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class BendOrdinaryRenderer implements OrdinaryRenderer
{
    private final boolean flipX;
    private final double sizeRatio;

    @Override
    public RenderContour render(RenderContour contour, Line line, Painter painter)
    {
        Box bounds = contour.getBounds();
        double x1 = bounds.getX1();
        double y1 = bounds.getY1();
        double x2 = bounds.getX2();
        double width = bounds.getWidth();
        double height = bounds.getHeight();
        double step = sizeRatio * painter.getOrdinaryThickness() / Math.sqrt(2) * line.getScaleFactor();
        double startX = flipX ? x2 : x1;
        double startY = y1;
        double size = Math.max(width, height);
        double endX = flipX ? (x2 - size) : (x1 + size);
        double endY = y1 + size;
        double period = painter.getLinePeriodFactor() * Math.min(width, height);
        List<PathStep> steps = new ArrayList<>();
        LineRenderer.line(steps, startX, startY - step, endX, endY - step, line, period, flipX, sizeRatio);
        steps.add(new LinePathStep(endX, endY - step, endX, endY + step));
        LineRenderer.line(steps, endX, endY + step, startX, startY + step, line, period, flipX, sizeRatio);
        steps.add(new LinePathStep(startX, startY + step, startX, startY - step));
        return new RenderContour(new Path(steps));
    }
}
