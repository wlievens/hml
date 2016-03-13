package heraldry.model;

import heraldry.render.Painter;
import heraldry.render.RenderContour;
import heraldry.render.RenderShape;
import heraldry.render.Renderable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
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
        positions = Collections.singletonList(position);
        model = new ChargedBackgroundModel();
        model.setBackground(new FieldBackground(tincture));
        model.setCharges(new ArrayList<>());
    }

    @Override
    public Collection<RenderShape> render(RenderContour contour, Painter painter)
    {
        return model.render(contour, painter);
    }
}
