package heraldry.render.paint;

import heraldry.render.paint.Paint;
import lombok.Value;

@Value
public final class Color implements Paint
{
    private final double red;
    private final double green;
    private final double blue;
}
