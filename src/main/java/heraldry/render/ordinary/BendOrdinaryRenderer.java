package heraldry.render.ordinary;

import heraldry.model.Line;
import heraldry.model.Tincture;
import heraldry.render.Box;
import heraldry.render.LinePathStep;
import heraldry.render.Painter;
import heraldry.render.PathStep;
import heraldry.render.RenderShape;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class BendOrdinaryRenderer implements OrdinaryRenderer
{
    private final double sizeRatio;

    @Override
    public Collection<RenderShape> render(Box bounds, Tincture tincture, Line line, Painter painter)
    {
        double x1 = bounds.getX1();
        double y1 = bounds.getY1();
        double width = bounds.getWidth();
        double height = bounds.getHeight();
        double step;
        double right1;
        double right2;
        double bottom1;
        double bottom2;
        if (width > height)
        {
            step = sizeRatio * painter.getOrdinaryThickness() / Math.sqrt(2);
            bottom1 = height;
            bottom2 = height;
            right1 = height + step;
            right2 = height - step;
        }
        else
        {
            step = sizeRatio * painter.getOrdinaryThickness() / Math.sqrt(2);
            bottom1 = width - step;
            bottom2 = width + step;
            right1 = width;
            right2 = width;
        }
        List<PathStep> steps = new ArrayList<>();

        int indents = 10;

        switch (line)
        {
            case PLAIN:
            {
                steps.add(new LinePathStep(x1, y1, x1 + step, y1));
                steps.add(new LinePathStep(x1 + step, y1, x1 + right1, y1 + bottom1));
                steps.add(new LinePathStep(x1 + right1, y1 + bottom1, x1 + right2, y1 + bottom2));
                steps.add(new LinePathStep(x1 + right2, y1 + bottom2, x1, y1 + step));
                steps.add(new LinePathStep(x1, y1 + step, x1, y1));
                break;
            }
            case INDENTED:
            {
                steps.add(new LinePathStep(x1, y1, x1 + step, y1));
                plotLineIndented(steps, x1 + step, y1, x1 + right1, y1 + bottom1, indents);
                steps.add(new LinePathStep(x1 + right1, y1 + bottom1, x1 + right2, y1 + bottom2));
                plotLineIndented(steps, x1 + right2, y1 + bottom2, x1, y1 + step, indents);
                steps.add(new LinePathStep(x1, y1 + step, x1, y1));
                break;
            }
            default:
            {
                throw new IllegalStateException("NYI");
            }
        }

        return Arrays.asList(new RenderShape(steps, painter.getColor(tincture), null));
    }

    private void plotLineIndented(List<PathStep> steps, double startX, double startY, double endX, double endY, int indents)
    {
        double strideX = (endX - startX) / indents;
        double strideY = (endY - startY) / indents;
        for (int n = 0; n < indents; ++n)
        {
            double x1 = startX + (n + 0) * strideX + 0.5 * strideX;
            double y1 = startY + (n + 0) * strideY;
            double x2 = startX + (n + 1) * strideX + 0.5 * strideX;
            double y2 = startY + (n + 1) * strideY;
            if (n == 0)
            {
                steps.add(new LinePathStep(startX, startY, x1, y1));
            }
            steps.add(new LinePathStep(x1, y1, x1, y2));
            if (n < indents - 1)
            {
                steps.add(new LinePathStep(x1, y2, x2, y2));
            }
            else
            {
                steps.add(new LinePathStep(x2, y2, endX, endY));
            }
        }
    }
}
