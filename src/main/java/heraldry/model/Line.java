package heraldry.model;

import heraldry.util.EnumUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Line
{
    PLAIN(1.0),
    DOVETAILED(1.0),
    EMBATTLED(1.0),
    ENGRAILED(1.0),
    INDENTED(1.0),
    INVECTED(0.9),
    NEBULY(0.75),
    WAVY(1.0);

    @Getter
    private final double scaleFactor;

    public String getLabel()
    {
        return EnumUtils.getLabel(this);
    }
}