package heraldry.render.division;

import heraldry.render.Box;
import heraldry.render.Painter;
import heraldry.render.Point;
import heraldry.render.RenderContour;
import heraldry.util.GeometryUtils;

import java.util.Arrays;
import java.util.List;

public class FessDivisionRenderer extends AbstractDivisionRenderer
{
    @Override
    public List<RenderContour> render(RenderContour contour, Painter painter)
    {
        Box bounds = contour.getBounds();
        Point center = bounds.getFessPoint();
        double x1 = bounds.getX1();
        double x2 = bounds.getX2();
        double y1 = bounds.getY1();
        double y2 = bounds.getY2();
        double cy = center.getY();
        return Arrays.asList(
                contour.clip(new RenderContour(GeometryUtils.rectangle(x1, y1, x2, cy))).get(0),
                contour.clip(new RenderContour(GeometryUtils.rectangle(x1, cy, x2, y2))).get(0)
        );
    }
}
