package heraldry.render.path;

import lombok.Getter;

import java.util.List;

@Getter
public final class PathString extends AbstractPath
{
    public PathString(PathStep... steps)
    {
        super(steps);
    }

    public PathString(List<PathStep> steps)
    {
        super(steps);
    }

    @Override
    public boolean isClosed()
    {
        return true;
    }
}
