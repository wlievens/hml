package heraldry.model;

import heraldry.render.Box;
import heraldry.render.Color;
import heraldry.render.Painter;
import heraldry.render.RenderContour;
import heraldry.render.RenderShape;
import heraldry.util.GeometryUtils;
import heraldry.util.StringUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class MobileCharge extends Charge
{
    /**
     * The tincture of this charge. If the tincture is <code>null</code>, the charge will be rendered <em>proper</em>.
     */
    private final Tincture tincture;

    /**
     * The symbolic name of the figure.
     */
    private final String figure;

    @Override
    public String generateBlazon(BlazonContext context)
    {
        String figureLabel = context.isPlural() ? StringUtils.getPlural(this.figure) : this.figure;
        if (tincture == null)
        {
            return figureLabel + " proper";
        }
        if (tincture == context.getTincture())
        {
            return figureLabel + " of the field";
        }
        return (context.isPlural() ? "" : "a ") + figureLabel + " " + tincture.getLabel().toLowerCase();
    }

    @Override
    public boolean isSquareShapePreferred()
    {
        return false;
    }

    @Override
    public Collection<RenderShape> render(RenderContour contour, Painter painter)
    {
        // TODO implement mobile charges
        Box bounds = contour.getBounds();
        double x1 = bounds.getX1() + bounds.getWidth() * 0.2;
        double y1 = bounds.getY1() + bounds.getHeight() * 0.2;
        double x2 = bounds.getX1() + bounds.getWidth() * 0.8;
        double y2 = bounds.getY1() + bounds.getHeight() * 0.8;
        return Collections.singleton(new RenderShape(GeometryUtils.rectangle(x1, y1, x2, y2), null, new Color(0, 1, 1)));
    }
}
