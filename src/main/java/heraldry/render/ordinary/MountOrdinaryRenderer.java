package heraldry.render.ordinary;

import heraldry.model.Line;
import heraldry.render.Box;
import heraldry.render.path.CubicPathStep;
import heraldry.render.path.LinePathStep;
import heraldry.render.Painter;
import heraldry.render.RenderContour;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Collection;

@RequiredArgsConstructor
public class MountOrdinaryRenderer implements OrdinaryRenderer
{
    @Override
    public Collection<RenderContour> render(RenderContour contour, Line line, Painter painter)
    {
        Box bounds = contour.getBounds();
        double x1 = bounds.getX1();
        double x2 = bounds.getX2();
        double y2 = bounds.getY2();
        double c1 = -0.25;
        double c2 = 0.50;
        return contour.clip(new RenderContour(Arrays.asList(
                new CubicPathStep(x1, y2, bounds.lerpX(c1), bounds.lerpY(c2), bounds.lerpX(1 - c1), bounds.lerpY(c2), x2, y2),
                new LinePathStep(x2, y2, x1, y2)
        )));
    }
}
