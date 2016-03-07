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
public class FieldBackground extends Background
{
    private final Tincture tincture;

    @Override
    public String generateBlazon(BlazonContext context)
    {
        return tincture.getLabel();
    }

    @Override
    public Collection<RenderShape> render(RenderContour contour, Painter painter)
    {
        return Collections.singleton(new RenderShape(contour.getSteps(), painter.getColor(tincture), null));
    }
}