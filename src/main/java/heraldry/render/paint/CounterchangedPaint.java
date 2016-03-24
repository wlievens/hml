package heraldry.render.paint;

import heraldry.model.Tincture;
import lombok.Value;

@Value
public final class CounterchangedPaint implements Paint
{
    private final Tincture firstTincture;
    private final Tincture secondTincture;
}
