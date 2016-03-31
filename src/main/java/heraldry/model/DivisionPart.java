package heraldry.model;

import heraldry.render.Painter;
import heraldry.render.RenderContour;
import heraldry.render.RenderShape;
import heraldry.render.Renderable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class DivisionPart implements Renderable
{
    private final List<Integer> positions;
    private final ChargedBackgroundModel model;

    public DivisionPart(int position, Tincture tincture)
    {
        this(Collections.singletonList(position), new ChargedBackgroundModel(tincture));
    }

    public DivisionPart(int position, Tincture tincture, Charge... charges)
    {
        this(Collections.singletonList(position), new ChargedBackgroundModel(tincture, charges));
    }

    @Override
    public Collection<RenderShape> render(RenderContour contour, Painter painter)
    {
        return model.render(contour, painter);
    }
}
