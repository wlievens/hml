package heraldry.render;

import heraldry.render.path.Path;
import heraldry.util.GeometryUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Getter
@ToString
@RequiredArgsConstructor
public final class RenderContour
{
    private final Path path;

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
        List<RenderShape> list = new ArrayList<>();
        for (RenderShape shape : shapes)
        {
            list.addAll(clip(shape));
        }
        return list;
    }

    public List<RenderContour> clipContours(Collection<RenderContour> contours)
    {
        List<RenderContour> list = new ArrayList<>();
        for (RenderContour contour : contours)
        {
            list.addAll(clip(contour));
        }
        return list;
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
