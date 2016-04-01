package heraldry.render;

import heraldry.render.paint.Color;
import heraldry.render.paint.Paint;
import heraldry.render.path.Path;
import heraldry.util.GeometryUtils;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Wither;

import java.awt.geom.Area;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Getter
@ToString
@RequiredArgsConstructor
public final class RenderContour
{
    @NonNull
    private final Path path;
    
    @Wither
    private final Path spine;
    
    public RenderContour(@NonNull Path path)
    {
        this(path, null);
    }
    
    public RenderShape render(Paint fillPaint, Color border, String label)
    {
        return RenderShape.create(path, fillPaint, border, label);
    }
    
    public Area createArea()
    {
        return GeometryUtils.convertPathToArea(path);
    }
    
    public Path getPath()
    {
        return this.path;
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
    
    public List<RenderContour> clipContours(@NonNull Collection<RenderContour> contours)
    {
        return contours.stream()
                .map(this::clip)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
    
    public List<Path> clip(@NonNull Path path)
    {
        return GeometryUtils.clip(path, this);
    }
    
    public List<RenderShape> clip(@NonNull RenderShape shape)
    {
        return GeometryUtils.clip(shape.getPath(), this).stream()
                .map(path -> path.render(shape.getFillPaint(), shape.getBorderColor(), "clipped " + shape.getLabel()))
                .collect(toList());
    }
    
    public List<RenderContour> clip(@NonNull RenderContour contour)
    {
        return GeometryUtils.clip(contour.getPath(), this).stream()
                .map(RenderContour::new)
                .collect(toList());
    }
    
    public boolean isRectangle()
    {
        return path.isRectangle();
    }
    
    public Box fitBox(@NonNull Point center)
    {
        double centerX = center.getX();
        double centerY = center.getY();
        Area area = createArea();
        Box bounds = getBounds();
        int steps = 50;
        double largestArea = 0;
        Box largestRectangle = null;
        for (int wstep = steps - 1; wstep >= 0; wstep--)
        {
            for (int hstep = steps - 1; hstep >= 0; hstep--)
            {
                double width = bounds.getWidth() * (wstep + 1) / steps;
                double height = bounds.getHeight() * (hstep + 1) / steps;
                double x1 = centerX - width / 2;
                double y1 = centerY - height / 2;
                double x2 = centerX + width / 2;
                double y2 = centerY + height / 2;
                if (!bounds.contains(x1, y1, x2, y2))
                {
                    continue;
                }
                double surface = width * height;
                if (surface < largestArea)
                {
                    continue;
                }
                if (area.contains(x1, y1, width, height))
                {
                    largestArea = surface;
                    largestRectangle = new Box(x1, y1, x2, y2);
                }
            }
        }
        return largestRectangle;
    }
}
