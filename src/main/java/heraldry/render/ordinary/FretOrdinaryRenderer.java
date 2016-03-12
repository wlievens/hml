package heraldry.render.ordinary;

import heraldry.model.Line;
import heraldry.render.Box;
import heraldry.render.Painter;
import heraldry.render.Point;
import heraldry.render.RenderContour;
import heraldry.util.GeometryUtils;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class FretOrdinaryRenderer implements OrdinaryRenderer
{
    @Override
    public Collection<RenderContour> render(Box bounds, Line line, Painter painter)
    {
        Point center = bounds.getFessPoint();
        double width = bounds.getWidth();
        double height = bounds.getHeight();
        double step = painter.getFretSizeStep();
        double margin = painter.getFretMargin();
        double x1 = bounds.getX1();
        double y1 = bounds.getY1();
        double middleX = center.getX();
        double middleY = y1 + (middleX - x1);
        if (width > height)
        {
            double shift = center.getY() - middleY;
            y1 += shift;
            middleY += shift;
        }
        double distance = Math.min(width, height) / 6;
        double nearX = middleX - distance;
        double nearY = middleY - distance;
        double farX = middleX + distance;
        double farY = middleY + distance;

        List<RenderContour> list = new ArrayList<>();

        list.add(new RenderContour(GeometryUtils.polygon(
                x1 - step, y1,
                x1, y1 - step,
                nearX - margin, nearY - step - margin,
                nearX - step - margin, nearY - margin
        )));

        list.add(new RenderContour(GeometryUtils.polygon(
                nearX + margin, nearY + step + margin,
                nearX + step + margin, nearY + margin,
                farX - margin, farY - step - margin,
                farX - step - margin, farY - margin
        )));

        list.add(new RenderContour(GeometryUtils.polygon(
                farX + margin, farY + step + margin,
                farX + step + margin, farY + margin,
                x1 + width + step, y1 + width,
                x1 + width, y1 + width + step
        )));

        list.add(new RenderContour(GeometryUtils.polygon(
                x1 + width, y1 - step,
                x1 + width + step, y1,
                middleX + step + margin, middleY - margin,
                middleX + margin, middleY - step - margin
        )));

        list.add(new RenderContour(GeometryUtils.polygon(
                middleX - step - margin, middleY + margin,
                middleX - margin, middleY + step + margin,
                x1, y1 + width + step,
                x1 - step, y1 + width
        )));

        list.add(new RenderContour(GeometryUtils.polygon(
                middleX, middleY - 2 * distance - step,
                middleX - 2 * distance - step, middleY,
                middleX - distance - step - margin, middleY + distance - margin,
                middleX - distance - margin, middleY + distance - step - margin,
                middleX - 2 * distance + step, middleY,
                middleX, middleY - 2 * distance + step,
                middleX + distance - step - margin, middleY - distance - margin,
                middleX + distance - margin, middleY - distance - step - margin
        )));

        list.add(new RenderContour(GeometryUtils.polygon(
                middleX, middleY + 2 * distance - step,
                middleX + 2 * distance - step, middleY,
                middleX + distance + margin, middleY - distance + step + margin,
                middleX + distance + step + margin, middleY - distance + margin,
                middleX + 2 * distance + step, middleY,
                middleX, middleY + 2 * distance + step,
                middleX - distance + margin, middleY + step + distance + margin,
                middleX - distance + step + margin, middleY + distance + margin
        )));

        return list;
    }
}
