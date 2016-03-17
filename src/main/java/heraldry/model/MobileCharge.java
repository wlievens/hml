package heraldry.model;

import com.kitfox.svg.SVGDiagram;
import heraldry.render.Box;
import heraldry.render.Color;
import heraldry.render.Painter;
import heraldry.render.PathStep;
import heraldry.render.Point;
import heraldry.render.RenderContour;
import heraldry.render.RenderShape;
import heraldry.util.GeometryUtils;
import heraldry.util.StringUtils;
import heraldry.util.SvgUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class MobileCharge extends Charge
{
    private static final boolean DEBUG = false;

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
        // TODO consider positioning the mobile in the larger possible inscribed rectangle?
        Box bounds = contour.getBounds();
        double margin = 0.15;
        double x1 = bounds.getX1() + bounds.getWidth() * margin;
        double y1 = bounds.getY1() + bounds.getHeight() * margin;
        double x2 = bounds.getX2() - bounds.getWidth() * margin;
        double y2 = bounds.getY2() - bounds.getHeight() * margin;
        Point center = bounds.getFessPoint();
        SVGDiagram diagram = SvgUtils.loadSvg(String.format("/mobiles/%s.svg", this.figure));
        float diagramWidth = diagram.getWidth();
        float diagramHeight = diagram.getHeight();
        AffineTransform transform = new AffineTransform();
        double scale = Math.min((x2 - x1) / diagramWidth, (y2 - y1) / diagramHeight);
        transform.translate(center.getX() - 0.5 * scale * diagramWidth, center.getY() - 0.5 * scale * diagramHeight);
        transform.scale(scale, scale);
        List<RenderShape> list = new ArrayList<>();
        for (List<PathStep> c1 : SvgUtils.collect(diagram, transform))
        {
            for (List<PathStep> c2 : GeometryUtils.clip(c1, contour))
            {
                list.addAll(background.render(new RenderContour(c2), painter));
            }
        }
        if (DEBUG)
        {
            list.add(new RenderShape(GeometryUtils.rectangle(x1, y1, x2, y2), null, new Color(0, 1, 1)));
        }
        return list;
    }
}
