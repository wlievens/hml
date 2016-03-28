package heraldry.render.ordinary;

import heraldry.model.Line;
import heraldry.render.Box;
import heraldry.render.path.CubicPathStep;
import heraldry.render.path.LinePathStep;
import heraldry.render.Painter;
import heraldry.render.path.Path;
import heraldry.render.path.PathStep;
import heraldry.render.Point;
import heraldry.render.RenderContour;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class RoundelOrdinaryRenderer implements OrdinaryRenderer
{
    private final boolean open;

    @Override
    public Collection<RenderContour> render(RenderContour contour, Line line, Painter painter)
    {
        Box bounds = contour.getBounds();
        Point center = bounds.getFessPoint();
        double width = bounds.getWidth();
        double height = bounds.getHeight();
        double radius = Math.min(width, height) / 4;
        double x1 = center.getX() - radius;
        double x2 = center.getX();
        double x3 = center.getX() + radius;
        double y1 = center.getY() - radius;
        double y2 = center.getY();
        double y3 = center.getY() + radius;
        double step = painter.getOrdinaryThickness() / 2;
        double controlFactor = (4.0 / 3.0) * Math.tan(Math.PI / 8);
        double control1 = radius * controlFactor;
        double control2 = (radius - step) * controlFactor;

        if (open)
        {
            List<PathStep> steps1 = new ArrayList<>();
            steps1.add(new CubicPathStep(x1, y2, x1, y2 - control1, x2 - control1, y1, x2, y1));
            steps1.add(new CubicPathStep(x2, y1, x2 + control1, y1, x3, y2 - control1, x3, y2));
            steps1.add(new LinePathStep(x3, y2, x3 - step, y2));
            steps1.add(new CubicPathStep(x3 - step, y2, x3 - step, y2 - control2, x2 + control2, y1 + step, x2, y1 + step));
            steps1.add(new CubicPathStep(x2, y1 + step, x2 - control2, y1 + step, x1 + step, y2 - control2, x1 + step, y2));
            steps1.add(new LinePathStep(x1 + step, y2, x1, y2));

            List<PathStep> steps2 = new ArrayList<>();
            steps2.add(new LinePathStep(x1, y2, x1 + step, y2));
            steps2.add(new CubicPathStep(x1 + step, y2, x1 + step, y2 + control2, x2 - control2, y3 - step, x2, y3 - step));
            steps2.add(new CubicPathStep(x2, y3 - step, x2 + control2, y3 - step, x3 - step, y2 + control2, x3 - step, y2));
            steps2.add(new LinePathStep(x3 - step, y2, x3, y2));
            steps2.add(new CubicPathStep(x3, y2, x3, y2 + control1, x2 + control1, y3, x2, y3));
            steps2.add(new CubicPathStep(x2, y3, x2 - control1, y3, x1, y2 + control1, x1, y2));

            return Arrays.asList(new RenderContour(new Path(steps1)), new RenderContour(new Path(steps2)));
        }

        List<PathStep> steps = new ArrayList<>();
        steps.add(new CubicPathStep(x1, y2, x1, y2 - control1, x2 - control1, y1, x2, y1));
        steps.add(new CubicPathStep(x2, y1, x2 + control1, y1, x3, y2 - control1, x3, y2));
        steps.add(new CubicPathStep(x3, y2, x3, y2 + control1, x2 + control1, y3, x2, y3));
        steps.add(new CubicPathStep(x2, y3, x2 - control1, y3, x1, y2 + control1, x1, y2));

        return Collections.singleton(new RenderContour(new Path(steps)));
    }
}
