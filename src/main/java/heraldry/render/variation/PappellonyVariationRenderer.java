package heraldry.render.variation;

import heraldry.model.Line;
import heraldry.model.Tincture;
import heraldry.render.Box;
import heraldry.render.Painter;
import heraldry.render.RenderContour;
import heraldry.render.RenderShape;
import heraldry.render.path.CubicPathStep;
import heraldry.render.path.LinePathStep;
import heraldry.render.path.Path;
import heraldry.render.path.PathStep;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PappellonyVariationRenderer implements VariationRenderer
{
    @Override
    public Collection<RenderShape> render(RenderContour contour, Tincture firstTincture, Tincture secondTincture, Line line, int number, Painter painter)
    {
        Box bounds = contour.getBounds();
        double width = bounds.getWidth();
        double height = bounds.getHeight();
        List<RenderShape> list = new ArrayList<>();
        list.add(contour.render(painter.getPaint(firstTincture), null, getClass().getSimpleName() + " background"));
        int columns = 4;
        double spacingX = width / columns;
        double spacingY = spacingX / 2;
        double step = spacingX * 0.1;
        double control1 = spacingX / 4;
        double control2 = (spacingX + 2 * step) / 4;
        double ratioY = 0.7;
        int rows = (int)Math.ceil(height / spacingY);
        for (int row = 0; row < rows; ++row)
        {
            for (int column = -1; column < columns; ++column)
            {
                // X1-X2          X4-X5   :: y1
                // |   \          /   |
                // \    \___X3___/    /   :: y2
                //  \________________/    :: y3
                double x1 = bounds.lerpX(column / (double)columns) + (row % 2) * spacingX / 2;
                double x2 = x1 + step;
                double x3 = x1 + spacingX / 2;
                double x4 = x1 + spacingX - step;
                double x5 = x1 + spacingX;
                double y1 = bounds.getY1() + row * spacingY;
                double y2 = y1 + spacingY - step;
                double y3 = y1 + spacingY;
                List<PathStep> steps = new ArrayList<>();
                steps.add(new LinePathStep(x1, y1, x2, y1));
                steps.add(new CubicPathStep(x2, y1, x2, y1 + control1, x3 - control1, y2, x3, y2));
                steps.add(new CubicPathStep(x3, y2, x3 + control1, y2, x4, y1 + control1, x4, y1));
                steps.add(new LinePathStep(x4, y1, x5, y1));
                steps.add(new CubicPathStep(x5, y1, x5, y1 + control2, x3 + control2, y3, x3, y3));
                steps.add(new CubicPathStep(x3, y3, x3 - control2, y3, x1, y1 + control2, x1, y1));
                list.add(new RenderShape(new Path(steps), painter.getPaint(secondTincture), null, "pappellony " + row + ", " + column));
            }
        }
        return contour.clipShapes(list);
    }

}
