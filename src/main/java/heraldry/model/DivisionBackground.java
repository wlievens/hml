package heraldry.model;

import heraldry.render.Painter;
import heraldry.render.RenderContour;
import heraldry.render.RenderShape;
import heraldry.render.division.DivisionRenderer;
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
public class DivisionBackground extends Background
{
    private final Division division;
    private final List<DivisionPart> parts;

    @Override
    public String generateBlazon(BlazonContext context)
    {
        List<String> list = new ArrayList<>();
        switch (division)
        {
            case FESS:
            case PALE:
                list.add("Party");
                list.add("per");
                list.add(division.getLabel().toLowerCase());
                break;
            default:
                list.add(division.getLabel());
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
    public Collection<RenderShape> render(RenderContour contour, Painter painter)
    {
        DivisionRenderer renderer = division.getRenderer();
        if (renderer == null)
        {
            throw new IllegalStateException(String.format("No renderer implemented for division '%s'", division));
        }
        List<RenderContour> divisionContours = renderer.render(contour, painter);
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
