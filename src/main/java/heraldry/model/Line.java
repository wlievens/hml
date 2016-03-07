package heraldry.model;

public enum Line
{
    PLAIN,
    WAVY,
    INDENTED,
    INVECTED,
    EMBATTLED,
    ENGRAILED,
    DOVETAILED,
    NEBULY;

    public String getLabel()
    {
        return Character.toUpperCase(name().toLowerCase().charAt(0)) + name().toLowerCase().substring(1);
    }
}
