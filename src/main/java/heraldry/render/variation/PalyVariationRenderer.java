package heraldry.render.variation;

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

public class PalyVariationRenderer implements VariationRenderer
{
    @Override
    public Collection<RenderShape> render(RenderContour contour, Tincture firstTincture, Tincture secondTincture, Painter painter)
    {
        Box bounds = contour.getBounds();
        List<RenderShape> list = new ArrayList<>();
        double step = painter.getGridPatternSize();
        boolean alternate = true;
        for (double x1 = bounds.getX1(); x1 < bounds.getX2(); x1 += step)
        {
            List<PathStep> rectangle = GeometryUtils.rectangle(x1, bounds.getY1(), x1 + step, bounds.getY2());
            list.addAll(contour.clip(new RenderShape(rectangle, painter.getColor(alternate ? firstTincture : secondTincture), null)));
            alternate = !alternate;
        }
        return list;
    }
}
