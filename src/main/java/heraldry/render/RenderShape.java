package heraldry.render;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@RequiredArgsConstructor
public final class RenderShape
{
    private final List<PathStep> steps;
    private final Paint fillPaint;
    private final Color borderColor;
}
