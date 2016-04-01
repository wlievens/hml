package heraldry.render.ordinary;

import heraldry.model.Line;
import heraldry.render.Box;
import heraldry.render.Painter;
import heraldry.render.Point;
import heraldry.render.RenderContour;
import heraldry.render.path.CubicPathStep;
import heraldry.render.path.LinePathStep;
import heraldry.render.path.Path;
import heraldry.render.path.PathStep;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class GoreOrdinaryRenderer implements OrdinaryRenderer
{
    private final boolean flipX;

    @Override
    public RenderContour render(RenderContour contour, Line line, Painter painter)
    {
        Box bounds = contour.getBounds();
        double x1 = bounds.getX1();
        double x2 = bounds.getX2();
        double y1 = bounds.getY1();
        double y2 = bounds.getY2();
        Point center = bounds.getFessPoint();
        double centerX = center.getX();
        double centerY = center.getY();

        double w = centerX - x1;
        double h1 = centerY - y1;
        double h2 = y2 - centerY;

        double c1 = 0.30;
        double c2 = 0.80;
        double c3 = 0.50;

        List<PathStep> steps = new ArrayList<>();
        if (flipX)
        {
            steps.add(new LinePathStep(x2, y1, x2, y2));
            steps.add(new LinePathStep(x2, y2, centerX, y2));
            steps.add(new CubicPathStep(centerX, y2, x2 - w * c2, centerY + h2 * (1 - c1), x2 - w * c2, centerY + h2 * c1, centerX, centerY));
            steps.add(new CubicPathStep(centerX, centerY, x2 - w * c3, y1 + h1 * c2, x2 - w * c1, y1 + h1 * c1, x2, y1));
        }
        else
        {
            steps.add(new CubicPathStep(x1, y1, x1 + w * c1, y1 + h1 * c1, x1 + w * c3, y1 + h1 * c2, centerX, centerY));
            steps.add(new CubicPathStep(centerX, centerY, x1 + w * c2, centerY + h2 * c1, x1 + w * c2, centerY + h2 * (1 - c1), centerX, y2));
            steps.add(new LinePathStep(centerX, y2, x1, y2));
            steps.add(new LinePathStep(x1, y2, x1, y1));
        }
        return new RenderContour(new Path(steps));
    }
}
