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
import java.util.List;

@RequiredArgsConstructor
public class SaltireOrdinaryRenderer implements OrdinaryRenderer
{
    private final double rangeRatio;
    private final double sizeRatio;

    @Override
    public RenderContour render(RenderContour contour, Line line, Painter painter)
    {
        Box bounds = contour.getBounds();

        double width = bounds.getWidth();
        double height = bounds.getHeight();
        double scaledWidth = width * rangeRatio;
        double midX = bounds.lerpX(0.5);
        double midY = bounds.getY1() + width / 2;
        double x1 = midX - scaledWidth / 2;
        double y1 = midY - scaledWidth / 2;
        double x2 = midX + scaledWidth / 2;

        double step = sizeRatio * painter.getOrdinaryThickness() / Math.sqrt(2) * line.getScaleFactor();

        double period = painter.getLinePeriodFactor() * Math.min(width, height);
        List<PathStep> steps = new ArrayList<>();

        LineRenderer.line(steps, x1 + step / 2, y1 - step / 2, midX, midY - step, line, period, false, sizeRatio);
        LineRenderer.line(steps, midX, midY - step, x2 - step / 2, y1 - step / 2, line, period, false, sizeRatio);
        steps.add(new LinePathStep(x2 - step / 2, y1 - step / 2, x2 + step / 2, y1 + step / 2));
        LineRenderer.line(steps, x2 + step / 2, y1 + step / 2, midX + step, midY, line, period, false, sizeRatio);
        LineRenderer.line(steps, midX + step, midY, x2 + step / 2, y1 + scaledWidth - step / 2, line, period, false, sizeRatio);
        steps.add(new LinePathStep(x2 + step / 2, y1 + scaledWidth - step / 2, x2 - step / 2, y1 + scaledWidth + step / 2));
        LineRenderer.line(steps, x2 - step / 2, y1 + scaledWidth + step / 2, midX, midY + step, line, period, false, sizeRatio);
        LineRenderer.line(steps, midX, midY + step, x1 + step / 2, y1 + scaledWidth + step / 2, line, period, false, sizeRatio);
        steps.add(new LinePathStep(x1 + step / 2, y1 + scaledWidth + step / 2, x1 - step / 2, y1 + scaledWidth - step / 2));
        LineRenderer.line(steps, x1 - step / 2, y1 + scaledWidth - step / 2, midX - step, midY, line, period, false, sizeRatio);
        LineRenderer.line(steps, midX - step, midY, x1, y1 + step, line, period, false, sizeRatio);
        steps.add(new LinePathStep(x1, y1 + step, x1 - step / 2, y1 + step / 2));

        return new RenderContour(new Path(steps));
    }
}
