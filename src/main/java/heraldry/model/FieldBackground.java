package heraldry.model;

import com.kitfox.svg.SVGDiagram;
import heraldry.render.Box;
import heraldry.render.Painter;
import heraldry.render.PathStep;
import heraldry.render.RenderContour;
import heraldry.render.RenderShape;
import heraldry.util.GeometryUtils;
import heraldry.util.SvgUtils;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class FieldBackground extends Background
{
    @NonNull
    private final Tincture tincture;

    @Override
    public String generateBlazon(BlazonContext context)
    {
        if (context.getTincture() == tincture)
        {
            return "of the field";
        }
        return tincture.getLabel();
    }

    @Override
    public Collection<RenderShape> render(RenderContour contour, Painter painter)
    {
        if (tincture.isFur())
        {
            SVGDiagram diagram = SvgUtils.loadSvg(String.format("/furs/%s.svg", tincture.name().toLowerCase()));
            float diagramWidth = diagram.getWidth();
            float diagramHeight = diagram.getHeight();
            List<RenderShape> list = new ArrayList<>();
            AffineTransform transform = new AffineTransform();
            Box bounds = contour.getBounds();
            double x1 = bounds.getX1();
            double y1 = bounds.getY1();
            double x2 = bounds.getX2();
            double y2 = bounds.getY2();
            int columns = 7;
            double scale = bounds.getWidth() / (2 * columns * diagramWidth);
            transform.scale(scale, scale);
            list.add(new RenderShape(contour.getSteps(), painter.getColor(Tincture.ARGENT), null));
            double stepX = bounds.getWidth() / columns;
            double stepY = stepX * 1.25;
            int row = 0;
            for (double y = y1; y <= y2 + stepY; y += stepY)
            {
                for (double x = x1; x <= x2 + stepX; x += stepX)
                {
                    double ox = x + (row % 2 - 0.5) * stepX / 2;
                    double oy = y - stepY / 2;
                    for (List<PathStep> c1 : SvgUtils.collect(diagram, transform))
                    {
                        for (List<PathStep> c2 : GeometryUtils.clip(c1, contour))
                        {
                            list.addAll(contour.clip(new RenderShape(c2.stream().map(s -> s.offset(ox, oy)).collect(Collectors.toList()), painter.getColor(Tincture.SABLE), null)));
                        }
                    }
                }
                ++row;
            }
            return list;
        }
        return Collections.singleton(new RenderShape(contour.getSteps(), painter.getColor(tincture), null));
    }
}
