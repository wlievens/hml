package heraldry.model;

import heraldry.render.ordinary.BendOrdinaryRenderer;
import heraldry.render.ordinary.ChiefOrdinaryRenderer;
import heraldry.render.ordinary.CrossOrdinaryRenderer;
import heraldry.render.ordinary.FretOrdinaryRenderer;
import heraldry.render.ordinary.OrdinaryRenderer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Ordinary
{
    BAR(null),
    BEND(new BendOrdinaryRenderer(1.0)),
    BEND_SINISTER(null),
    BENDLET(new BendOrdinaryRenderer(0.5)),
    BENDLET_SINISTER(null),
    BORDURE(null),
    CANTON(null),
    CHEVRON(null),
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
        return Character.toUpperCase(name().toLowerCase().charAt(0)) + name().toLowerCase().substring(1);
    }
}
