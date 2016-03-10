package heraldry.model;

import com.kitfox.svg.SVGDiagram;
import heraldry.render.*;
import heraldry.util.GeometryUtils;
import heraldry.util.StringUtils;
import heraldry.util.SvgUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.awt.geom.AffineTransform;
import java.net.URISyntaxException;
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
        Box bounds = contour.getBounds();
        double margin = 0.15;
        double x1 = bounds.getX1() + bounds.getWidth() * margin;
        double y1 = bounds.getY1() + bounds.getHeight() * margin;
        double x2 = bounds.getX2() - bounds.getWidth() * margin;
        double y2 = bounds.getY2() - bounds.getHeight() * margin;
        RenderContour child = new RenderContour(GeometryUtils.rectangle(x1, y1, x2, y2));
        String resource = String.format("/mobiles/%s.svg", this.figure);
        try
        {
            SVGDiagram diagram = SvgUtils.loadSvg(resource);
            float diagramWidth = diagram.getWidth();
            float diagramHeight = diagram.getHeight();
            AffineTransform transform = new AffineTransform();
            double scale = Math.min((x2 - x1) / diagramWidth, (y2 - y1) / diagramHeight);
            transform.translate(x1, y1);
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
        catch (URISyntaxException e)
        {
            throw new IllegalStateException(e);
        }
    }
}
