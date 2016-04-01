package heraldry.render.ordinary;

import heraldry.model.Line;
import heraldry.render.Box;
import heraldry.render.Painter;
import heraldry.render.Point;
import heraldry.render.RenderContour;
import heraldry.render.Surface;
import heraldry.render.path.Path;
import heraldry.util.GeometryUtils;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class FretOrdinaryRenderer implements OrdinaryRenderer
{
    @Override
    public RenderContour render(RenderContour contour, Line line, Painter painter)
    {
        Box bounds = contour.getBounds();
        Point center = bounds.getFessPoint();
        double width = bounds.getWidth();
        double height = bounds.getHeight();
        double step = painter.getFretThickness() / Math.sqrt(2);
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
        double scale = 2.0 / 3.0;
        double distance = Math.min(width, height) / 4 * scale - step / 2;
        double nearX = middleX - distance;
        double nearY = middleY - distance;
        double farX = middleX + distance;
        double farY = middleY + distance;

        List<Path> paths = new ArrayList<>();

        paths.add(GeometryUtils.polygon(
                x1 - step, y1,
                x1, y1 - step,
                nearX - margin, nearY - step - margin,
                nearX - step - margin, nearY - margin
        ));

        paths.add(GeometryUtils.polygon(
                nearX + margin, nearY + step + margin,
                nearX + step + margin, nearY + margin,
                farX - margin, farY - step - margin,
                farX - step - margin, farY - margin
        ));

        paths.add(GeometryUtils.polygon(
                farX + margin, farY + step + margin,
                farX + step + margin, farY + margin,
                x1 + width + step, y1 + width,
                x1 + width, y1 + width + step
        ));

        paths.add(GeometryUtils.polygon(
                x1 + width, y1 - step,
                x1 + width + step, y1,
                middleX + step + margin, middleY - margin,
                middleX + margin, middleY - step - margin
        ));

        paths.add(GeometryUtils.polygon(
                middleX - step - margin, middleY + margin,
                middleX - margin, middleY + step + margin,
                x1, y1 + width + step,
                x1 - step, y1 + width
        ));

        paths.add(GeometryUtils.polygon(
                middleX, middleY - 2 * distance - step,
                middleX - 2 * distance - step, middleY,
                middleX - distance - step - margin, middleY + distance - margin,
                middleX - distance - margin, middleY + distance - step - margin,
                middleX - 2 * distance + step, middleY,
                middleX, middleY - 2 * distance + step,
                middleX + distance - step - margin, middleY - distance - margin,
                middleX + distance - margin, middleY - distance - step - margin
        ));

        paths.add(GeometryUtils.polygon(
                middleX, middleY + 2 * distance - step,
                middleX + 2 * distance - step, middleY,
                middleX + distance + margin, middleY - distance + step + margin,
                middleX + distance + step + margin, middleY - distance + margin,
                middleX + 2 * distance + step, middleY,
                middleX, middleY + 2 * distance + step,
                middleX - distance + margin, middleY + step + distance + margin,
                middleX - distance + step + margin, middleY + distance + margin
        ));

        return new RenderContour(new Surface(paths));
    }
}
