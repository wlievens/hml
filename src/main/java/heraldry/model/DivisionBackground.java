package heraldry.model;

import heraldry.render.Box;
import heraldry.render.Painter;
import heraldry.render.RenderContour;
import heraldry.render.RenderShape;
import heraldry.render.division.DivisionRenderer;
import heraldry.render.paint.Color;
import heraldry.render.path.Path;
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
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

@Slf4j
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class DivisionBackground extends Background
{
    private final Division division;
    private final Line line;
    private final List<DivisionPart> parts;

    public DivisionBackground(Division division, DivisionPart... parts)
    {
        this(division, Line.PLAIN, Arrays.asList(parts));
    }

    public DivisionBackground(Division division, Tincture... tinctures)
    {
        this(division, Line.PLAIN, IntStream.range(0, tinctures.length).mapToObj(n -> new DivisionPart(n + 1, tinctures[n])).collect(toList()));
    }

    @Override
    public String generateBlazon(BlazonContext context)
    {
        List<String> list = new ArrayList<>();
        switch (division)
        {
            case QUARTERLY:
                list.add(division.getLabel());
                break;
            default:
                if (parts.size() == 2)
                {
                    list.add("Party");
                }
                else
                {
                    list.add("Tierced");
                }
                list.add("per");
                list.add(division.getLabel().toLowerCase());
        }
        if (line != Line.PLAIN)
        {
            list.add(line.getLabel().toLowerCase());
        }
        for (int n = 0; n < parts.size(); ++n)
        {
            if (n > 0)
            {
                if (n == parts.size() - 1)
                {
                    list.add("and");
                }
                else
                {
                    list.add(",");
                }
            }
            DivisionPart part = parts.get(n);
            if (parts.size() > 2)
            {
                for (int i = 0; i < part.getPositions().size(); ++i)
                {
                    if (i > 0)
                    {
                        if (i == part.getPositions().size() - 1)
                        {
                            list.add("and");
                        }
                        else
                        {
                            list.add(",");
                        }
                    }
                    list.add(StringUtils.getOrdinal(part.getPositions().get(i)).toLowerCase());
                }
            }
            list.add(part.getModel().generateBlazon(context).toLowerCase());
        }
        return StringUtils.sentence(list);
    }

    @Override
    public Path getSpine(RenderContour contour)
    {
        DivisionRenderer renderer = division.getRenderer();
        if (renderer == null)
        {
            log.warn("No renderer implemented for division '{}'", division);
            return null;
        }
        return renderer.getSpine(contour);
    }

    @Override
    public Collection<RenderShape> render(RenderContour contour, Painter painter)
    {
        DivisionRenderer renderer = division.getRenderer();
        if (renderer == null)
        {
            log.warn("No renderer implemented for division '{}'", division);
            Box bounds = contour.getBounds();
            Path rectangle = GeometryUtils.rectangle(bounds.lerpX(0.2), bounds.lerpY(0.2), bounds.lerpX(0.8), bounds.lerpY(0.8));
            return Collections.singleton(rectangle.render(null, new Color(1, 0, 1), "fall-back background for missing division"));
        }
        List<RenderContour> divisionContours = renderer.render(contour, line, painter);
        List<RenderShape> list = new ArrayList<>();
        for (int n = 0; n < divisionContours.size(); ++n)
        {
            int index = n + 1;
            RenderContour divisionContour = divisionContours.get(n);
            parts.stream()
                    .filter(part -> part.getPositions().contains(index))
                    .forEach(part -> list.addAll(part.render(divisionContour, painter)));
        }
        return list;
    }
}
