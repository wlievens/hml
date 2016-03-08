package heraldry.model;

import com.kitfox.svg.SVGDiagram;
import heraldry.render.Color;
import heraldry.render.Paint;
import heraldry.render.Painter;
import heraldry.render.RenderContour;
import heraldry.render.RenderShape;
import heraldry.render.Rendering;
import heraldry.util.SvgUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class CoatOfArms
{
    private static final Color COLOR_ARGENT = new Color(0.90, 0.90, 0.90);
    private static final Color COLOR_AZURE = new Color(0.15, 0.15, 0.85);
    private static final Color COLOR_OR = new Color(0.85, 0.85, 0.15);
    private static final Color COLOR_SABLE = new Color(0.15, 0.15, 0.15);
    private static final Color COLOR_GULES = new Color(0.85, 0.15, 0.15);
    private static final Color COLOR_VERT = new Color(0.15, 0.85, 0.15);
    private static final Color COLOR_PURPURE = new Color(0.65, 0.25, 0.65);

    private String title;
    private String blazon;
    private String shape;
    private ChargedBackgroundModel model;

    public String generateBlazon()
    {
        return model.generateBlazon(BlazonContext.create());
    }

    public Rendering render()
    {
        String resource = String.format("/shapes/%s.svg", shape);
        try
        {
            SVGDiagram diagram = SvgUtils.loadSvg(resource);
            RenderContour contour = new RenderContour(SvgUtils.convertSvgElementToPath((com.kitfox.svg.Path)diagram.getElement("contour")));
            Painter painter = new Painter()
            {
                @Override
                public double getOrdinaryThickness()
                {
                    return 0.15 * contour.getBounds().getWidth();
                }

                @Override
                public Paint getColor(Tincture tincture)
                {
                    switch (tincture)
                    {
                        case ARGENT:
                            return COLOR_ARGENT;
                        case OR:
                            return COLOR_OR;
                        case SABLE:
                            return COLOR_SABLE;
                        case AZURE:
                            return COLOR_AZURE;
                        case GULES:
                            return COLOR_GULES;
                        case VERT:
                            return COLOR_VERT;
                        case PURPURE:
                            return COLOR_PURPURE;
                        default:
                            throw new IllegalStateException("Unmapped tincture " + tincture);
                    }
                }

                @Override
                public Color getOrdinaryBorderColor()
                {
                    return new Color(0, 0, 0);
                }

                @Override
                public Color getOuterBorderColor()
                {
                    return new Color(0, 0, 0);
                }

                @Override
                public double getChequyPatternSize()
                {
                    return 0.10 * contour.getBounds().getWidth();
                }

                @Override
                public double getFrettyPatternSize()
                {
                    return 0.25 * contour.getBounds().getWidth();
                }

                @Override
                public double getFretMargin()
                {
                    return 0.005 * Math.min(contour.getBounds().getWidth(), contour.getBounds().getHeight());
                }

                @Override
                public double getFretSizeStep()
                {
                    return 0.05 * Math.min(contour.getBounds().getWidth(), contour.getBounds().getHeight());
                }

                @Override
                public double getLinePeriod()
                {
                    return 0.15 * Math.min(contour.getBounds().getWidth(), contour.getBounds().getHeight());
                }
            };
            List<RenderShape> paths = new ArrayList<>();
            paths.addAll(model.render(contour, painter));
            paths.add(new RenderShape(contour.getSteps(), null, painter.getOuterBorderColor()));
            return new Rendering(contour, paths);
        }
        catch (URISyntaxException e)
        {
            throw new IllegalStateException(e);
        }
    }
}
