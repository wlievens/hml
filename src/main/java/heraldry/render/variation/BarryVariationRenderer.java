package heraldry.render.variation;

import heraldry.model.Line;
import heraldry.model.Tincture;
import heraldry.render.Box;
import heraldry.render.Painter;
import heraldry.render.PathStep;
import heraldry.render.RenderContour;
import heraldry.render.RenderShape;
import heraldry.util.GeometryUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BarryVariationRenderer implements VariationRenderer
{
    @Override
    public Collection<RenderShape> render(RenderContour contour, Tincture firstTincture, Tincture secondTincture, Line line, int number, Painter painter)
    {
        Box bounds = contour.getBounds();
        List<RenderShape> list = new ArrayList<>();
        double step = number == 0 ? painter.getGridPatternSize() : bounds.getHeight() / number;
        boolean alternate = true;
        for (double y1 = bounds.getY1(); y1 < bounds.getY2(); y1 += step)
        {
            List<PathStep> rectangle = GeometryUtils.rectangle(bounds.getX1(), y1, bounds.getX2(), y1 + step);
            list.addAll(contour.clip(new RenderShape(rectangle, painter.getColor(alternate ? firstTincture : secondTincture), null)));
            alternate = !alternate;
        }
        return list;
    }
}
