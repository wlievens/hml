package heraldry.render.ordinary;

import heraldry.model.Line;
import heraldry.model.Tincture;
import heraldry.render.Box;
import heraldry.render.Painter;
import heraldry.render.Point;
import heraldry.render.RenderShape;
import heraldry.util.GeometryUtils;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Collection;

@RequiredArgsConstructor
public class CrossOrdinaryRenderer implements OrdinaryRenderer
{
    @Override
    public Collection<RenderShape> render(Box bounds, Tincture tincture, Line line, Painter painter)
    {
        Point center = bounds.getFessPoint();
        double step = painter.getOrdinaryThickness() * 0.5;
        double centerX = center.getX();
        double centerY = center.getY();
        return Arrays.asList(
                new RenderShape(GeometryUtils.rectangle(centerX - step, bounds.getY1(), centerX + step, bounds.getY2()), painter.getColor(tincture), null),
                new RenderShape(GeometryUtils.rectangle(bounds.getX1(), centerY - step, bounds.getX2(), centerY + step), painter.getColor(tincture), null)
        );
    }
}
