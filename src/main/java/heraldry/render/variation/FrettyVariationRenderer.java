package heraldry.render.variation;

import heraldry.model.Line;
import heraldry.model.Tincture;
import heraldry.render.Box;
import heraldry.render.Painter;
import heraldry.render.RenderContour;
import heraldry.render.RenderShape;
import heraldry.util.GeometryUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FrettyVariationRenderer implements VariationRenderer
{
    @Override
    public Collection<RenderShape> render(RenderContour contour, Tincture firstTincture, Tincture secondTincture, Line line, Painter painter)
    {
        Box bounds = contour.getBounds();
        List<RenderShape> list = new ArrayList<>();
        list.add(new RenderShape(contour.getSteps(), painter.getColor(firstTincture), null));
        double spacing = painter.getFrettyPatternSize();
        double step = painter.getFretSizeStep();
        double margin = painter.getFretMargin();

        for (double x = bounds.getX1(); x < bounds.getX2() + spacing; x += spacing)
        {
            for (double y = bounds.getY1(); y < bounds.getY2() + spacing; y += spacing)
            {
                list.add(new RenderShape(GeometryUtils.polygon(
                        x + step + margin, y + margin,
                        x + spacing - margin, y + spacing - step - margin,
                        x + spacing - step - margin, y + spacing - margin,
                        x + margin, y + step + margin
                ), painter.getColor(secondTincture), null));

                list.add(new RenderShape(GeometryUtils.polygon(
                        x - spacing / 2 + margin, y + spacing / 2 - step - margin,
                        x - spacing / 2 + step + margin, y + spacing / 2 - margin,
                        x + spacing / 2 - margin, y - spacing / 2 + step + margin,
                        x + spacing / 2 - step - margin, y - spacing / 2 + margin
                ), painter.getColor(secondTincture), null));
            }
        }

        return contour.clipShapes(list);
    }

}
