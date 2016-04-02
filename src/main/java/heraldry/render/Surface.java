package heraldry.render;

import heraldry.render.paint.Color;
import heraldry.render.paint.Paint;
import heraldry.render.path.Path;
import heraldry.util.GeometryUtils;
import lombok.Getter;
import lombok.ToString;

import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
@ToString
public class Surface
{
    private final List<Path> positives;
    private final List<Path> negatives;

    public Surface(List<Path> positives, List<Path> negatives)
    {
        this.positives = positives;
        this.negatives = negatives;
        for (Path path : positives)
        {
            if (!path.isClosed())
            {
                throw new IllegalArgumentException("surface must consist of closed paths");
            }
        }
        for (Path path : negatives)
        {
            if (!path.isClosed())
            {
                throw new IllegalArgumentException("surface must consist of closed paths");
            }
        }
    }

    public Surface(List<Path> paths)
    {
        this(paths, Collections.emptyList());
    }

    public Surface(Path... paths)
    {
        this(Arrays.asList(paths));
    }

    public Box getBounds()
    {
        Rectangle2D bounds = createArea().getBounds2D();
        return new Box(bounds.getX(), bounds.getY(), bounds.getMaxX(), bounds.getMaxY());
    }

    public Area createArea()
    {
        if (positives.size() == 1 && negatives.isEmpty())
        {
            return new Area(GeometryUtils.convertPathToArea(positives.get(0)));
        }
        Area area = new Area();
        positives.stream().map(GeometryUtils::convertPathToArea).forEach(area::add);
        negatives.stream().map(GeometryUtils::convertPathToArea).forEach(area::subtract);
        return area;
    }

    public boolean isRectangle()
    {
        return isSingular() && positives.get(0).isRectangle();
    }

    public RenderShape render(Paint fillPaint, Color borderColor, String label)
    {
        return RenderShape.create(this, fillPaint, borderColor, label);
    }

    public boolean isSingular()
    {
        return negatives.isEmpty() && positives.size() == 1;
    }

    public Surface normalize()
    {
        return GeometryUtils.convertPathIteratorToSurface(createArea().getPathIterator(null));
    }
}
