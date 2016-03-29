package heraldry.render;

import heraldry.render.path.AbstractPath;
import heraldry.render.path.Path;
import heraldry.render.path.PathString;
import heraldry.util.GeometryUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Getter
@ToString
@RequiredArgsConstructor
public final class RenderContour
{
    private final Path path;

    private final PathString spine;

    public RenderContour(Path path)
    {
        this(path, null);
    }

    public Point getFessPoint()
    {
        return getBounds().getFessPoint();
    }

    public Box getBounds()
    {
        return path.getBounds();
    }

    public List<RenderShape> clipShapes(Collection<RenderShape> shapes)
    {
        return shapes.stream()
                .map(this::clip)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public List<RenderContour> clipContours(Collection<RenderContour> contours)
    {
        return contours.stream()
                .map(this::clip)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public <T extends AbstractPath> List<T> clip(T path)
    {
        return GeometryUtils.clip(path, this);
    }

    public List<RenderShape> clip(RenderShape shape)
    {
        return GeometryUtils.clip(shape.getPath(), this).stream()
                .map(steps -> new RenderShape(steps, shape.getFillPaint(), shape.getBorderColor(), "clipped " + shape.getLabel()))
                .collect(toList());
    }

    public List<RenderContour> clip(RenderContour contour)
    {
        return GeometryUtils.clip(contour.getPath(), this).stream()
                .map(RenderContour::new)
                .collect(toList());
    }

    public boolean isRectangle()
    {
        return path.isRectangle();
    }
}
