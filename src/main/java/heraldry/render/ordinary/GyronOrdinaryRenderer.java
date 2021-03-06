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
public class GyronOrdinaryRenderer implements OrdinaryRenderer
{
    private final double sizeRatio;
    private final boolean flipX;
    private final boolean flipY;

    @Override
    public RenderContour render(RenderContour contour, Line line, Painter painter)
    {
        Box bounds = contour.getBounds();
        double width = bounds.getWidth();
        double height = bounds.getHeight();
        double x1, y1, x2, y2;
        double size = width * sizeRatio;
        if (flipX)
        {
            x1 = bounds.getX2();
            x2 = x1 - size;
        }
        else
        {
            x1 = bounds.getX1();
            x2 = x1 + size;
        }
        if (flipY)
        {
            y1 = bounds.getY2();
            y2 = y1 - size;
        }
        else
        {
            y1 = bounds.getY1();
            y2 = y1 + size;
        }
        double period = painter.getLinePeriodFactor() * Math.min(width, height);
        List<PathStep> steps = new ArrayList<>();
        LineRenderer.line(steps, x1, y1, x2, y2, line, period, false, sizeRatio);
        LineRenderer.line(steps, x2, y2, x1, y2, line, period, false, sizeRatio);
        steps.add(new LinePathStep(x1, y2, x1, y1));
        return new RenderContour(new Path(steps));
    }
}
