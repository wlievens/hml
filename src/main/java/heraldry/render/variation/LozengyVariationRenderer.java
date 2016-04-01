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

public class LozengyVariationRenderer implements VariationRenderer
{
    @Override
    public Collection<RenderShape> render(RenderContour contour, Tincture firstTincture, Tincture secondTincture, Line line, int number, Painter painter)
    {
        Box bounds = contour.getBounds();
        List<RenderShape> list = new ArrayList<>();
        double step = painter.getGridPatternSize() * Math.sqrt(2);
        list.addAll(contour.clip(contour.render(painter.getPaint(secondTincture), null, getClass().getSimpleName() + " background")));
        for (double y1 = bounds.getY1(); y1 < bounds.getY2(); y1 += step)
        {
            for (double x1 = bounds.getX1(); x1 < bounds.getX2(); x1 += step)
            {
                double x2 = x1 + step;
                double y2 = y1 + step;
                double mx = (x1 + x2) / 2;
                double my = (y1 + y2) / 2;
                Path path = GeometryUtils.polygon(mx, y1, x2, my, mx, y2, x1, my);
                list.addAll(contour.clip(path.render(painter.getPaint(firstTincture), null, "lozengy " + x1 + "," + x2)));
            }
        }
        return list;
    }
}
