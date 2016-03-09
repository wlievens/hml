package heraldry.model;

import heraldry.render.variation.ChequyVariationRenderer;
import heraldry.render.variation.FrettyVariationRenderer;
import heraldry.render.variation.LozengyVariationRenderer;
import heraldry.render.variation.VariationRenderer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Variation
{
    BARRY(null),
    BENDY(null),
    BENDY_SINISTER(null),
    CHEQUY(new ChequyVariationRenderer()),
    CHEVRONNY(null),
    FRETTY(new FrettyVariationRenderer()),
    GYRONNY(null),
    LOZENGY(new LozengyVariationRenderer()),
    PALY(null);

    @Getter
    private final VariationRenderer renderer;

    public String getLabel()
    {
        return Character.toUpperCase(name().toLowerCase().charAt(0)) + name().toLowerCase().substring(1);
    }
}
