package heraldry.render.ordinary;

import heraldry.model.Line;
import heraldry.render.Painter;
import heraldry.render.RenderContour;

import java.util.Collection;

public interface OrdinaryRenderer
{
    Collection<RenderContour> render(RenderContour contour, Line line, Painter painter);
}
