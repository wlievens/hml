package heraldry.render.division;

import heraldry.model.Line;
import heraldry.render.Painter;
import heraldry.render.RenderContour;
import heraldry.render.path.Path;

import java.util.List;

public interface DivisionRenderer
{
    List<RenderContour> render(RenderContour contour, Line line, Painter painter);

    Path getSpine(RenderContour contour);
}
