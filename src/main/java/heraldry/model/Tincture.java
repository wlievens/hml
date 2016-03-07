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
    VAIR;

    public String getLabel()
    {
        return Character.toUpperCase(name().toLowerCase().charAt(0)) + name().toLowerCase().substring(1);
    }
}
