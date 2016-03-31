package heraldry.model;

import heraldry.render.Painter;
import heraldry.render.RenderContour;
import heraldry.render.RenderShape;
import heraldry.render.Renderable;
import heraldry.render.path.Path;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class ChargedBackgroundModel implements Renderable
{
    @NonNull
    private final Background background;
    private final List<Charge> charges;

    public ChargedBackgroundModel(Tincture tincture)
    {
        this(new FieldBackground(tincture), Collections.emptyList());
    }

    public ChargedBackgroundModel(Tincture background, Charge... charges)
    {
        this(new FieldBackground(background), charges);
    }

    public ChargedBackgroundModel(Background background, Charge... charges)
    {
        this(background, Arrays.asList(charges));
    }

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
        Path spine = background.getSpine(contour);
        charges.stream().map(charge -> charge.render(new RenderContour(contour.getPath(), spine), painter)).forEach(list::addAll);
        return list;
    }
}
