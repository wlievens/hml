package heraldry.util;

import com.kitfox.svg.SVGCache;
import com.kitfox.svg.SVGDiagram;
import heraldry.render.PathStep;
import lombok.NonNull;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.net.URISyntaxException;
import java.util.List;

public class SvgUtils
{
    public static List<List<PathStep>> collect(SVGDiagram diagram, AffineTransform transform)
    {
        return GeometryUtils.convertPathIteratorToPathSteps(diagram.getRoot().getShape().getPathIterator(transform));
    }

    public static List<PathStep> convertSvgElementToPath(@NonNull com.kitfox.svg.Path svgPath)
    {
        return convertSvgElementToPath(svgPath, null);
    }

    public static List<PathStep> convertSvgElementToPath(@NonNull com.kitfox.svg.Path svgPath, AffineTransform transform)
    {
        Shape path2d = svgPath.getShape();
        PathIterator it = path2d.getPathIterator(transform);
        List<List<PathStep>> paths = GeometryUtils.convertPathIteratorToPathSteps(it);
        if (paths.size() == 1)
        {
            return paths.get(0);
        }
        throw new IllegalStateException();
    }

    public static SVGDiagram loadSvg(@NonNull String resource)
    {
        try
        {
            SVGDiagram diagram = SVGCache.getSVGUniverse().getDiagram(SvgUtils.class.getResource(resource).toURI());
            if (diagram == null)
            {
                throw new IllegalArgumentException(String.format("Could not find resource '%s'", resource));
            }
            return diagram;
        }
        catch (URISyntaxException e)
        {
            throw new IllegalStateException(e);
        }
    }
}
