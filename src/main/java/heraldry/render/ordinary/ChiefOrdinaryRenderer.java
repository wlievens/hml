package heraldry.render.ordinary;

import heraldry.model.Line;
import heraldry.model.Tincture;
import heraldry.render.Box;
import heraldry.render.Painter;
import heraldry.render.RenderShape;
import heraldry.util.GeometryUtils;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Collections;

@RequiredArgsConstructor
public class ChiefOrdinaryRenderer extends AbstractOrdinaryRenderer
{
    @Override
    public Collection<RenderShape> render(Box bounds, Tincture tincture, Line line, Painter painter)
    {
        return Collections.singleton(new RenderShape(GeometryUtils.rectangle(bounds.getX1(), bounds.getY1(), bounds.getX2(), bounds.lerpY(0.25)), painter.getColor(tincture), null));
    }
}
