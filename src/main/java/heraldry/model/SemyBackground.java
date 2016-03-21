package heraldry.model;

import heraldry.render.Box;
import heraldry.render.paint.Color;
import heraldry.render.Painter;
import heraldry.render.RenderContour;
import heraldry.render.RenderShape;
import heraldry.util.GeometryUtils;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class SemyBackground extends Background
{
    private static final boolean DEBUG = false;

    @NonNull
    private final Background background;

    @NonNull
    private final Charge charge;

    @Override
    public String generateBlazon(BlazonContext context)
    {
        return String.format("%s semy of %s", background.generateBlazon(context), charge.generateBlazon(context.withPlural(true)));
    }

    @Override
    public Collection<RenderShape> render(RenderContour contour, Painter painter)
    {
        Box bounds = contour.getBounds();
        double width = bounds.getWidth();
        double height = bounds.getHeight();
        int columns = 5;
        double size = width / columns;
        int rows = (int)Math.ceil(height / size);

        List<RenderShape> list = new ArrayList<>();
        list.addAll(background.render(contour, painter));
        double margin = size * 0.05;
        for (int row = 0; row < rows + 1; ++row)
        {
            for (int column = 0; column < columns + 1; ++column)
            {
                double x1 = (column - 0.5 + 0.5 * (row % 2)) * size + margin;
                double x2 = (column + 0.5 + 0.5 * (row % 2)) * size - margin;
                double y1 = (row - 0.5) * size + margin;
                double y2 = (row + 0.5) * size - margin;
                RenderContour child = new RenderContour(GeometryUtils.rectangle(x1, y1, x2, y2));
                list.addAll(contour.clipShapes(charge.render(child, painter)));
                if (DEBUG)
                {
                    list.add(new RenderShape(child.getSteps(), null, new Color(1, 0, 1)));
                }
            }
        }
        return list;
    }
}
