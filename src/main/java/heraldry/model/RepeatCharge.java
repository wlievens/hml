package heraldry.model;

import heraldry.render.Box;
import heraldry.render.Painter;
import heraldry.render.Point;
import heraldry.render.RenderContour;
import heraldry.render.RenderShape;
import heraldry.util.GeometryUtils;
import heraldry.util.MathUtils;
import heraldry.util.StringUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.List;
import java.util.Random;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class RepeatCharge extends Charge
{
    private final int number;
    private final Charge charge;

    @Override
    public String generateBlazon(BlazonContext context)
    {
        return StringUtils.getNumeral(number).toLowerCase() + " " + charge.generateBlazon(context.withPlural(number > 1));
    }

    @Override
    public Collection<RenderShape> render(RenderContour contour, Painter painter)
    {
        Box bounds = contour.getBounds();
        if (contour.isRectangle())
        {
            List<RenderShape> list = new ArrayList<>();

            double width = bounds.getWidth();
            double height = bounds.getHeight();
            if (width > height)
            {
                for (double n = 0; n < number; ++n)
                {
                    double x1 = bounds.getX1() + (n + 0) * width / number;
                    double x2 = bounds.getX1() + (n + 1) * width / number;
                    RenderContour child = new RenderContour(GeometryUtils.rectangle(x1, bounds.getY1(), x2, bounds.getY2()));
                    list.addAll(charge.render(child, painter));
                }
            }

            return list;
        }

        Area area = GeometryUtils.convertContourToArea(contour);
        // Use a rasterized Voronoi approach with Lloyd's algorithm for approaching optimal distribution
        // https://en.wikipedia.org/wiki/Voronoi_diagram
        // https://en.wikipedia.org/wiki/Lloyd%27s_algorithm
        int resolution = 100;
        BitSet inclusion = sampleInclusionMatrix(area, bounds, resolution);
        List<Point> points = generatePoints(area, bounds);
        relaxPoints(bounds, inclusion, resolution, points);

        List<RenderShape> list = new ArrayList<>();
        for (int n = 0; n < number; ++n)
        {
            double x1 = points.get(n).getX() - 5;
            double y1 = points.get(n).getY() - 5;
            double x2 = points.get(n).getX() + 5;
            double y2 = points.get(n).getY() + 5;
            RenderContour child = new RenderContour(GeometryUtils.rectangle(x1, y1, x2, y2));
            list.addAll(charge.render(child, painter));
        }
        return list;
    }

    private void relaxPoints(Box bounds, BitSet inclusion, int resolution, List<Point> points)
    {
        while (true)
        {
            double totalChange = 0;
            int[] matrix = new int[resolution * resolution];
            for (int y = 0; y < resolution; ++y)
            {
                for (int x = 0; x < resolution; ++x)
                {
                    int index = x + y * resolution;
                    if (inclusion.get(index))
                    {
                        double cx = bounds.lerpX(x / (double)resolution);
                        double cy = bounds.lerpY(y / (double)resolution);
                        int nearest = 0;
                        double nearestDistance = 0;
                        for (int n = 0; n < number; ++n)
                        {
                            Point point = points.get(n);
                            double distance = MathUtils.distance(cx, cy, point.getX(), point.getY());
                            if (n == 0 || distance < nearestDistance)
                            {
                                nearest = n;
                                nearestDistance = distance;
                            }
                        }
                        matrix[index] = nearest;
                    }
                }
            }
            for (int n = 0; n < number; ++n)
            {
                double sumX = 0;
                double sumY = 0;
                int samples = 0;
                for (int y = 0; y < resolution; ++y)
                {
                    for (int x = 0; x < resolution; ++x)
                    {
                        int index = x + y * resolution;
                        if (matrix[index] == n)
                        {
                            double cx = bounds.lerpX(x / (double)resolution);
                            double cy = bounds.lerpY(y / (double)resolution);
                            ++samples;
                            sumX += cx;
                            sumY += cy;
                        }
                    }
                }
                sumX /= samples;
                sumY /= samples;
                Point point = points.get(n);
                totalChange += MathUtils.distance(sumX, sumY, point.getX(), point.getY());
                points.set(n, new Point(sumX, sumY));
            }
            // Arbitrary threshold
            if (totalChange < 0.5)
            {
                break;
            }
        }
    }

    private List<Point> generatePoints(Area area, Box bounds)
    {
        List<Point> points = new ArrayList<>();
        Random random = new Random(1);
        for (int n = 0; n < number; ++n)
        {
            double x;
            double y;
            while (true)
            {
                x = bounds.lerpX(random.nextDouble());
                y = bounds.lerpY(random.nextDouble());
                if (area.contains(x, y))
                {
                    break;
                }
            }
            points.add(new Point(x, y));
        }
        return points;
    }

    private BitSet sampleInclusionMatrix(Area area, Box bounds, int resolution)
    {
        BitSet matrixActive = new BitSet();
        for (int y = 0; y < resolution; ++y)
        {
            for (int x = 0; x < resolution; ++x)
            {
                int index = x + y * resolution;
                double cx = bounds.lerpX(x / (double)resolution);
                double cy = bounds.lerpY(y / (double)resolution);
                matrixActive.set(index, area.contains(cx, cy));
            }
        }
        return matrixActive;
    }
}
