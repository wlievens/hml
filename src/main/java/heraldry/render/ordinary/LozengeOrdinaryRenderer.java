package heraldry.render.ordinary;

import heraldry.model.Line;
import heraldry.render.Box;
import heraldry.render.LineRenderer;
import heraldry.render.Painter;
import heraldry.render.Point;
import heraldry.render.RenderContour;
import heraldry.render.path.LinePathStep;
import heraldry.render.path.Path;
import heraldry.render.path.PathStep;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class LozengeOrdinaryRenderer implements OrdinaryRenderer
{
    private final double sizeXRatio;
    private final double sizeYRatio;
    private final boolean open;

    @Override
    public Collection<RenderContour> render(RenderContour contour, Line line, Painter painter)
    {
        Box bounds = contour.getBounds();
        Point center = bounds.getFessPoint();
        double size = Math.min(bounds.getWidth(), bounds.getHeight());
        double scale = 2.0 / 3.0;
        double sizeX = size * scale * sizeXRatio;
        double sizeY = size * scale * sizeYRatio;
        //      X2
        //     /:\      :: Y1
        //    /   \
        // X1 :    : X3 :: Y2
        //    \   /
        //     \:/      :: Y3
        //      X2
        double x1 = center.getX() - sizeX / 2;
        double x2 = center.getX();
        double x3 = center.getX() + sizeX / 2;
        double y1 = center.getY() - sizeY / 2;
        double y2 = center.getY();
        double y3 = center.getY() + sizeY / 2;
        double step = painter.getFretThickness() * Math.sqrt(2);
        double period = painter.getLinePeriodFactor() * size;

        if (open)
        {
            List<PathStep> steps1 = new ArrayList<>();
            LineRenderer.line(steps1, x1, y2, x2, y1, line, period, false, 1.0);
            LineRenderer.line(steps1, x2, y1, x3, y2, line, period, false, 1.0);
            steps1.add(new LinePathStep(x3, y2, x3 - step, y2));
            LineRenderer.line(steps1, x3 - step, y2, x2, y1 + step, line, period, false, 1.0);
            LineRenderer.line(steps1, x2, y1 + step, x1 + step, y2, line, period, false, 1.0);
            steps1.add(new LinePathStep(x1 + step, y2, x1, y2));

            List<PathStep> steps2 = new ArrayList<>();
            steps2.add(new LinePathStep(x1, y2, x1 + step, y2));
            LineRenderer.line(steps2, x1 + step, y2, x2, y3 - step, line, period, false, 1.0);
            LineRenderer.line(steps2, x2, y3 - step, x3 - step, y2, line, period, false, 1.0);
            steps2.add(new LinePathStep(x3 - step, y2, x3, y2));
            LineRenderer.line(steps2, x3, y2, x2, y3, line, period, false, 1.0);
            LineRenderer.line(steps2, x2, y3, x1, y2, line, period, false, 1.0);

            return Arrays.asList(new RenderContour(new Path(steps1)), new RenderContour(new Path(steps2)));
        }

        List<PathStep> steps = new ArrayList<>();
        LineRenderer.line(steps, x1, y2, x2, y1, line, period, false, 1.0);
        LineRenderer.line(steps, x2, y1, x3, y2, line, period, false, 1.0);
        LineRenderer.line(steps, x3, y2, x2, y3, line, period, false, 1.0);
        LineRenderer.line(steps, x2, y3, x1, y2, line, period, false, 1.0);
        return Collections.singleton(new RenderContour(new Path(steps)));
    }
}
