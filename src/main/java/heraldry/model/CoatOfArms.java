package heraldry.model;

import com.kitfox.svg.SVGDiagram;
import heraldry.render.Box;
import heraldry.render.Painter;
import heraldry.render.RenderContour;
import heraldry.render.RenderShape;
import heraldry.render.Rendering;
import heraldry.render.paint.Color;
import heraldry.render.paint.Paint;
import heraldry.render.paint.Pattern;
import heraldry.render.paint.SpecialPaint;
import heraldry.render.path.PathStep;
import heraldry.util.GeometryUtils;
import heraldry.util.SvgUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Getter
@Setter
@ToString
public class CoatOfArms
{
    private static final Color COLOR_ARGENT = new Color(0.90, 0.90, 0.90);
    private static final Color COLOR_AZURE = new Color(0.15, 0.25, 0.80);
    private static final Color COLOR_GULES = new Color(0.70, 0.15, 0.10);
    private static final Color COLOR_OR = new Color(0.85, 0.85, 0.15);
    private static final Color COLOR_PURPURE = new Color(0.50, 0.25, 0.50);
    private static final Color COLOR_SABLE = new Color(0.15, 0.15, 0.15);
    private static final Color COLOR_VERT = new Color(0.20, 0.60, 0.20);

    private static final Pattern PATTERN_ERMINE = new Pattern("ermine", COLOR_ARGENT, COLOR_SABLE);
    private static final Pattern PATTERN_ERMINES = new Pattern("ermine", COLOR_SABLE, COLOR_ARGENT);
    private static final Pattern PATTERN_ERMINOIS = new Pattern("ermine", COLOR_SABLE, COLOR_OR);
    private static final Pattern PATTERN_PEAN = new Pattern("ermine", COLOR_OR, COLOR_SABLE);

    private static final SpecialPaint PAINT_COUNTERCHANGED = new SpecialPaint();

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
        RenderContour shieldContour = new RenderContour(SvgUtils.convertSvgElementToPath((com.kitfox.svg.Path)shieldDiagram.getElement("contour")));
        Painter painter = new Painter()
        {
            @Override
            public double getOrdinaryThickness()
            {
                return 0.15 * shieldContour.getBounds().getWidth();
            }

            @Override
            public Paint getPaint(Tincture tincture)
            {
                switch (tincture)
                {
                    case ARGENT:
                        return COLOR_ARGENT;
                    case AZURE:
                        return COLOR_AZURE;
                    case ERMINE:
                        return PATTERN_ERMINE;
                    case ERMINES:
                        return PATTERN_ERMINES;
                    case ERMINOIS:
                        return PATTERN_ERMINOIS;
                    case GULES:
                        return COLOR_GULES;
                    case OR:
                        return COLOR_OR;
                    case PEAN:
                        return PATTERN_PEAN;
                    case PURPURE:
                        return COLOR_PURPURE;
                    case SABLE:
                        return COLOR_SABLE;
                    case VERT:
                        return COLOR_VERT;
                    default:
                        throw new IllegalStateException("Unmapped tincture " + tincture);
                }
            }

            @Override
            public Paint getCounterchangedPaint()
            {
                return PAINT_COUNTERCHANGED;
            }

            @Override
            public Color getMobileBorderColor()
            {
                return COLOR_SABLE;
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
                return shieldContour.getBounds().getHeight() / 8;
            }

            @Override
            public double getFrettyPatternSize()
            {
                return 0.25 * shieldContour.getBounds().getWidth();
            }

            @Override
            public double getFretMargin()
            {
                return 0.005 * Math.min(shieldContour.getBounds().getWidth(), shieldContour.getBounds().getHeight());
            }

            @Override
            public double getFretSizeStep()
            {
                return 0.05 * Math.min(shieldContour.getBounds().getWidth(), shieldContour.getBounds().getHeight());
            }

            @Override
            public double getLinePeriodFactor()
            {
                return 1.0 / 9.0;
            }

            @Override
            public double getChiefHeight()
            {
                return shieldContour.getBounds().getHeight() / 4;
            }
        };
        List<RenderShape> paths = new ArrayList<>();
        paths.addAll(model.render(shieldContour, painter));
        paths.add(new RenderShape(shieldContour.getSteps(), null, painter.getOuterBorderColor()));

