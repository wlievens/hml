package heraldry.model;

import com.kitfox.svg.SVGDiagram;
import heraldry.render.Box;
import heraldry.render.Color;
import heraldry.render.Paint;
import heraldry.render.Painter;
import heraldry.render.PathStep;
import heraldry.render.Pattern;
import heraldry.render.RenderContour;
import heraldry.render.RenderShape;
import heraldry.render.Rendering;
import heraldry.util.GeometryUtils;
import heraldry.util.SvgUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Getter
@Setter
@ToString
public class CoatOfArms
{
    private static final Color COLOR_ARGENT = new Color(0.90, 0.90, 0.90);
    private static final Color COLOR_AZURE = new Color(0.05, 0.15, 0.85);
    private static final Color COLOR_OR = new Color(0.85, 0.85, 0.15);
    private static final Color COLOR_SABLE = new Color(0.15, 0.15, 0.15);
    private static final Color COLOR_GULES = new Color(0.85, 0.15, 0.15);
    private static final Color COLOR_VERT = new Color(0.15, 0.85, 0.15);
    private static final Color COLOR_PURPURE = new Color(0.65, 0.25, 0.65);

    private static final Pattern PATTERN_ERMINE = new Pattern(Tincture.ERMINE);

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
        SVGDiagram shieldDiagram = SvgUtils.loadSvg(String.format("/shapes/%s.svg", shape));
        RenderContour contour = new RenderContour(SvgUtils.convertSvgElementToPath((com.kitfox.svg.Path)shieldDiagram.getElement("contour")));
        Painter painter = new Painter()
        {
            @Override
            public double getOrdinaryThickness()
            {
                return 0.15 * contour.getBounds().getWidth();
            }

            @Override
            public Paint getPaint(Tincture tincture)
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
                    case ERMINE:
                        return PATTERN_ERMINE;
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
            public double getGridPatternSize()
            {
                return contour.getBounds().getHeight() / 8;
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
            public double getLinePeriodFactor()
            {
                return 1.0 / 9.0;
            }

            @Override
            public double getChiefHeight()
            {
                return contour.getBounds().getHeight() / 4;
            }
        };
        List<RenderShape> paths = new ArrayList<>();
        paths.addAll(model.render(contour, painter));
        paths.add(new RenderShape(contour.getSteps(), null, painter.getOuterBorderColor()));
        // Process all patterned shapes
        paths = paths.stream()
                .map(shape -> {
                    if (shape.getFillPaint() instanceof Pattern)
                    {
                        Tincture tincture = ((Pattern)shape.getFillPaint()).getTincture();
                        if (tincture.isFur())
                        {
                            SVGDiagram furDiagram = SvgUtils.loadSvg(String.format("/furs/%s.svg", tincture.name().toLowerCase()));
                            float diagramWidth = furDiagram.getWidth();
                            List<RenderShape> list = new ArrayList<>();
                            AffineTransform transform = new AffineTransform();
                            RenderContour shapeContour = new RenderContour(shape.getSteps());
                            Box bounds = shapeContour.getBounds();
                            double x1 = bounds.getX1();
                            double y1 = bounds.getY1();
                            double x2 = bounds.getX2();
                            double y2 = bounds.getY2();
                            int columns = 9;
                            double scale = bounds.getWidth() / (2 * columns * diagramWidth);
                            transform.scale(scale, scale);
                            list.add(new RenderShape(shapeContour.getSteps(), painter.getPaint(Tincture.ARGENT), null));
                            double stepX = bounds.getWidth() / columns;
                            double stepY = stepX * 1.25;
                            int row = 0;
                            for (double y = y1; y <= y2 + stepY; y += stepY)
                            {
                                for (double x = x1; x <= x2 + stepX; x += stepX)
                                {
                                    double ox = x + (row % 2 - 0.5) * stepX / 2;
                                    double oy = y - stepY / 2;
                                    for (List<PathStep> c1 : SvgUtils.collect(furDiagram, transform))
                                    {
                                        for (List<PathStep> c2 : GeometryUtils.clip(c1, shapeContour))
                                        {
                                            list.addAll(shapeContour.clip(new RenderShape(c2.stream().map(s -> s.offset(ox, oy)).collect(Collectors.toList()), painter.getPaint(Tincture.SABLE), null)));
                                        }
                                    }
                                }
                                ++row;
                            }
                            return list;
                        }
                        throw new IllegalStateException();
                    }
                    return Collections.singleton(shape);
                })
                .flatMap(s -> s.stream())
                .collect(toList());
        return new Rendering(contour, paths);
    }
}
