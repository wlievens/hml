package heraldry.model;

import heraldry.render.Painter;
import heraldry.render.RenderContour;
import heraldry.render.RenderShape;
import heraldry.render.path.Path;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class CounterchangedBackground extends Background
{
    private final Tincture firstTincture;
    private final Tincture secondTincture;

    @Override
    public String generateBlazon(BlazonContext context)
    {
        return "Counterchanged";
    }

    @Override
    public Path getSpine(RenderContour contour)
    {
        // This background has no natural spine for positioning charges
        return null;
    }

    @Override
    public Collection<RenderShape> render(RenderContour contour, Painter painter)
    {
        return Collections.singleton(contour.render(painter.getCounterchangedPaint(firstTincture, secondTincture), null, getClass().getSimpleName()));
    }
}
