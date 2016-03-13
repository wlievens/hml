package heraldry.util;

import com.google.common.base.Joiner;

import java.util.Iterator;

public class StringUtils
{
    public static String getArticle(String word)
    {
        if (word == null)
        {
            return null;
        }
        if (word.length() == 0)
        {
            return "";
        }
        char c = Character.toUpperCase(word.charAt(0));
        switch (c)
        {
            case 'A':
            case 'E':
            case 'H':
            case 'I':
            case 'O':
            case 'U':
                return "an";
            default:
                return "a";
        }
    }

    public static String getNumeral(int number)
    {
        switch (number)
        {
            case 1:
                return "One";
            case 2:
                return "Two";
            case 3:
                return "Three";
            case 4:
                return "Four";
            case 5:
                return "Five";
            case 6:
                return "Six";
            case 7:
                return "Seven";
            case 8:
                return "Eight";
            case 9:
                return "Nine";
            case 10:
                return "Ten";
            case 11:
                return "Eleven";
            case 12:
                return "Twelve";
            default:
                return String.valueOf(number);
        }
    }

    public static String getOrdinal(int number)
    {
        switch (number)
        {
            case 1:
                return "First";
            case 2:
                return "Second";
            case 3:
                return "Third";
            case 4:
                return "Fourth";
            case 5:
                return "Fifth";
            default:
                return String.valueOf(number) + "th";
        }
    }

    public static String getPlural(String word)
    {
        if ("fleur-de-lis".equals(word))
        {
            // TODO where do we define this?
            return "fleurs-de-lis";
        }
        return word + "s";
    }

    public static String join(Iterable<String> iterable, String delimiter)
    {
        return Joiner.on(" ").join(iterable);
    }

    public static String sentence(Iterable<String> list)
    {
        StringBuilder builder = new StringBuilder();
        int n = 0;
        Iterator<String> it = list.iterator();
        while (it.hasNext())
        {
            String word = it.next();
            if (n > 0 && !word.equals(","))
            {
                builder.append(" ");
            }
            builder.append(word);
            ++n;
        }
        return builder.toString();
    }
}
