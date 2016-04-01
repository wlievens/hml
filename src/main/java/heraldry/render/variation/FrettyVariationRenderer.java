package heraldry.render.variation;

import heraldry.model.Line;
import heraldry.model.Tincture;
import heraldry.render.Box;
import heraldry.render.Painter;
import heraldry.render.RenderContour;
import heraldry.render.RenderShape;
import heraldry.render.path.Path;
import heraldry.util.GeometryUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FrettyVariationRenderer implements VariationRenderer
{
    @Override
    public Collection<RenderShape> render(RenderContour contour, Tincture firstTincture, Tincture secondTincture, Line line, int number, Painter painter)
    {
        Box bounds = contour.getBounds();
        List<RenderShape> list = new ArrayList<>();
        list.add(contour.render(painter.getPaint(firstTincture), null, getClass().getSimpleName() + " background"));
        double spacing = painter.getFrettyPatternSize();
        double step = painter.getFretThickness() / Math.sqrt(2);
        double margin = painter.getFretMargin();

        for (double x = bounds.getX1(); x < bounds.getX2() + spacing; x += spacing)
        {
            for (double y = bounds.getY1(); y < bounds.getY2() + spacing; y += spacing)
            {
                Path polygon1 = GeometryUtils.polygon(
                        x + step + margin, y + margin,
                        x + spacing - margin, y + spacing - step - margin,
                        x + spacing - step - margin, y + spacing - margin,
                        x + margin, y + step + margin
                );
                list.add(polygon1.render(painter.getPaint(secondTincture), null, "fretty first " + x + ", " + y));

                Path polygon2 = GeometryUtils.polygon(
                        x - spacing / 2 + margin, y + spacing / 2 - step - margin,
                        x - spacing / 2 + step + margin, y + spacing / 2 - margin,
                        x + spacing / 2 - margin, y - spacing / 2 + step + margin,
                        x + spacing / 2 - step - margin, y - spacing / 2 + margin
                );
                list.add(polygon2.render(painter.getPaint(secondTincture), null, "fretty second " + x + ", " + y));
            }
        }

        return contour.clipShapes(list);
    }

}
