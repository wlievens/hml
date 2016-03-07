package heraldry.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Wither;

@Getter
@Wither
@RequiredArgsConstructor
public class BlazonContext
{
    private final Tincture tincture;
    private final boolean plural;

    public static BlazonContext create()
    {
        return new BlazonContext(null, false);
    }
}
