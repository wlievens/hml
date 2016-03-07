package heraldry.model;

import heraldry.render.Renderable;

public abstract class Background implements Renderable
{
    public abstract String generateBlazon(BlazonContext context);
}
