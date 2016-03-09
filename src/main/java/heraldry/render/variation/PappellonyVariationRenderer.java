package heraldry.render.variation;

import heraldry.model.Line;
import heraldry.model.Tincture;
import heraldry.render.Box;
import heraldry.render.LinePathStep;
import heraldry.render.Painter;
import heraldry.render.PathStep;
import heraldry.render.QuadraticPathStep;
import heraldry.render.RenderContour;
import heraldry.render.RenderShape;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PappellonyVariationRenderer implements VariationRenderer
{
    @Override
    public Collection<RenderShape> render(RenderContour contour, Tincture firstTincture, Tincture secondTincture, Line line, Painter painter)
    {
        Box bounds = contour.getBounds();
        double width = bounds.getWidth();
        double height = bounds.getHeight();
        List<RenderShape> list = new ArrayList<>();
        list.add(new RenderShape(contour.getSteps(), painter.getColor(firstTincture), null));
        int columns = 4;
        double spacingX = width / columns;
        double step = spacingX * 0.1;
        double ratioY = 0.7;

        int row = 0;
        while (row < 10)
        {
            double y = bounds.getY1() + row * spacingX / 2;
            for (int column = 0; column < columns; ++column)
            {
                double x = bounds.lerpX(column / (double)columns) + (y % 2) * spacingX / 2;
                List<PathStep> steps = new ArrayList<>();
                steps.add(new LinePathStep(x, y, x + step, y));
                steps.add(new QuadraticPathStep(x + step, y, (x + spacingX / 2), y + spacingX, x + spacingX - step, y));
                steps.add(new LinePathStep(x + spacingX - step, y, x + spacingX, y));
                steps.add(new QuadraticPathStep(x + spacingX, y, (x + spacingX / 2), y + spacingX + step, x, y));
                list.add(new RenderShape(steps, painter.getColor(secondTincture), null));
            }
            ++row;
        }

        return contour.clipAll(list);
    }

}
