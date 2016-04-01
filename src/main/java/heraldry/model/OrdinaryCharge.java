package heraldry.model;

import heraldry.render.Box;
import heraldry.render.Painter;
import heraldry.render.RenderContour;
import heraldry.render.RenderShape;
import heraldry.render.Surface;
import heraldry.render.ordinary.OrdinaryRenderer;
import heraldry.render.paint.Color;
import heraldry.render.path.Path;
import heraldry.util.CollectionUtils;
import heraldry.util.GeometryUtils;
import heraldry.util.StringUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
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

    public OrdinaryCharge(Ordinary ordinary, Tincture tincture)
    {
        this(ordinary, Line.PLAIN, new FieldBackground(tincture), new ArrayList<>());
    }

    public OrdinaryCharge(Ordinary ordinary, Line line, Tincture tincture, Charge... charges)
    {
        this(ordinary, line, new FieldBackground(tincture), Arrays.asList(charges));
    }

    public OrdinaryCharge(Ordinary ordinary, Tincture tincture, Charge... charges)
    {
        this(ordinary, Line.PLAIN, tincture, charges);
    }

    @Override
    public boolean isRepeatSupported()
    {
        return ordinary.isRepeatSupported();
    }

    @Override
    public String generateBlazon(BlazonContext context)
    {
        List<String> list = new ArrayList<>();
        String ordinaryLabel = ordinary.getLabel().toLowerCase();
        if (context.isPlural())
        {
            ordinaryLabel = StringUtils.getPlural(ordinaryLabel);
        }
        if (charges.size() > 0)
        {
            list.add("on");
        }
        if (!ordinary.isNamePlural() && !context.isPlural())
        {
            list.add(StringUtils.getArticle(ordinaryLabel));
        }
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
            Path rectangle = GeometryUtils.rectangle(bounds.lerpX(0.2), bounds.lerpY(0.2), bounds.lerpX(0.8), bounds.lerpY(0.8));
            return Collections.singleton(rectangle.render(null, new Color(1, 0, 1), "fall-back for missing ordinary renderer"));
        }
        RenderContour chargeContour = renderer.render(contour, line, painter);
        List<RenderShape> list = new ArrayList<>();
        Surface clippedSurface = contour.clip(chargeContour.getSurface());
        Path spine = chargeContour.getSpine() == null ? null : CollectionUtils.single(contour.clip(chargeContour.getSpine()));
        RenderContour clippedContour = new RenderContour(clippedSurface, spine);
        list.addAll(background.render(clippedContour, painter));
        for (Charge charge : charges)
        {
            list.addAll(charge.render(clippedContour, painter));
        }
        return list;
    }
}
