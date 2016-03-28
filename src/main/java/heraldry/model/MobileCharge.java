package heraldry.model;

import com.kitfox.svg.SVGDiagram;
import heraldry.render.Box;
import heraldry.render.Painter;
import heraldry.render.Point;
import heraldry.render.RenderContour;
import heraldry.render.RenderShape;
import heraldry.render.paint.Color;
import heraldry.render.path.Path;
import heraldry.render.path.PathStep;
import heraldry.util.GeometryUtils;
import heraldry.util.StringUtils;
import heraldry.util.SvgUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class MobileCharge extends Charge
{
    private static final boolean DEBUG = true;

    /**
     * The symbolic name of the figure.
     */
    private final String figure;

    private final Background background;

    private final List<Charge> charges;

    @Override
    public boolean isRepeatSupported()
    {
        return true;
    }

    @Override
    public String generateBlazon(BlazonContext context)
    {
        String figureLabel = context.isPlural() ? StringUtils.getPlural(this.figure) : this.figure;
        if (background == null)
        {
            return figureLabel + " proper";
        }
        return (context.isPlural() ? "" : "a ") + figureLabel + " " + background.generateBlazon(context);
    }

    @Override
    public boolean isSquareShapePreferred()
    {
        return false;
    }

    @Override
    public Collection<RenderShape> render(RenderContour contour, Painter painter)
    {
        // Place the mobile in the largest inscribed X-centered rectangle
        Box bounds = contour.getBounds();
        Area contourArea = GeometryUtils.convertContourToArea(contour);
        double centerX = bounds.lerpX(0.5);
        int steps = 50;
        double largestArea = 0;
        Box largestRectangle = null;
        for (int ystep = 0; ystep < steps; ++ystep)
        {
            for (int wstep = steps - 1; wstep >= 0; wstep--)
            {
                for (int hstep = steps - 1; hstep >= 0; hstep--)
                {
                    double width = bounds.getWidth() * (wstep + 1) / steps;
                    double height = bounds.getHeight() * (hstep + 1) / steps;
                    double centerY = bounds.lerpY((double)(ystep + 1) / steps);
                    double x1 = centerX - width / 2;
                    double y1 = centerY - height / 2;
                    double x2 = centerX + width / 2;
                    double y2 = centerY + height / 2;
                    if (!bounds.contains(x1, y1, x2, y2))
                    {
                        continue;
                    }
                    double area = width * height;
                    if (area < largestArea)
                    {
                        continue;
                    }
                    if (contourArea.contains(x1, y1, width, height))
                    {
                        if (DEBUG)
                        {
                            System.out.printf("%.2f\t%.2f\t%.2f\t%.2f\t%.2f%n", x1, y1, x2, y2, area);
                        }
                        largestArea = area;
                        largestRectangle = new Box(x1, y1, x2, y2);
                    }
                }
            }
        }
        if (largestRectangle == null)
        {
            return Collections.emptyList();
        }

        double margin = 0.05;
        double x1 = largestRectangle.lerpX(margin);
        double y1 = largestRectangle.lerpY(margin);
        double x2 = largestRectangle.lerpX(1 - margin);
        double y2 = largestRectangle.lerpY(1 - margin);
        Point center = new Point((x1 + x2) / 2, (y1 + y2) / 2);
        SVGDiagram diagram = SvgUtils.loadSvg(String.format("/mobiles/%s.svg", this.figure));
        float diagramWidth = diagram.getWidth();
        float diagramHeight = diagram.getHeight();
        AffineTransform transform = new AffineTransform();
        double scale = Math.min((x2 - x1) / diagramWidth, (y2 - y1) / diagramHeight);
        transform.translate(center.getX() - 0.5 * scale * diagramWidth, center.getY() - 0.5 * scale * diagramHeight);
        transform.scale(scale, scale);
        List<RenderShape> list = new ArrayList<>();
        for (Path c1 : SvgUtils.collect(diagram, transform))
        {
            for (Path c2 : GeometryUtils.clip(c1, contour))
            {
                list.addAll(background.render(new RenderContour(c2), painter).stream()
                    .map(shape -> shape.withBorderColor(painter.getMobileBorderColor()).withLabel(String.format("'%s' %s", figure, shape.getLabel())))
                    .collect(Collectors.toList()));
            }
        }
        if (DEBUG)
        {
            list.add(new RenderShape(GeometryUtils.rectangle(x1, y1, x2, y2), null, new Color(0, 1, 1), String.format("'%s' mobile debug reticle", figure)));
        }
        return list;
    }
}
