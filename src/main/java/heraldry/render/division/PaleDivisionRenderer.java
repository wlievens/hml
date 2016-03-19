package heraldry.render.division;

import heraldry.model.Line;
import heraldry.render.Box;
import heraldry.render.Painter;
import heraldry.render.Point;
import heraldry.render.RenderContour;
import heraldry.util.GeometryUtils;

import java.awt.geom.Area;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PaleDivisionRenderer implements DivisionRenderer
{
    @Override
    public List<RenderContour> render(RenderContour contour, Line line, Painter painter)
    {
        Box bounds = contour.getBounds();
        Point center = bounds.getFessPoint();

        Box[] boxes = new Box[]{
                new Box(bounds.getX1(), bounds.getY1(), center.getX(), bounds.getY2()),
                new Box(center.getX(), bounds.getY1(), bounds.getX2(), bounds.getY2())
        };

        return Arrays.stream(boxes)
                .map(box -> {
                    Area area = GeometryUtils.convertContourToArea(contour);
                    area.intersect(GeometryUtils.convertBoxToArea(box));
                    return area;
                })
                .map(GeometryUtils::convertAreaToContours)
                .map(list -> {
                    if (list.size() != 1)
                    {
                        throw new IllegalStateException();
                    }
                    return list.get(0);
                })
                .collect(Collectors.toList());
    }
}
