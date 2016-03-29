package heraldry.render.path;

import lombok.Getter;

import java.util.List;

@Getter
public final class Path extends AbstractPath
{
    public Path(PathStep... steps)
    {
        super(steps);
    }

    public Path(List<PathStep> steps)
    {
        super(steps);
    }

    @Override
    public boolean isClosed()
    {
        return true;
    }
}
