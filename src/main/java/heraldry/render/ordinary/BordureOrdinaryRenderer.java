package heraldry.render.ordinary;

import heraldry.model.Line;
import heraldry.render.Box;
import heraldry.render.Painter;
import heraldry.render.Point;
import heraldry.render.RenderContour;
import heraldry.render.Surface;
import heraldry.render.path.CubicPathStep;
import heraldry.render.path.LinePathStep;
import heraldry.render.path.Path;
import heraldry.render.path.PathStep;
import heraldry.render.path.QuadraticPathStep;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class BordureOrdinaryRenderer implements OrdinaryRenderer
{
    private final double outerScaleFactor;
    private final double innerScaleFactor;

    @Override
    public RenderContour render(RenderContour contour, Line line, Painter painter)
    {
        Box bounds = contour.getBounds();
        double width = bounds.getWidth();
        double height = bounds.getHeight();
        Point center = bounds.getFessPoint();
        double centerX = center.getX();
        double centerY = center.getY();
        double size = Math.min(width, height);
        double period = painter.getLinePeriodFactor() * size;
        double outerScale = 1.0 - outerScaleFactor * 2 * painter.getOrdinaryThickness() / width;
        double innerScale = outerScale - innerScaleFactor * 2 * painter.getOrdinaryThickness() / width;

        Surface surface = contour.getSurface();
        if (!surface.isSingular())
        {
            throw new IllegalArgumentException();
        }
        Path path = surface.getPositives().get(0);
        Path outer = new Path((List<PathStep>)path.getSteps().stream()
                .map(step -> rescale(step, centerX, centerY, outerScale))
                .collect(toList()));
        Path inner = new Path((List<PathStep>)path.getSteps().stream()
                .map(step -> rescale(step, centerX, centerY, innerScale))
                .collect(toList()));
        return new RenderContour(new Surface(Collections.singletonList(outer), Collections.singletonList(inner)));
    }

    private PathStep rescale(PathStep step, double centerX, double centerY, double scale)
    {
        if (step instanceof LinePathStep)
        {
            return rescale((LinePathStep)step, centerX, centerY, scale);
        }
        if (step instanceof QuadraticPathStep)
        {
            return rescale((QuadraticPathStep)step, centerX, centerY, scale);
        }
        if (step instanceof CubicPathStep)
        {
            return rescale((CubicPathStep)step, centerX, centerY, scale);
        }
        throw new IllegalStateException();
    }
    
    private QuadraticPathStep rescale(QuadraticPathStep step, double centerX, double centerY, double scale)
    {
        QuadraticPathStep quadraticStep = step;
        double x1 = centerX + (quadraticStep.getX1() - centerX) * scale;
        double y1 = centerY + (quadraticStep.getY1() - centerY) * scale;
        double x2 = centerX + (quadraticStep.getX2() - centerX) * scale;
        double y2 = centerY + (quadraticStep.getY2() - centerY) * scale;
        double x3 = centerX + (quadraticStep.getX3() - centerX) * scale;
        double y3 = centerY + (quadraticStep.getY3() - centerY) * scale;
        return new QuadraticPathStep(x1, y1, x2, y2, x3, y3);
    }
    
    private LinePathStep rescale(LinePathStep step, double centerX, double centerY, double scale)
    {
        LinePathStep lineStep = step;
        double x1 = centerX + (lineStep.getX1() - centerX) * scale;
        double y1 = centerY + (lineStep.getY1() - centerY) * scale;
        double x2 = centerX + (lineStep.getX2() - centerX) * scale;
        double y2 = centerY + (lineStep.getY2() - centerY) * scale;
        return new LinePathStep(x1, y1, x2, y2);
    }
    
    private CubicPathStep rescale(CubicPathStep step, double centerX, double centerY, double scale)
    {
        CubicPathStep cubicStep = step;
        double x1 = centerX + (cubicStep.getX1() - centerX) * scale;
        double y1 = centerY + (cubicStep.getY1() - centerY) * scale;
        double x2 = centerX + (cubicStep.getX2() - centerX) * scale;
        double y2 = centerY + (cubicStep.getY2() - centerY) * scale;
        double x3 = centerX + (cubicStep.getX3() - centerX) * scale;
        double y3 = centerY + (cubicStep.getY3() - centerY) * scale;
        double x4 = centerX + (cubicStep.getX4() - centerX) * scale;
        double y4 = centerY + (cubicStep.getY4() - centerY) * scale;
        return new CubicPathStep(x1, y1, x2, y2, x3, y3, x4, y4);
    }
}
