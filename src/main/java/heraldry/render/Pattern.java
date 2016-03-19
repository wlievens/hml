package heraldry.render;

import heraldry.model.Tincture;
import lombok.Value;

@Value
public final class Pattern implements Paint
{
    private final String figure;
    private final Color background;
    private final Color foreground;
}
