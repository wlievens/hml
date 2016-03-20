package heraldry.util;

import java.util.List;

public class CollectionUtils
{
    public static final <T> T single(List<T> list)
    {
        if (list.size() != 1)
        {
            throw new IllegalArgumentException("List has " + list.size() + " elements");
        }
        return list.get(0);
    }
}
