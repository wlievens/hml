package heraldry.model;

import heraldry.render.Renderable;

public abstract class Charge implements Renderable
{
    public abstract String generateBlazon(BlazonContext context);
}
