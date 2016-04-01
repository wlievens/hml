package heraldry.render;

import heraldry.render.paint.Color;
import heraldry.render.paint.Paint;
import heraldry.render.path.Path;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Wither;

@Getter
@RequiredArgsConstructor(staticName = "create")
public final class RenderShape
{
    @NonNull
    private final Surface surface;

    @Wither
    private final Paint fillPaint;

    @Wither
    private final Color borderColor;

    @Wither
    private final String label;

    public static RenderShape create(Path path, Paint fillPaint, Color borderColor, String label)
    {
        return create(new Surface(path), fillPaint, borderColor, label);
    }

    @Override
    public String toString()
    {
        return String.format("Shape(\"%s\" %s)", label, surface);
    }
}
