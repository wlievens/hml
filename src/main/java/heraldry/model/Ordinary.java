package heraldry.model;

import heraldry.render.ordinary.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Ordinary
{
    BAR(null),
    BEND(new BendOrdinaryRenderer(false, 1.0)),
    BEND_SINISTER(new BendOrdinaryRenderer(true, 1.0)),
    BENDLET(new BendOrdinaryRenderer(false, 0.5)),
    BENDLET_SINISTER(new BendOrdinaryRenderer(true, 0.5)),
    BORDURE(null),
    CANTON(null),
    CHEVRON(new ChevronOrdinaryRenderer(false, 1.0)),
    CHEVRON_INVERTED(new ChevronOrdinaryRenderer(true, 1.0)),
    CHIEF(new ChiefOrdinaryRenderer()),
    CROSS(new CrossOrdinaryRenderer()),
    FESS(null),
    FRET(new FretOrdinaryRenderer()),
    ORLE(null),
    PALE(null),
    QUARTER(null),
    TRESSURE(null);

    @Getter
    private final OrdinaryRenderer renderer;

    public String getLabel()
    {
        return Character.toUpperCase(name().toLowerCase().charAt(0)) + name().toLowerCase().substring(1).replace("_", " ");
    }
}
