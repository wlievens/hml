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
    BEND(new BendOrdinaryRenderer(false, 1.0)),
    BEND_SINISTER(new BendOrdinaryRenderer(true, 1.0)),
    BENDLET(new BendOrdinaryRenderer(false, 0.5)),
    BENDLET_SINISTER(new BendOrdinaryRenderer(true, 0.5)),
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
