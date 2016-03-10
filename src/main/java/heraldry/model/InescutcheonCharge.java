package heraldry.model;

import com.kitfox.svg.SVGDiagram;
import heraldry.render.Box;
import heraldry.render.Painter;
import heraldry.render.Point;
import heraldry.render.RenderContour;
import heraldry.render.RenderShape;
import heraldry.util.SvgUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class InescutcheonCharge extends Charge
{
    private final ChargedBackgroundModel model;

    @Override
    public String generateBlazon(BlazonContext context)
    {
        return "An inescutcheon " + model.generateBlazon(context.create());
    }

    @Override
    public boolean isSquareShapePreferred()
    {
        return false;
    }

    @Override
    public Collection<RenderShape> render(RenderContour contour, Painter painter)
    {
        SVGDiagram diagram = SvgUtils.loadSvg("/shapes/heater-shield.svg");
        double scale = 0.37;
        Box bounds = contour.getBounds();
        Point center = bounds.getFessPoint();
        AffineTransform transform = AffineTransform.getTranslateInstance(center.getX() - 0.5 * scale * bounds.getWidth(), center.getY() - 0.5 * scale * bounds.getHeight());
        transform.scale(scale, scale);
        RenderContour subContour = new RenderContour(SvgUtils.convertSvgElementToPath((com.kitfox.svg.Path)diagram.getElement("contour"), transform));
        List<RenderShape> shapes = new ArrayList<>();
        shapes.addAll(model.render(subContour, painter));
        shapes.add(new RenderShape(subContour.getSteps(), null, painter.getOuterBorderColor()));
        return shapes;
    }
}
