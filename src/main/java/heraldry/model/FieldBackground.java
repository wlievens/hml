package heraldry.model;

import heraldry.render.Painter;
import heraldry.render.RenderContour;
import heraldry.render.RenderShape;
import heraldry.render.path.Path;
import lombok.Getter;
import lombok.NonNull;
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
    @NonNull
    private final Tincture tincture;

    @Override
    public String generateBlazon(BlazonContext context)
    {
        if (context.getTincture() == tincture)
        {
            return "of the field";
        }
        return tincture.getLabel();
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
        return Collections.singleton(contour.render(painter.getPaint(tincture), null, getClass().getSimpleName()));
    }
}
