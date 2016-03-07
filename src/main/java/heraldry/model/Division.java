package heraldry.model;

import heraldry.render.division.DivisionRenderer;
import heraldry.render.division.PaleDivisionRenderer;
import heraldry.render.division.QuarterlyDivisionRenderer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Division
{
    FESS(null),
    PALE(new PaleDivisionRenderer()),
    QUARTERLY(new QuarterlyDivisionRenderer());

    @Getter
    private final DivisionRenderer renderer;

    public String getLabel()
    {
        return Character.toUpperCase(name().toLowerCase().charAt(0)) + name().toLowerCase().substring(1);
    }
}
