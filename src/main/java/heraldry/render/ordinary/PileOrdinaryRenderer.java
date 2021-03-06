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
public class PileOrdinaryRenderer implements OrdinaryRenderer
{
    private final boolean flipY;

    @Override
    public RenderContour render(RenderContour contour, Line line, Painter painter)
    {
        Box bounds = contour.getBounds();
        double period = painter.getLinePeriodFactor() * Math.min(bounds.getWidth(), bounds.getHeight());
        double margin = 0.15;
        double x1 = bounds.lerpX(margin);
        double x2 = bounds.lerpX(1 - margin);
        double midX = (x1 + x2) / 2;

        if (flipY)
        {
            double y1 = bounds.lerpY(margin);
            double y2 = bounds.getY2();
            List<PathStep> steps = new ArrayList<>();
            LineRenderer.line(steps, midX, y1, x2, y2, line, period, false, 1.0);
            steps.add(new LinePathStep(x2, y2, x1, y2));
            LineRenderer.line(steps, x1, y2, midX, y1, line, period, false, 1.0);
            return new RenderContour(new Path(steps));
        }

        double y1 = bounds.getY1();
        double y2 = bounds.lerpY(1 - margin);
        List<PathStep> steps = new ArrayList<>();
        steps.add(new LinePathStep(x1, y1, x2, y1));
        LineRenderer.line(steps, x2, y1, midX, y2, line, period, false, 1.0);
        LineRenderer.line(steps, midX, y2, x1, y1, line, period, false, 1.0);
        return new RenderContour(new Path(steps));
    }
}
