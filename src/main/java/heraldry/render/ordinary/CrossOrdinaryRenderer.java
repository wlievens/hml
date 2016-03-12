package heraldry.render.ordinary;

import heraldry.model.Line;
import heraldry.render.Box;
import heraldry.render.Painter;
import heraldry.render.Point;
import heraldry.render.RenderContour;
import heraldry.util.GeometryUtils;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Collection;

@RequiredArgsConstructor
public class CrossOrdinaryRenderer implements OrdinaryRenderer
{
    private final double sizeRatio;

    @Override
    public Collection<RenderContour> render(Box bounds, Line line, Painter painter)
    {
        Point center = bounds.getFessPoint();
        double step = sizeRatio * painter.getOrdinaryThickness() * 0.5;
        double centerX = center.getX();
        double centerY = center.getY();
        return Arrays.asList(
                new RenderContour(GeometryUtils.rectangle(centerX - step, bounds.getY1(), centerX + step, bounds.getY2())),
                new RenderContour(GeometryUtils.rectangle(bounds.getX1(), centerY - step, bounds.getX2(), centerY + step))
        );
    }
}
