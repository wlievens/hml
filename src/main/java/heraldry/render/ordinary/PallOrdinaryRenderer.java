package heraldry.render.ordinary;

import heraldry.model.Line;
import heraldry.render.Box;
import heraldry.render.LinePathStep;
import heraldry.render.LineRenderer;
import heraldry.render.Painter;
import heraldry.render.PathStep;
import heraldry.render.RenderContour;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class PallOrdinaryRenderer implements OrdinaryRenderer
{
    private final double sizeRatio;
    private final boolean flipY;

    @Override
    public Collection<RenderContour> render(Box bounds, Line line, Painter painter)
    {
        double x1 = bounds.getX1();
        double x2 = bounds.getX2();
        double y1 = bounds.getY1();
        double y2 = bounds.getY2();
        double width = bounds.getWidth();
        double height = bounds.getHeight();
        double step = sizeRatio * painter.getOrdinaryThickness() * line.getScaleFactor() / 2;
        double stepDiagonal = step * Math.sqrt(2);
        double period = painter.getLinePeriodFactor() * Math.min(width, height);
        double midX = (x1 + x2) / 2;
        double midY = y1 + width / 2;

        List<PathStep> steps = new ArrayList<>();

        if (flipY)
        {

        }
        else
        {
            steps.add(new LinePathStep(x1, y1, x1 + stepDiagonal, y1));
            LineRenderer.line(steps, x1 + stepDiagonal, y1, midX, midY - stepDiagonal, line, period, false, sizeRatio);
            LineRenderer.line(steps, midX, midY - stepDiagonal, x2 - stepDiagonal, y1, line, period, false, sizeRatio);
            steps.add(new LinePathStep(x2 - stepDiagonal, y1, x2, y1));
            steps.add(new LinePathStep(x2, y1, x2, y1 + stepDiagonal));
            LineRenderer.line(steps, x2, y1 + stepDiagonal, midX + step, midY + stepDiagonal - step, line, period, false, sizeRatio);
            LineRenderer.line(steps, midX + step, midY + stepDiagonal - step, midX + step, y2, line, period, false, sizeRatio);
            steps.add(new LinePathStep(midX + step, y2, midX - step, y2));
            LineRenderer.line(steps, midX - step, y2, midX - step, y2, line, period, false, sizeRatio);
            LineRenderer.line(steps, midX - step, y2, midX - step, midY + stepDiagonal - step, line, period, false, sizeRatio);
            LineRenderer.line(steps, midX - step, midY + stepDiagonal - step, x1, y1 + stepDiagonal, line, period, false, sizeRatio);
            steps.add(new LinePathStep(x1, y1 + stepDiagonal, x1, y1));
        }
        
        return Collections.singleton(new RenderContour(steps));
    }
}