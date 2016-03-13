package heraldry.render.variation;

import heraldry.model.Line;
import heraldry.model.Tincture;
import heraldry.render.Painter;
import heraldry.render.RenderContour;
import heraldry.render.RenderShape;

import java.util.Collection;

public interface VariationRenderer
{
    Collection<RenderShape> render(RenderContour contour, Tincture firstTincture, Tincture secondTincture, Line line, int number, Painter painter);
}
