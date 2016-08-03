package heraldry.model;

import heraldry.render.division.BendDivisionRenderer;
import heraldry.render.division.ChevronDivisionRenderer;
import heraldry.render.division.DivisionRenderer;
import heraldry.render.division.FessDivisionRenderer;
import heraldry.render.division.PaleDivisionRenderer;
import heraldry.render.division.PallDivisionRenderer;
import heraldry.render.division.QuarterlyDivisionRenderer;
import heraldry.render.division.SaltireDivisionRenderer;
import heraldry.util.EnumUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Division
{
    BEND(new BendDivisionRenderer(false)),
    BEND_SINISTER(new BendDivisionRenderer(true)),
    CHAUSSE(null),
    CHEVRON(new ChevronDivisionRenderer(false)),
    CHEVRON_INVERTED(new ChevronDivisionRenderer(true)),
    FESS(new FessDivisionRenderer()),
    PALE(new PaleDivisionRenderer()),
    PALL(new PallDivisionRenderer(false)),
    PALL_INVERTED(new PallDivisionRenderer(true)),
    QUARTERLY(new QuarterlyDivisionRenderer()),
    SALTIRE(new SaltireDivisionRenderer());

    @Getter
    private final DivisionRenderer renderer;

    public int getMaxPositions()
    {
        switch (this)
        {
            case SALTIRE:
            case QUARTERLY:
            {
                return 4;
            }
            case PALL:
            case PALL_INVERTED:
            {
                return 3;
            }
            default:
            {
                return 2;
            }
        }
    }

    public String getLabel()
    {
        return EnumUtils.getLabel(this);
    }
}
