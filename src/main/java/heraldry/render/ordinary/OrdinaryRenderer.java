package heraldry.render.ordinary;

import heraldry.model.Line;
import heraldry.render.Painter;
import heraldry.render.RenderContour;

public interface OrdinaryRenderer
{
    RenderContour render(RenderContour contour, Line line, Painter painter);
}
