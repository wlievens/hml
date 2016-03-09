package heraldry.model;

import heraldry.render.ordinary.BendOrdinaryRenderer;
import heraldry.render.ordinary.ChevronOrdinaryRenderer;
import heraldry.render.ordinary.ChiefOrdinaryRenderer;
import heraldry.render.ordinary.CrossOrdinaryRenderer;
import heraldry.render.ordinary.FretOrdinaryRenderer;
import heraldry.render.ordinary.OrdinaryRenderer;
import heraldry.render.ordinary.PaleOrdinaryRenderer;
import heraldry.render.ordinary.SaltireOrdinaryRenderer;
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
    SALTIRE(new SaltireOrdinaryRenderer()),
    PALE(new PaleOrdinaryRenderer(1.0)),
    PALLET(new PaleOrdinaryRenderer(0.5)),
    QUARTER(null),
    TRESSURE(null);

    @Getter
    private final OrdinaryRenderer renderer;

    public String getLabel()
    {
        return Character.toUpperCase(name().toLowerCase().charAt(0)) + name().toLowerCase().substring(1).replace("_", " ");
    }
}
