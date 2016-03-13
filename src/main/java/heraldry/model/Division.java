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
    FESS(new FessDivisionRenderer()),
    PALE(new PaleDivisionRenderer()),
    QUARTERLY(new QuarterlyDivisionRenderer());

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
