package heraldry.render.ordinary;

import heraldry.model.Line;
import heraldry.render.Box;
import heraldry.render.LineRenderer;
import heraldry.render.Painter;
import heraldry.render.PathStep;
import heraldry.render.Point;
import heraldry.render.RenderContour;
import heraldry.util.GeometryUtils;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class LozengeOrdinaryRenderer implements OrdinaryRenderer
{
    private final double aspectRatio;

    @Override
    public Collection<RenderContour> render(Box bounds, Line line, Painter painter)
    {
        Point center = bounds.getFessPoint();
        double width = bounds.getWidth() * 2 / 3;
        double height = bounds.getHeight() * 2 / 3;
        double sizeX = Math.min(width, height) * aspectRatio;
        double sizeY = Math.min(width, height) / aspectRatio;
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
        double period = painter.getLinePeriodFactor() * Math.min(width, height);
        List<PathStep> steps = new ArrayList<>();
        LineRenderer.line(steps, x1, y2, x2, y1, line, period, false, 1.0);
        LineRenderer.line(steps, x2, y1, x3, y2, line, period, false, 1.0);
        LineRenderer.line(steps, x3, y2, x2, y3, line, period, false, 1.0);
        LineRenderer.line(steps, x2, y3, x1, y2, line, period, false, 1.0);
        return Collections.singleton(new RenderContour(steps));
    }
}
