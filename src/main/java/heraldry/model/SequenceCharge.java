package heraldry.model;

import heraldry.render.Box;
import heraldry.render.paint.Color;
import heraldry.render.Painter;
import heraldry.render.RenderContour;
import heraldry.render.RenderShape;
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
    private static final boolean DEBUG = false;

    private final List<Charge> charges;

    @Override
    public boolean isRepeatSupported()
    {
        return false;
    }

    @Override
    public String generateBlazon(BlazonContext context)
    {
        return "TODO";
    }

    @Override
    public boolean isSquareShapePreferred()
    {
        return charges.size() == 1 && charges.get(0).isSquareShapePreferred();
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
                List<Box> boxes = new ArrayList<>();
                int squares = (int)charges.stream().filter(Charge::isSquareShapePreferred).count();
                if (squares * height <= width)
                {
                    double x = bounds.getX1();
                    for (Charge charge : charges)
                    {
                        double chargeWidth;
                        if (charge.isSquareShapePreferred())
                        {
                            chargeWidth = height;
                        }
                        else
                        {
                            chargeWidth = (width - height * squares) / (charges.size() - squares);
                        }
                        boxes.add(new Box(x, bounds.getY1(), x + chargeWidth, bounds.getY1() + height));
                        x += chargeWidth;
                    }
                }
                else
                {
                    throw new IllegalStateException("TODO");
                }

                for (int n = 0; n < charges.size(); ++n)
                {
                    Charge charge = charges.get(n);
                    Box box = boxes.get(n);
                    RenderContour child = new RenderContour(box.toPath());
                    list.addAll(charge.render(child, painter));
                    if (DEBUG)
                    {
                        list.add(new RenderShape(child.getSteps(), null, new Color(1, 0, 1), "sequence debug reticle"));
                    }
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
