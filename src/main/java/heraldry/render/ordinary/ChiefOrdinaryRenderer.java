package heraldry.render.ordinary;

import heraldry.model.Line;
import heraldry.render.Box;
import heraldry.render.Painter;
import heraldry.render.RenderContour;
import heraldry.util.GeometryUtils;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Collections;

@RequiredArgsConstructor
public class ChiefOrdinaryRenderer implements OrdinaryRenderer
{
    @Override
    public Collection<RenderContour> render(Box bounds, Line line, Painter painter)
    {
        return Collections.singleton(new RenderContour(GeometryUtils.rectangle(bounds.getX1(), bounds.getY1(), bounds.getX2(), bounds.getY1() + painter.getChiefHeight())));
    }
}
