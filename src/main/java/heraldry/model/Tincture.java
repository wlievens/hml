package heraldry.model;

public enum Tincture
{
    OR,
    ARGENT,
    AZURE,
    GULES,
    PURPURE,
    SABLE,
    VERT,
    ERMINE,
    ERMINES,
    ERMINOIS,
    PEAN,;// VAIR;

    public boolean isMetal()
    {
        return this == OR || this == ARGENT;
    }

    public boolean isColor()
    {
        return this == AZURE || this == GULES || this == PURPURE || this == SABLE || this == VERT;
    }

    public boolean isFur()
    {
        return this == ERMINE || this == ERMINES || this == ERMINOIS || this == PEAN;// || this == VAIR;
    }

    public String getLabel()
    {
        return Character.toUpperCase(name().toLowerCase().charAt(0)) + name().toLowerCase().substring(1);
    }
}
