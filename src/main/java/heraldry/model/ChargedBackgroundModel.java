package heraldry.model;

import heraldry.render.Painter;
import heraldry.render.RenderContour;
import heraldry.render.RenderShape;
import heraldry.render.Renderable;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@ToString
public class ChargedBackgroundModel implements Renderable
{
    @NonNull
    private Background background;
    private List<Charge> charges;

    public String generateBlazon(BlazonContext context)
    {
        StringBuilder builder = new StringBuilder();
        builder.append(background.generateBlazon(context));
        if (background instanceof FieldBackground)
        {
            context = context.withTincture(((FieldBackground)background).getTincture());
        }
        for (int n = 0; n < charges.size(); ++n)
        {
            Charge charge = charges.get(n);
            if (n > 0)
            {
                builder.append(",");
            }
            builder.append(" ");
            builder.append(charge.generateBlazon(context));
        }
        return builder.toString();
    }

    @Override
    public Collection<RenderShape> render(RenderContour contour, Painter painter)
    {
        List<RenderShape> list = new ArrayList<>();
        list.addAll(background.render(contour, painter));
        charges.stream().map(charge -> charge.render(contour, painter)).forEach(list::addAll);
        return list;
    }
}
