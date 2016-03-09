package heraldry.model;

import heraldry.render.Box;
import heraldry.render.Painter;
import heraldry.render.RenderContour;
import heraldry.render.RenderShape;
import heraldry.util.GeometryUtils;
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
public class SequenceCharge extends Charge
{
    private final List<Charge> charges;

    @Override
    public String generateBlazon(BlazonContext context)
    {
        return "TODO";
    }

    @Override
    public Collection<RenderShape> render(RenderContour contour, Painter painter)
    {
        List<Charge> charges = flattenChargeList();
        if (contour.isRectangle())
        {
            Box bounds = contour.getBounds();
            List<RenderShape> list = new ArrayList<>();
            double width = bounds.getWidth();
            double height = bounds.getHeight();
            if (width > height)
            {
                for (int n = 0; n < charges.size(); ++n)
                {
                    Charge charge = charges.get(n);
                    double x1 = bounds.getX1() + (n + 0) * width / charges.size();
                    double x2 = bounds.getX1() + (n + 1) * width / charges.size();
                    RenderContour child = new RenderContour(GeometryUtils.rectangle(x1, bounds.getY1(), x2, bounds.getY2()));
                    list.addAll(charge.render(child, painter));
                }
            }
            else
            {
                throw new IllegalStateException();
            }
            return list;
        }
        throw new IllegalStateException();
    }

    private List<Charge> flattenChargeList()
    {
        List<Charge> charges = new ArrayList<>();
        this.charges.forEach(charge ->
        {
            if (charge instanceof RepeatCharge)
            {
                RepeatCharge repeat = (RepeatCharge)charge;
                for (int n = 0; n < repeat.getNumber(); ++n)
                {
                    charges.add(repeat.getCharge());
                }
            }
            else
            {
                charges.add(charge);
            }
        });
        return charges;
    }
}
