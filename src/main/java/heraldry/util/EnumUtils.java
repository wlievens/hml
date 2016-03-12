package heraldry.util;

public class EnumUtils
{
    public static String getLabel(Enum<?> value)
    {
        String name = value.name();
        return Character.toUpperCase(name.toLowerCase().charAt(0)) + name.toLowerCase().substring(1).replace("_", " ");
    }

    public static <T extends Enum<T>> void printAsXsd(T[] values)
    {
        for (T value : values)
        {
            System.out.println("<xs:enumeration value=\"" + value.name().toLowerCase().replace("_", "-") + "\"/>");
        }
    }
}
