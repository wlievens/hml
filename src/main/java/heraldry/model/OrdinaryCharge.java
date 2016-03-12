package heraldry.model;

import heraldry.render.Box;
import heraldry.render.Painter;
import heraldry.render.RenderContour;
import heraldry.render.RenderShape;
import heraldry.render.ordinary.OrdinaryRenderer;
import heraldry.util.GeometryUtils;
import heraldry.util.StringUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class OrdinaryCharge extends Charge
{
    private final Ordinary ordinary;
    private final Line line;
    private final Background background;
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
        list.add(background.generateBlazon(context).toLowerCase());
        charges.forEach(charge -> list.add(charge.generateBlazon(context)));
        return StringUtils.sentence(list);
    }

    @Override
    public boolean isSquareShapePreferred()
    {
        switch (ordinary)
        {
            case GYRON:
            case GYRON_SINISTER:
                return true;
            default:
                return false;
        }
    }

    @Override
    public Collection<RenderShape> render(RenderContour contour, Painter painter)
    {
        Box bounds = contour.getBounds();
        OrdinaryRenderer renderer = ordinary.getRenderer();
        if (renderer == null)
        {
            log.warn("No renderer implemented for ordinary '{}'", ordinary);
            return background.render(new RenderContour(GeometryUtils.rectangle(bounds.lerpX(0.2), bounds.lerpY(0.2), bounds.lerpX(0.8), bounds.lerpY(0.8))), painter);
        }
        List<RenderContour> contours = contour.clipContours(renderer.render(bounds, line, painter));
        List<RenderShape> list = new ArrayList<>();
        contours.forEach(child -> list.addAll(background.render(child, painter)));
        for (Charge charge : charges)
        {
            // TODO Use the first shape as contour for now
            list.addAll(charge.render(new RenderContour(contours.get(0).getSteps()), painter));
        }
        return list;
    }
}
