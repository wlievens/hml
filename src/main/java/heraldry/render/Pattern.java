package heraldry.render;

import heraldry.model.Tincture;
import lombok.Value;

@Value
public final class Pattern implements Paint
{
    private final Tincture tincture;
}
