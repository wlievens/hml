package heraldry.render;

import heraldry.render.paint.Color;
import heraldry.render.paint.Paint;
import heraldry.render.path.LinePathStep;
import heraldry.render.path.Path;
import heraldry.render.path.PathStep;
import heraldry.util.GeometryUtils;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Wither;

import java.awt.geom.Area;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
@ToString
@RequiredArgsConstructor
public final class RenderContour
{
    @NonNull
    private final Surface surface;
    
    @Wither
    private final Path spine;
    
    public RenderContour(@NonNull Path path)
    {
        this(new Surface(path));
    }

    public RenderContour(@NonNull Surface surface)
    {
        this(surface, null);
    }

    public RenderShape render(Paint fillPaint, Color border, String label)
    {
        return RenderShape.create(surface, fillPaint, border, label);
    }
    
    public Area createArea()
    {
        return surface.createArea();
    }

    public Point getFessPoint()
    {
        return getBounds().getFessPoint();
    }
    
    public Box getBounds()
    {
        return surface.getBounds();
    }
    
    public List<RenderShape> clipShapes(Collection<RenderShape> shapes)
    {
        return shapes.stream()
                .map(this::clip)
                .collect(Collectors.toList());
    }
    
    public List<RenderContour> clipContours(@NonNull Collection<RenderContour> contours)
    {
        return contours.stream()
                .map(this::clip)
                .collect(Collectors.toList());
    }
    
    public List<Path> clip(@NonNull Path path)
    {
        Area contourArea = createArea();
        if (path.isClosed())
        {
            Area area = GeometryUtils.convertPathToArea(path);
            area.intersect(contourArea);
            Surface surface = GeometryUtils.convertPathIteratorToSurface(area.getPathIterator(null), true);
            if (!surface.getNegatives().isEmpty())
            {
                throw new IllegalStateException();
            }
            return surface.getPositives();
        }
        int samples = 100;
        List<Point> points = IntStream.range(0, samples)
                .mapToDouble(n -> n / (double)samples)
                .mapToObj(path::sample)
                .filter(sample -> contourArea.contains(sample.getX(), sample.getY()))
                .collect(Collectors.toList());
        List<PathStep> steps = new ArrayList<>();
        for (int n = 0; n < points.size() - 1; ++n)
        {
            steps.add(new LinePathStep(points.get(n), points.get(n + 1)));
        }
        return Collections.singletonList(new Path(steps, false));
    }
    
    public RenderShape clip(@NonNull RenderShape shape)
    {
        Paint fillPaint = shape.getFillPaint();
        Color borderColor = shape.getBorderColor();
        String label = "clipped " + shape.getLabel();
        Surface surface = clip(shape.getSurface());
        return surface.render(fillPaint, borderColor, label);
    }
    
    public RenderContour clip(@NonNull RenderContour contour)
    {
        return new RenderContour(clip(contour.getSurface()), null);
    }

    public Surface clip(Surface surface)
    {
        Area negative = surface.createArea();
        Area area = createArea();
        area.intersect(negative);
        PathIterator iterator = area.getPathIterator(null);
        return GeometryUtils.convertPathIteratorToSurface(iterator, true);
    }
    
    public boolean isRectangle()
    {
        return surface.isRectangle();
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
