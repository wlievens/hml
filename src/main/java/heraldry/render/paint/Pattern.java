package heraldry.render.paint;

import heraldry.render.paint.Color;
import heraldry.render.paint.Paint;
import lombok.Value;

@Value
public final class Pattern implements Paint
{
    private final String figure;
    private final Color background;
    private final Color foreground;
}
