package heraldry.model;

import heraldry.render.Painter;
import heraldry.render.RenderContour;
import heraldry.render.RenderShape;
import heraldry.render.ordinary.OrdinaryRenderer;
import heraldry.util.StringUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class OrdinaryCharge extends Charge
{
    private final Ordinary ordinary;
    private final Line line;
    private final Tincture tincture;
    private final List<Charge> charges;

    @Override
    public String generateBlazon(BlazonContext context)
    {
        List<String> list = new ArrayList<>();
        String ordinaryLabel = ordinary.getLabel().toLowerCase();
        if (charges.size() > 0)
        {
            list.add("on");
        }
        list.add(StringUtils.getArticle(ordinaryLabel));
        list.add(ordinaryLabel);
        if (line != Line.PLAIN)
        {
            list.add(line.getLabel().toLowerCase());
        }
        list.add(tincture.getLabel().toLowerCase());
        charges.forEach(charge -> list.add(charge.generateBlazon(context)));
        return StringUtils.sentence(list);
    }

    @Override
    public Collection<RenderShape> render(RenderContour contour, Painter painter)
    {
        List<RenderShape> list = new ArrayList<>();
        OrdinaryRenderer renderer = ordinary.getRenderer();
        if (renderer == null)
        {
            throw new IllegalStateException(String.format("No renderer implemented for ordinary '%s'", ordinary));
        }
        List<RenderShape> shapes = contour.clipAll(renderer.render(contour.getBounds(), tincture, line, painter));
        list.addAll(shapes);
        for (Charge charge : charges)
        {
            // TODO Use the first shape as contour for now
            list.addAll(charge.render(new RenderContour(shapes.get(0).getSteps()), painter));
        }
        return list;
    }
}
