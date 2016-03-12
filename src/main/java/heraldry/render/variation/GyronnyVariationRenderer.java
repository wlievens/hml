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
    public Collection<RenderShape> render(RenderContour contour, Tincture firstTincture, Tincture secondTincture, Line line, Painter painter)
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
        list.addAll(contour.clip(new RenderShape(contour.getSteps(), painter.getColor(secondTincture), null)));
        list.addAll(contour.clip(new RenderShape(GeometryUtils.polygon(midX, midY, x1, y1, midX, y1), painter.getColor(firstTincture), null)));
        list.addAll(contour.clip(new RenderShape(GeometryUtils.polygon(midX, midY, x2, y1, x2, midY), painter.getColor(firstTincture), null)));
        list.addAll(contour.clip(new RenderShape(GeometryUtils.polygon(midX, midY, x2, y2, midX, y2), painter.getColor(firstTincture), null)));
        list.addAll(contour.clip(new RenderShape(GeometryUtils.polygon(midX, midY, x1, y2, x1, midY), painter.getColor(firstTincture), null)));
        list.addAll(contour.clip(new RenderShape(GeometryUtils.rectangle(midX, y2, x2, bounds.getY2()), painter.getColor(firstTincture), null)));
        return list;
    }
}