package heraldry.model;

import heraldry.render.Box;
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
    @NonNull
    private final Tincture tincture;

    @NonNull
    private final Charge charge;

    @Override
    public String generateBlazon(BlazonContext context)
    {
        return String.format("Semy of %s", charge.generateBlazon(context.withPlural(true)));
    }

    @Override
    public Collection<RenderShape> render(RenderContour contour, Painter painter)
    {
        Box bounds = contour.getBounds();
        double width = bounds.getWidth();
        double height = bounds.getHeight();
        int columns = 4;
        double size = width / columns;
        int rows = (int)Math.ceil(height / size);

        List<RenderShape> list = new ArrayList<>();
        list.add(new RenderShape(contour.getSteps(), painter.getColor(tincture), null));
        for (int row = 0; row < rows + 1; ++row)
        {
            for (int column = 0; column < columns + 1; ++column)
            {
                double x1 = (column - 0.5 + 0.5 * (row % 2)) * size;
                double x2 = (column + 0.5 + 0.5 * (row % 2)) * size;
                double y1 = (row - 0.5) * size;
                double y2 = (row + 0.5) * size;
                RenderContour child = new RenderContour(GeometryUtils.rectangle(x1, y1, x2, y2));
                list.addAll(contour.clipShapes(charge.render(child, painter)));
            }
        }
        return list;
    }
}
