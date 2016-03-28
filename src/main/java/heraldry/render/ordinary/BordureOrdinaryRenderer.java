package heraldry.render.ordinary;

import heraldry.model.Line;
import heraldry.render.Box;
import heraldry.render.Painter;
import heraldry.render.Point;
import heraldry.render.RenderContour;
import heraldry.render.path.CubicPathStep;
import heraldry.render.path.LinePathStep;
import heraldry.render.path.Path;
import heraldry.render.path.PathStep;
import heraldry.render.path.QuadraticPathStep;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class BordureOrdinaryRenderer implements OrdinaryRenderer
{
    @Override
    public Collection<RenderContour> render(RenderContour contour, Line line, Painter painter)
    {
        Box bounds = contour.getBounds();
        double width = bounds.getWidth();
        double height = bounds.getHeight();
        Point center = bounds.getFessPoint();
        double centerX = center.getX();
        double centerY = center.getY();
        double size = Math.min(width, height);
        double period = painter.getLinePeriodFactor() * size;
        double scale = 1.0 - painter.getOrdinaryThickness() / size;

        int stepCount = contour.getPath().getStepCount();
        int midIndex = stepCount / 2;

        List<PathStep> steps1 = createBordure(contour, centerX, centerY, scale, 0, midIndex - 1);
        List<PathStep> steps2 = createBordure(contour, centerX, centerY, scale, midIndex, stepCount - 1);
        return Arrays.asList(new RenderContour(new Path(steps1)), new RenderContour(new Path(steps2)));
    }

    private List<PathStep> createBordure(RenderContour contour, double centerX, double centerY, double scale, int start, int end)
    {
        List<PathStep> steps = new ArrayList<>();
        for (int index = start; index <= end; ++index)
        {
            steps.add(contour.getPath().getStep(index));
        }
        {
            PathStep last = steps.get(steps.size() - 1);
            PathStep rescaled = rescale(last, centerX, centerY, scale);
            steps.add(new LinePathStep(last.getEndX(), last.getEndY(), rescaled.getEndX(), rescaled.getEndY()));
        }
        for (int index = end; index >= start; --index)
        {
            steps.add(rescale(contour.getPath().getStep(index), centerX, centerY, scale).inverse());
        }
        {
            PathStep last = steps.get(steps.size() - 1);
            PathStep first = steps.get(start);
            steps.add(new LinePathStep(last.getEndX(), last.getEndY(), first.getStartX(), first.getStartY()));
        }
        return steps;
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
