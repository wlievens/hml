package heraldry.render;

import heraldry.render.paint.Color;
import heraldry.render.paint.Paint;
import heraldry.render.path.PathStep;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public final class RenderShape
{
    @NonNull
    private final List<PathStep> steps;
    private final Paint fillPaint;
    private final Color borderColor;
    private final String label;

    @Override
    public String toString()
    {
        return String.format("Shape(%s %s)", label, steps);
    }
}
