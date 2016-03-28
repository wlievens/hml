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
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class PaleOrdinaryRenderer implements OrdinaryRenderer
{
    private final double sizeRatio;

    @Override
    public Collection<RenderContour> render(RenderContour contour, Line line, Painter painter)
    {
        Box bounds = contour.getBounds();
        double x1 = bounds.getX1();
        double y1 = bounds.getY1();
        double x2 = bounds.getX2();
        double y2 = bounds.getY2();
        double width = bounds.getWidth();
        double height = bounds.getHeight();
        double step = sizeRatio * painter.getOrdinaryThickness() * line.getScaleFactor() / 2;
        double midX = (x1 + x2) / 2;
        double period = painter.getLinePeriodFactor() * Math.min(width, height);
        List<PathStep> steps = new ArrayList<>();
        steps.add(new LinePathStep(midX - step, y1, midX + step, y1));
        LineRenderer.line(steps, midX + step, y1, midX + step, y2, line, period, false, sizeRatio);
        steps.add(new LinePathStep(midX + step, y2, midX - step, y2));
        LineRenderer.line(steps, midX - step, y2, midX - step, y1, line, period, false, sizeRatio);
        return Arrays.asList(new RenderContour(new Path(steps)));
    }
}
