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

public class ChequyVariationRenderer implements VariationRenderer
{
    @Override
    public Collection<RenderShape> render(RenderContour contour, Tincture firstTincture, Tincture secondTincture, Line line, int number, Painter painter)
    {
        Box bounds = contour.getBounds();
        List<RenderShape> list = new ArrayList<>();
        double step = painter.getGridPatternSize();
        boolean rowAlternate = true;
        for (double y1 = bounds.getY1(); y1 < bounds.getY2(); y1 += step)
        {
            boolean alternate = rowAlternate;
            for (double x1 = bounds.getX1(); x1 < bounds.getX2(); x1 += step)
            {
                double x2 = x1 + step;
                double y2 = y1 + step;
                Path rectangle = GeometryUtils.rectangle(x1, y1, x2, y2);
                list.add(contour.clip(rectangle.render(painter.getPaint(alternate ? firstTincture : secondTincture), null, "chequy element " + x1 + ", " + y1)));
                alternate = !alternate;
            }
            rowAlternate = !rowAlternate;
        }
        return list;
    }
}
