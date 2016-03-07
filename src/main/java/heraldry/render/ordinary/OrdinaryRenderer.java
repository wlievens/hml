package heraldry.render.ordinary;

import heraldry.model.Line;
import heraldry.model.Tincture;
import heraldry.render.Box;
import heraldry.render.Painter;
import heraldry.render.RenderShape;

import java.util.Collection;

public interface OrdinaryRenderer
{
    Collection<RenderShape> render(Box bounds, Tincture tincture, Line line, Painter painter);
}
