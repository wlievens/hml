package heraldry.render.ordinary;

import heraldry.model.Line;
import heraldry.model.Tincture;
import heraldry.render.Box;
import heraldry.render.LinePathStep;
import heraldry.render.LineRenderer;
import heraldry.render.Painter;
import heraldry.render.PathStep;
import heraldry.render.RenderShape;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class GyronOrdinaryRenderer implements OrdinaryRenderer
{
    private final boolean flipX;
    private final boolean flipY;

    @Override
    public Collection<RenderShape> render(Box bounds, Tincture tincture, Line line, Painter painter)
    {
        double x1 = bounds.getX1();
        double y1 = bounds.getY1();
        double x2 = bounds.getX2();
        double y2 = bounds.getY2();
        double width = bounds.getWidth();
        double height = bounds.getHeight();
        double period = painter.getLinePeriodFactor() * Math.min(width, height);
        List<PathStep> steps = new ArrayList<>();

        double startX = flipX ? x2 : x1;
        double endX = flipX ? x1 : x2;
        LineRenderer.plotLine(steps, startX, y1, endX, y2, line, period, false);
        steps.add(new LinePathStep(endX, y2, startX, y2));
        steps.add(new LinePathStep(startX, y2, startX, y1));

        return Arrays.asList(new RenderShape(steps, painter.getColor(tincture), null));
    }
}
