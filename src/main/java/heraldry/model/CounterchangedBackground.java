package heraldry.model;

import heraldry.render.Painter;
import heraldry.render.RenderContour;
import heraldry.render.RenderShape;
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
    public Collection<RenderShape> render(RenderContour contour, Painter painter)
    {
        return Collections.singleton(new RenderShape(contour.getPath(), painter.getCounterchangedPaint(firstTincture, secondTincture), null, getClass().getSimpleName()));
    }
}
