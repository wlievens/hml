package heraldry.model;

import heraldry.render.RenderContour;
import heraldry.render.Renderable;
import heraldry.render.path.Path;

public abstract class Background implements Renderable
{
    public abstract String generateBlazon(BlazonContext context);

    public abstract Path getSpine(RenderContour contour);
}
