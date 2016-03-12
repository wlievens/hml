package heraldry.util;

public class EnumUtils
{
    public static String getLabel(Enum<?> value)
    {
        String name = value.name();
        return Character.toUpperCase(name.toLowerCase().charAt(0)) + name.toLowerCase().substring(1).replace("_", " ");
    }
}
