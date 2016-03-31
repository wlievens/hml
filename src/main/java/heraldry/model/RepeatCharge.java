package heraldry.model;

import heraldry.render.Box;
import heraldry.render.Painter;
import heraldry.render.Point;
import heraldry.render.RenderContour;
import heraldry.render.RenderShape;
import heraldry.render.paint.Color;
import heraldry.render.path.Path;
import heraldry.util.GeometryUtils;
import heraldry.util.MathUtils;
import heraldry.util.StringUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class RepeatCharge extends Charge
{
    private static final boolean DEBUG = false;

    private final int number;
    private final Charge charge;

    @Override
    public String generateBlazon(BlazonContext context)
    {
        return StringUtils.getNumeral(number).toLowerCase() + " " + charge.generateBlazon(context.withPlural(number > 1));
    }

    @Override
    public boolean isSquareShapePreferred()
    {
        return number == 1 && charge.isSquareShapePreferred();
    }

    @Override
    public boolean isRepeatSupported()
    {
        return false;
    }

    @Override
    public Collection<RenderShape> render(RenderContour contour, Painter painter)
    {
        Box bounds = contour.getBounds();

        if (!charge.isRepeatSupported())
        {
            return charge.render(contour, painter);
        }

        Path spine = contour.getSpine();
        if (spine != null)
        {
            List<Box> largestBoxes = null;
            double largestArea = 0.0;
            double marginStep = 0.05;
            for (double marginFactor = 0.0; marginFactor <= 1.0; marginFactor += marginStep)
            {
                double mf = marginFactor;
                List<Point> positions = IntStream.range(0, number)
                        .mapToDouble(n -> (n + mf) / (number + mf * 2 - 1))
                        .mapToObj(spine::sample)
                        .collect(toList());

                List<Box> boxes = positions.stream().map(contour::fitBox).collect(toList());
                if (boxes.stream().anyMatch(box -> box == null))
                {
                    continue;
                }

                double minWidth = boxes.stream().mapToDouble(Box::getWidth).min().getAsDouble();
                double minHeight = boxes.stream().mapToDouble(Box::getHeight).min().getAsDouble();
                boxes = boxes.stream().map(box -> Box.around(box.getCenterX(), box.getCenterY(), minWidth, minHeight)).collect(toList());
                boolean overlap = false;
                loop1:
                for (int index1 = 0; index1 < boxes.size(); index1++)
                {
                    Box box1 = boxes.get(index1);
                    for (int index2 = index1 + 1; index2 < boxes.size(); index2++)
                    {
                        Box box2 = boxes.get(index2);
                        if (box1.intersects(box2))
                        {
                            overlap = true;
                            break loop1;
                        }
                    }
                }
                if (overlap)
                {
                    continue;
                }
                double area = minWidth * minHeight;
                if (area > largestArea)
                {
                    largestArea = area;
                    largestBoxes = boxes;
                }
            }

            List<RenderShape> list = new ArrayList<>();
            for (Box box : largestBoxes)
            {
                RenderContour child = new RenderContour(box.toPath());
                list.addAll(contour.clipShapes(charge.render(child, painter)));
                if (DEBUG)
                {
                    list.add(new RenderShape(child.getPath(), null, new Color(1, 0, 1), null));
                }
            }
            return list;
        }

        if (charge instanceof OrdinaryCharge)
        {
            OrdinaryCharge ordinaryCharge = (OrdinaryCharge)charge;
            if (ordinaryCharge.getOrdinary().isVerticalStacking())
            {
                List<RenderShape> list = new ArrayList<>();
                for (int n = 0; n < number; ++n)
                {
                    double t1 = (n - number / 2.0 + 0.5) / (number);
                    double t2 = t1 + 1.0;
                    RenderContour child = new RenderContour(GeometryUtils.rectangle(bounds.getX1(), bounds.lerpY(t1), bounds.getX2(), bounds.lerpY(t2)));
                    list.addAll(contour.clipShapes(charge.render(child, painter)));
                }
                return list;
            }
        }

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
        int resolution = 500;
        BitSet inclusion = sampleInclusionMatrix(area, bounds, resolution);
        List<Point> points = generatePoints(area, bounds);
        relaxPoints(area, bounds, inclusion, resolution, points);

        List<RenderShape> list = new ArrayList<>();
        for (int n = 0; n < number; ++n)
        {
            // TODO fit a rectangle, then resize all the rectangles to the smallest
            double x1 = points.get(n).getX() - 15;
            double y1 = points.get(n).getY() - 15;
            double x2 = points.get(n).getX() + 15;
            double y2 = points.get(n).getY() + 15;
            RenderContour child = new RenderContour(GeometryUtils.rectangle(x1, y1, x2, y2));
            list.addAll(contour.clipShapes(charge.render(child, painter)));
            if (DEBUG)
            {
                list.add(new RenderShape(child.getPath(), null, new Color(1, 0, 1), "DEBUG repeat reticle"));
            }
        }
        return list;
    }

    private void relaxPoints(Area area, Box bounds, BitSet inclusion, int resolution, List<Point> points)
    {
        int totalArea = inclusion.cardinality();
        double[] weights = new double[points.size()];
        int iteration = 0;
        while (true)
        {
            double totalChange = 0;

            int[] matrix = new int[resolution * resolution];
            Arrays.fill(matrix, -1);
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
                        for (int pointIndex = 0; pointIndex < number; ++pointIndex)
                        {
                            Point point = points.get(pointIndex);
                            double distance = MathUtils.distance(cx, cy, point.getX(), point.getY());
                            if (weights[pointIndex] > 0)
                            {
                                distance = distance * (1 + weights[pointIndex]);
                            }
                            if (pointIndex == 0 || distance < nearestDistance)
                            {
                                nearest = pointIndex;
                                nearestDistance = distance;
                            }
                        }
                        matrix[index] = nearest;
                    }
                }
            }

            {
                BufferedImage image = generateDebugImage(bounds, resolution, points, matrix);
                File file = new File(String.format("temp/iteration%03d.png", iteration));
                file.getParentFile().mkdirs();
                try
                {
                    ImageIO.write(image, "png", file);
                }
                catch (IOException e)
                {
                    throw new IllegalStateException(e);
                }
            }

            for (int pointIndex = 0; pointIndex < number; ++pointIndex)
            {
                double centerX = 0;
                double centerY = 0;
                int samples = 0;
                for (int y = 0; y < resolution; ++y)
                {
                    for (int x = 0; x < resolution; ++x)
                    {
                        int index = x + y * resolution;
                        if (inclusion.get(index) && matrix[index] == pointIndex)
                        {
                            double cx = bounds.lerpX(x / (double)resolution);
                            double cy = bounds.lerpY(y / (double)resolution);
                            ++samples;
                            centerX += cx;
                            centerY += cy;
                        }
                    }
                }
                if (samples == 0)
                {
                    throw new IllegalStateException();
                }
                centerX /= samples;
                centerY /= samples;
                weights[pointIndex] = samples / (double)totalArea;
                Point point = points.get(pointIndex);
                if (!area.contains(point.getX(), point.getY()))
                {
                    // pick a random point inside the marked area
                    List<Integer> locations = new ArrayList<>();
                    for (int i = 0; i < resolution * resolution; ++i)
                    {
                        if (matrix[i] == pointIndex)
                        {
                            locations.add(i);
                        }
                    }
                    if (locations.size() == 0)
                    {
                        throw new IllegalStateException();
                    }
                    int pick = locations.get(new Random(1).nextInt(locations.size()));
                    int pickX = pick % resolution;
                    int pickY = pick / resolution;
                    point = new Point(bounds.lerpX(pickX / (double)resolution), bounds.lerpY(pickY / (double)resolution));
                }
                totalChange += MathUtils.distance(centerX, centerY, point.getX(), point.getY());
                points.set(pointIndex, new Point(centerX, centerY));
            }

            ++iteration;

            // Arbitrary threshold
            if (totalChange < 0.1)
            {
                break;
            }
        }
    }

    private BufferedImage generateDebugImage(Box bounds, int resolution, List<Point> points, int[] matrix)
    {
        BufferedImage image = new BufferedImage(resolution, resolution, BufferedImage.TYPE_INT_RGB);
        List<Integer> colors = new ArrayList<>();
        for (int n = 0; n < number; ++n)
        {
            Random random = new Random(n * 134321651);
            float hue = random.nextFloat();
            colors.add(java.awt.Color.HSBtoRGB(hue, 0.8f, 0.6f));
        }
        for (int y = 0; y < resolution; ++y)
        {
            for (int x = 0; x < resolution; ++x)
            {
                int index = x + y * resolution;
                int value = matrix[index];
                if (value >= 0)
                {
                    image.setRGB(x, y, colors.get(value));
                }
            }
        }

        {
            Graphics2D gfx = (Graphics2D)image.getGraphics();
            gfx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            for (int n = 0; n < points.size(); ++n)
            {
                int r = resolution / 100;
                gfx.setColor(java.awt.Color.ORANGE);
                gfx.fillOval((int)Math.round(bounds.prelX(points.get(n).getX()) * resolution) - r, (int)Math.round(bounds.prelY(points.get(n).getY()) * resolution) - r, r * 2, r * 2);
            }
            gfx.dispose();
        }
        return image;
    }

    private List<Point> generatePoints(Area area, Box bounds)
    {
        List<Point> points = new ArrayList<>();
        Random random = new Random(1);
        for (int n = 0; n < number; ++n)
        {
            points.add(generatePoint(area, bounds, random));
        }
        return points;
    }

    private Point generatePoint(Area area, Box bounds, Random random)
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
        return new Point(x, y);
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
