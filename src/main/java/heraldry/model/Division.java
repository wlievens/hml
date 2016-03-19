package heraldry.model;

import heraldry.render.division.DivisionRenderer;
import heraldry.render.division.FessDivisionRenderer;
import heraldry.render.division.PaleDivisionRenderer;
import heraldry.render.division.QuarterlyDivisionRenderer;
import heraldry.util.EnumUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Division
{
    BEND(null),
    BEND_SINISTER(null),
    CHAUSSÉ(null),
    CHEVRON(null),
    CHEVRON_INVERTED(null),
    CROSS(null),
    FESS(new FessDivisionRenderer()),
    PALE(new PaleDivisionRenderer()),
    PALL(null),
    PALL_INVERTED(null),
    QUARTERLY(new QuarterlyDivisionRenderer()),
    SALTIRE(null);

    @Getter
    private final DivisionRenderer renderer;

    public int getMaxPositions()
    {
        switch (this)
        {
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
