package heraldry.render;

import java.util.Collection;

public interface Renderable
{
    Collection<RenderShape> render(RenderContour contour, Painter painter);
}
