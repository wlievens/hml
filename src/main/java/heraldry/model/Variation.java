package heraldry.model;

import heraldry.render.variation.BarryVariationRenderer;
import heraldry.render.variation.BendyVariationRenderer;
import heraldry.render.variation.ChequyVariationRenderer;
import heraldry.render.variation.ChevronnyVariationRenderer;
import heraldry.render.variation.FrettyVariationRenderer;
import heraldry.render.variation.GyronnyVariationRenderer;
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
    BENDY(new BendyVariationRenderer(false)),
    BENDY_SINISTER(new BendyVariationRenderer(true)),
    CHEQUY(new ChequyVariationRenderer()),
    CHEVRONNY(new ChevronnyVariationRenderer(false)),
    CHEVRONNY_INVERTED(new ChevronnyVariationRenderer(true)),
    FRETTY(new FrettyVariationRenderer()),
    PAPPELLONY(new PappellonyVariationRenderer()),
    GYRONNY(new GyronnyVariationRenderer()),
    LOZENGY(new LozengyVariationRenderer()),
    PALY(new PalyVariationRenderer());

    @Getter
    private final VariationRenderer renderer;

    public String getLabel()
    {
        return EnumUtils.getLabel(this);
    }
}
