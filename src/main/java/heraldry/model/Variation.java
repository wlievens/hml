package heraldry.model;

import heraldry.render.variation.BarryVariationRenderer;
import heraldry.render.variation.ChequyVariationRenderer;
import heraldry.render.variation.ChevronnyVariationRenderer;
import heraldry.render.variation.FrettyVariationRenderer;
import heraldry.render.variation.LozengyVariationRenderer;
import heraldry.render.variation.PalyVariationRenderer;
import heraldry.render.variation.PappellonyVariationRenderer;
import heraldry.render.variation.VariationRenderer;
import heraldry.util.EnumUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Variation
{
    BARRY(new BarryVariationRenderer()),
    BENDY(null),
    BENDY_SINISTER(null),
    CHEQUY(new ChequyVariationRenderer()),
    CHEVRONNY(new ChevronnyVariationRenderer(false)),
    CHEVRONNY_INVERTED(new ChevronnyVariationRenderer(true)),
    FRETTY(new FrettyVariationRenderer()),
    PAPPELLONY(new PappellonyVariationRenderer()),
    GYRONNY(null),
    LOZENGY(new LozengyVariationRenderer()),
    PALY(new PalyVariationRenderer());

    @Getter
    private final VariationRenderer renderer;

    public String getLabel()
    {
        return EnumUtils.getLabel(this);
    }
}
