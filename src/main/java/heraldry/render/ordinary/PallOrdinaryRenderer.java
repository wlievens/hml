package heraldry.render.ordinary;

import heraldry.model.Line;
import heraldry.render.Box;
import heraldry.render.LineRenderer;
import heraldry.render.Painter;
import heraldry.render.RenderContour;
import heraldry.render.path.LinePathStep;
import heraldry.render.path.Path;
import heraldry.render.path.PathStep;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class PallOrdinaryRenderer implements OrdinaryRenderer
{
    private final double sizeRatio;
    private final boolean flipY;

    @Override
    public RenderContour render(RenderContour contour, Line line, Painter painter)
    {
        Box bounds = contour.getBounds();
        double x1 = bounds.getX1();
        double x2 = bounds.getX2();
        double y1 = bounds.getY1();
        double y2 = bounds.getY2();
        double width = bounds.getWidth();
        double height = bounds.getHeight();
        double step = sizeRatio * painter.getOrdinaryThickness() * line.getScaleFactor() / 2;
        double stepDiagonal = step * Math.sqrt(2);
        double period = painter.getLinePeriodFactor() * Math.min(width, height);
        double centerX = (x1 + x2) / 2;
        double centerY = y1 + width / 2;

        List<PathStep> steps = new ArrayList<>();

        if (flipY)
        {
            double endY = centerY + x2 - centerX;
            steps.add(new LinePathStep(centerX - step, y1, centerX + step, y1));
            LineRenderer.line(steps, centerX + step, y1, centerX + step, centerY - stepDiagonal + step, line, period, false, sizeRatio);
            LineRenderer.line(steps, centerX + step, centerY - stepDiagonal + step, x2, endY - stepDiagonal, line, period, false, sizeRatio);
            steps.add(new LinePathStep(x2, endY - stepDiagonal, x2, endY + stepDiagonal));
            LineRenderer.line(steps, x2, endY + stepDiagonal, centerX, centerY + stepDiagonal, line, period, false, sizeRatio);
            LineRenderer.line(steps, centerX, centerY + stepDiagonal, x1, endY + stepDiagonal, line, period, false, sizeRatio);
            steps.add(new LinePathStep(x1, endY + stepDiagonal, x1, endY - stepDiagonal));
            LineRenderer.line(steps, x1, endY - stepDiagonal, centerX - step, centerY - stepDiagonal + step, line, period, false, sizeRatio);
            LineRenderer.line(steps, centerX - step, centerY - stepDiagonal + step, centerX - step, y1, line, period, false, sizeRatio);
        }
        else
        {
            steps.add(new LinePathStep(x1, y1, x1 + stepDiagonal, y1));
            LineRenderer.line(steps, x1 + stepDiagonal, y1, centerX, centerY - stepDiagonal, line, period, false, sizeRatio);
            LineRenderer.line(steps, centerX, centerY - stepDiagonal, x2 - stepDiagonal, y1, line, period, false, sizeRatio);
            steps.add(new LinePathStep(x2 - stepDiagonal, y1, x2, y1));
            steps.add(new LinePathStep(x2, y1, x2, y1 + stepDiagonal));
            LineRenderer.line(steps, x2, y1 + stepDiagonal, centerX + step, centerY + stepDiagonal - step, line, period, false, sizeRatio);
            LineRenderer.line(steps, centerX + step, centerY + stepDiagonal - step, centerX + step, y2, line, period, false, sizeRatio);
            steps.add(new LinePathStep(centerX + step, y2, centerX - step, y2));
            LineRenderer.line(steps, centerX - step, y2, centerX - step, y2, line, period, false, sizeRatio);
            LineRenderer.line(steps, centerX - step, y2, centerX - step, centerY + stepDiagonal - step, line, period, false, sizeRatio);
            LineRenderer.line(steps, centerX - step, centerY + stepDiagonal - step, x1, y1 + stepDiagonal, line, period, false, sizeRatio);
            steps.add(new LinePathStep(x1, y1 + stepDiagonal, x1, y1));
        }

        return new RenderContour(new Path(steps));
    }
}
