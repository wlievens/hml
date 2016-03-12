package heraldry.model;

import heraldry.render.Painter;
import heraldry.render.RenderContour;
import heraldry.render.RenderShape;
import heraldry.render.variation.VariationRenderer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Collections;

@Slf4j
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class VariationBackground extends Background
{
    private final Variation variation;
    private final Tincture firstTincture;
    private final Tincture secondTincture;
    private final Line line;

    @Override
    public String generateBlazon(BlazonContext context)
    {
        String variationLabelWithLine = variation.getLabel() + (line == Line.PLAIN ? "" : (" " + line.getLabel().toLowerCase()));
        switch (variation)
        {
            case FRETTY:
                return String.format("%s %s %s", firstTincture.getLabel(), variationLabelWithLine, secondTincture.getLabel());
            default:
                return String.format("%s %s and %s", variationLabelWithLine, firstTincture.getLabel(), secondTincture.getLabel());
        }
    }

    @Override
    public Collection<RenderShape> render(RenderContour contour, Painter painter)
    {
        VariationRenderer renderer = variation.getRenderer();
        if (renderer == null)
        {
            log.warn("No renderer implemented for variation '{}'", variation);
            return Collections.singleton(new RenderShape(contour.getSteps(), painter.getColor(firstTincture), null));
        }
        return renderer.render(contour, firstTincture, secondTincture, line, painter);
    }
}
