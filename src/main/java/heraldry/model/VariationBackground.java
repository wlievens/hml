package heraldry.model;

import heraldry.render.Painter;
import heraldry.render.RenderContour;
import heraldry.render.RenderShape;
import heraldry.render.variation.VariationRenderer;
import heraldry.util.StringUtils;
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
    private final int number;

    public VariationBackground(Variation variation, Tincture firstTincture, Tincture secondTincture)
    {
        this(variation, firstTincture, secondTincture, Line.PLAIN, 0);
    }

    @Override
    public String generateBlazon(BlazonContext context)
    {
        String label = variation.getLabel();
        if (line != Line.PLAIN)
        {
            label += " " + line.getLabel().toLowerCase();
        }
        if (number != 0)
        {
            label += " of " + StringUtils.getNumeral(number).toLowerCase();
        }
        switch (variation)
        {
            case FRETTY:
                return String.format("%s %s %s", firstTincture.getLabel().toLowerCase(), label, secondTincture.getLabel().toLowerCase());
            default:
                return String.format("%s %s and %s", label, firstTincture.getLabel().toLowerCase(), secondTincture.getLabel().toLowerCase());
        }
    }

    @Override
    public Collection<RenderShape> render(RenderContour contour, Painter painter)
    {
        VariationRenderer renderer = variation.getRenderer();
        if (renderer == null)
        {
            log.warn("No renderer implemented for variation '{}'", variation);
            return Collections.singleton(new RenderShape(contour.getSteps(), painter.getPaint(firstTincture), null, "fall-back background for missing variation"));
        }
        return renderer.render(contour, firstTincture, secondTincture, line, number, painter);
    }
}
