package heraldry.render.division;

import heraldry.render.Painter;
import heraldry.render.RenderContour;

import java.util.List;

public interface DivisionRenderer
{
    List<RenderContour> render(RenderContour contour, Painter painter);
}
