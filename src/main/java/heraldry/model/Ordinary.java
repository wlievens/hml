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
    ANNULET(null),
    BAR(new FessOrdinaryRenderer(0.5)),
    BARRULET(new FessOrdinaryRenderer(0.25)),
    BATON(null),
    BATON_SINISTER(null),
    BEND(new BendOrdinaryRenderer(false, 1.0)),
    BEND_SINISTER(new BendOrdinaryRenderer(true, 1.0)),
    BENDLET(new BendOrdinaryRenderer(false, 0.5)),
    BENDLET_SINISTER(new BendOrdinaryRenderer(true, 0.5)),
    BILLET(null),
    BORDURE(null),
    CANTON(new QuarterOrdinaryRenderer(1 / 3.0)),
    CHEVRON(new ChevronOrdinaryRenderer(false, 1.0)),
    CHEVRON_INVERTED(new ChevronOrdinaryRenderer(true, 1.0)),
    CHEVRONEL(new ChevronOrdinaryRenderer(false, 0.25)),
    CHEVRONEL_INVERTED(new ChevronOrdinaryRenderer(true, 0.25)),
    CHIEF(new ChiefOrdinaryRenderer(1.0)),
    COMBLE(new ChiefOrdinaryRenderer(1.0)),
    CROSS(new CrossOrdinaryRenderer(1.0)),
    CROSS_FILLET(new CrossOrdinaryRenderer(0.5)),
    ENDORSE(new PaleOrdinaryRenderer(0.25)),
    FESS(new FessOrdinaryRenderer(1.0)),
    FLAUNCHES(null),
    FRET(new FretOrdinaryRenderer()),
    FUSIL(null),
    GORE(null),
    GYRON(new GyronOrdinaryRenderer(1 / 3.0, false, false)),
    GYRON_SINISTER(new GyronOrdinaryRenderer(1 / 3.0, true, false)),
    HAMADE(null),
    LOZENGE(null),
    MASCLE(null),
    ORLE(null),
    PALE(new PaleOrdinaryRenderer(1.0)),
    PALL(null),
    PALLET(new PaleOrdinaryRenderer(0.5)),
    PILE(null),
    QUARTER(new QuarterOrdinaryRenderer(0.5)),
    RIBBON(new BendOrdinaryRenderer(false, 0.25)),
    RIBBON_SINISTER(new BendOrdinaryRenderer(true, 0.25)),
    ROUNDEL(null),
    SALTIRE(new SaltireOrdinaryRenderer(1.0)),
    SALTIRE_FILLET(new SaltireOrdinaryRenderer(0.5)),
    SALTOREL(null),
    SHAKEFORK(null),
    TRESSURE(null);
    
    @Getter
    private final OrdinaryRenderer renderer;

    public String getLabel()
    {
        return EnumUtils.getLabel(this);
    }
}
