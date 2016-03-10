package heraldry.util;

import com.kitfox.svg.SVGCache;
import com.kitfox.svg.SVGDiagram;
import heraldry.render.PathStep;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.net.URISyntaxException;
import java.util.List;

public class SvgUtils
{
    public static List<PathStep> convertSvgElementToPath(com.kitfox.svg.Path svgPath)
    {
        return convertSvgElementToPath(svgPath, null);
    }

    public static List<PathStep> convertSvgElementToPath(com.kitfox.svg.Path svgPath, AffineTransform transform)
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

    public static SVGDiagram loadSvg(String resource) throws URISyntaxException
    {
        return SVGCache.getSVGUniverse().getDiagram(SvgUtils.class.getResource(resource).toURI());
    }
}
