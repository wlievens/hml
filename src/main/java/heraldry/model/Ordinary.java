package heraldry.model;

import heraldry.render.ordinary.BendOrdinaryRenderer;
import heraldry.render.ordinary.ChevronOrdinaryRenderer;
import heraldry.render.ordinary.ChiefOrdinaryRenderer;
import heraldry.render.ordinary.CrossOrdinaryRenderer;
import heraldry.render.ordinary.FessOrdinaryRenderer;
import heraldry.render.ordinary.FretOrdinaryRenderer;
import heraldry.render.ordinary.GyronOrdinaryRenderer;
import heraldry.render.ordinary.OrdinaryRenderer;
import heraldry.render.ordinary.PaleOrdinaryRenderer;
import heraldry.render.ordinary.QuarterOrdinaryRenderer;
import heraldry.render.ordinary.SaltireOrdinaryRenderer;
import heraldry.util.EnumUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Ordinary
{
    BAR(new FessOrdinaryRenderer(0.5)),
    BEND(new BendOrdinaryRenderer(false, 1.0)),
    BEND_SINISTER(new BendOrdinaryRenderer(true, 1.0)),
    BENDLET(new BendOrdinaryRenderer(false, 0.5)),
    BENDLET_SINISTER(new BendOrdinaryRenderer(true, 0.5)),
    BORDURE(null),
    CANTON(new QuarterOrdinaryRenderer(1 / 3.0)),
    CHEVRON(new ChevronOrdinaryRenderer(false, 1.0)),
    CHEVRON_INVERTED(new ChevronOrdinaryRenderer(true, 1.0)),
    CHIEF(new ChiefOrdinaryRenderer()),
    CROSS(new CrossOrdinaryRenderer()),
    FESS(new FessOrdinaryRenderer(1.0)),
    FRET(new FretOrdinaryRenderer()),
    GYRON(new GyronOrdinaryRenderer(1 / 3.0, false, false)),
    GYRON_SINISTER(new GyronOrdinaryRenderer(1 / 3.0, true, false)),
    ORLE(null),
    SALTIRE(new SaltireOrdinaryRenderer()),
    PALE(new PaleOrdinaryRenderer(1.0)),
    PALL(null),
    PALLET(new PaleOrdinaryRenderer(0.5)),
    QUARTER(new QuarterOrdinaryRenderer(0.5)),
    TRESSURE(null);

    @Getter
    private final OrdinaryRenderer renderer;

    public String getLabel()
    {
        return EnumUtils.getLabel(this);
    }
}
