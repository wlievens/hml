package heraldry.render.variation;

import heraldry.model.Line;
import heraldry.model.Tincture;
import heraldry.render.Box;
import heraldry.render.Painter;
import heraldry.render.Point;
import heraldry.render.RenderContour;
import heraldry.render.RenderShape;
import heraldry.util.GeometryUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GyronnyVariationRenderer implements VariationRenderer
{
    @Override
    public Collection<RenderShape> render(RenderContour contour, Tincture firstTincture, Tincture secondTincture, Line line, int number, Painter painter)
    {
        Box bounds = contour.getBounds();
        Point center = bounds.getFessPoint();
        double midX = center.getX();
        double midY = center.getY();
        double x1 = bounds.getX1();
        double y1 = bounds.getY1();
        double x2 = bounds.getX2();
        double y2 = midY * 2 - y1;
        List<RenderShape> list = new ArrayList<>();
        list.add(contour.clip(contour.render(painter.getPaint(secondTincture), null, getClass().getSimpleName() + " background")));
        list.add(contour.clip(GeometryUtils.polygon(midX, midY, x1, y1, midX, y1).render(painter.getPaint(firstTincture), null, "gyron 1")));
        list.add(contour.clip(GeometryUtils.polygon(midX, midY, x2, y1, x2, midY).render(painter.getPaint(firstTincture), null, "gyron 2")));
        list.add(contour.clip(GeometryUtils.polygon(midX, midY, x2, y2, midX, y2).render(painter.getPaint(firstTincture), null, "gyron 3")));
        list.add(contour.clip(GeometryUtils.polygon(midX, midY, x1, y2, x1, midY).render(painter.getPaint(firstTincture), null, "gyron 4 top part")));
        list.add(contour.clip(GeometryUtils.rectangle(midX, y2, x2, bounds.getY2()).render(painter.getPaint(firstTincture), null, "gyron 4 bottom part")));
        return list;
    }
}
