package heraldry.render.ordinary;

import heraldry.model.Line;
import heraldry.render.*;
import heraldry.render.path.CubicPathStep;
import heraldry.render.path.LinePathStep;
import heraldry.render.path.PathStep;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class FlaunchesOrdinaryRenderer implements OrdinaryRenderer
{
    @Override
    public Collection<RenderContour> render(RenderContour contour, Line line, Painter painter)
    {
        Box bounds = contour.getBounds();
        double x1 = bounds.getX1();
        double x2 = bounds.getX2();
        double y1 = bounds.getY1();
        double y2 = bounds.getY2();

        double cx = 0.35;
        double cy = 0.35;

        List<PathStep> steps1 = new ArrayList<>();
        steps1.add(new CubicPathStep(x1, y1, bounds.lerpX(cx), bounds.lerpY(cy), bounds.lerpX(cx), bounds.lerpY(1 - cy), x1, y2));
        steps1.add(new LinePathStep(x1, y2, x1, y1));

        List<PathStep> steps2 = new ArrayList<>();
        steps2.add(new LinePathStep(x2, y1, x2, y2));
        steps2.add(new CubicPathStep(x2, y2, bounds.lerpX(1 - cx), bounds.lerpY(1 - cy), bounds.lerpX(1 - cx), bounds.lerpY(cy), x2, y1));

        return Arrays.asList(new RenderContour(steps1), new RenderContour(steps2));
    }
}
