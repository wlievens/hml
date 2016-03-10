package heraldry.render.ordinary;

import heraldry.model.Line;
import heraldry.render.Box;
import heraldry.render.Painter;
import heraldry.render.RenderContour;

import java.util.Collection;

public interface OrdinaryRenderer
{
    Collection<RenderContour> render(Box bounds, Line line, Painter painter);
}
