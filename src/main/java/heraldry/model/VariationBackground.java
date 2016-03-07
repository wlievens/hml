package heraldry.model;

import heraldry.render.Painter;
import heraldry.render.RenderContour;
import heraldry.render.RenderShape;
import heraldry.render.variation.VariationRenderer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Collection;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class VariationBackground extends Background
{
    private final Variation variation;
    private final Tincture firstTincture;
    private final Tincture secondTincture;

    @Override
    public String generateBlazon(BlazonContext context)
    {
        switch (variation)
        {
            case FRETTY:
                return String.format("%s %s %s", firstTincture.getLabel(), variation.getLabel(), secondTincture.getLabel());
            default:
                return String.format("%s %s and %s", variation.getLabel(), firstTincture.getLabel(), secondTincture.getLabel());
        }
    }

    @Override
    public Collection<RenderShape> render(RenderContour contour, Painter painter)
    {
        VariationRenderer renderer = variation.getRenderer();
        if (renderer == null)
        {
            throw new IllegalStateException(String.format("No renderer implemented for variation '%s'", variation));
        }
        return renderer.render(contour, firstTincture, secondTincture, painter);
    }
}