        // Process counterchanged paint
        Map<Paint, Area> paintedAreas = new HashMap<>();
        for (int n = 0; n < paths.size(); ++n)
        {
            RenderShape path = paths.get(n);
            Paint paint = path.getFillPaint();
            if (paint == null)
            {
                continue;
            }
            if (paint == PAINT_COUNTERCHANGED)
            {
                List<Paint> colors = new ArrayList<>(paintedAreas.keySet());
                if (colors.size() != 2)
                {
                    throw new IllegalStateException("Cannot counterchange with " + colors.size() + " color(s)!");
                }
                paths.remove(n);
                for (int c = 0; c < colors.size(); ++c)
                {
                    Paint color = colors.get(c);
                    Paint counter = colors.get(1 - c);
                    Area shapeArea = GeometryUtils.convertShapeToArea(path);
                    shapeArea.intersect(paintedAreas.get(color));
                    List<RenderContour> intersectionContours = GeometryUtils.convertAreaToContours(shapeArea);
                    for (int i = 0; i < intersectionContours.size(); ++i)
                    {
                        paths.add(n + i, new RenderShape(intersectionContours.get(i).getSteps(), counter, null));
                    }
                }
                n--;
                continue;
            }
            Area area = GeometryUtils.convertShapeToArea(path);
            Area existing = paintedAreas.get(paint);
            if (existing == null)
            {
                paintedAreas.put(paint, area);
            }
            else
            {
                existing.add(area);
            }
        }

        // Process all patterned shapes
        paths = paths.stream()
                .map(shape -> {
                    if (shape.getFillPaint() instanceof Pattern)
                    {
                        return processPatternPaint(shieldContour, shape);
                    }
                    return Collections.singleton(shape);
                })
                .flatMap(s -> s.stream())
                .collect(toList());

        // Return as rendering
        return new Rendering(shieldContour, paths);
    }

    private Collection<RenderShape> processPatternPaint(RenderContour shieldContour, RenderShape shape)
    {
        Pattern pattern = (Pattern)shape.getFillPaint();
        SVGDiagram patternDiagram = SvgUtils.loadSvg(String.format("/furs/%s.svg", pattern.getFigure()));
        float diagramWidth = patternDiagram.getWidth();
        List<RenderShape> list = new ArrayList<>();
        AffineTransform transform = new AffineTransform();
        RenderContour shapeContour = new RenderContour(shape.getSteps());
        Box bounds = shieldContour.getBounds(); // Use the shield contour for the bounds!
        double x1 = bounds.getX1();
        double y1 = bounds.getY1();
        double x2 = bounds.getX2();
        double y2 = bounds.getY2();
        int columns = 9;
        double scale = bounds.getWidth() / (2 * columns * diagramWidth);
        transform.scale(scale, scale);
        list.add(new RenderShape(shapeContour.getSteps(), pattern.getBackground(), null));
        double stepX = bounds.getWidth() / columns;
        double stepY = stepX * 1.25;
        int row = 0;
        List<List<PathStep>> patternPaths = SvgUtils.collect(patternDiagram, transform);
        for (double y = y1; y <= y2 + stepY; y += stepY)
        {
            for (double x = x1; x <= x2 + stepX; x += stepX)
            {
                double patternX = x + (row % 2 - 0.5) * stepX / 2;
                double patternY = y - stepY / 2;
                List<List<PathStep>> transformedPaths = patternPaths.stream()
                        .map(steps -> steps.stream().map(step -> step.offset(patternX, patternY)).collect(toList()))
                        .collect(Collectors.toList());
                for (List<PathStep> path : transformedPaths)
                {
                    for (List<PathStep> clipped : GeometryUtils.clip(path, shapeContour))
                    {
                        list.addAll(shapeContour.clip(new RenderShape(clipped, pattern.getForeground(), null)));
                    }
                }
            }
            ++row;
        }
        return list;
    }
}
