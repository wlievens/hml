package heraldry.render;

import com.google.common.base.Joiner;
import heraldry.render.paint.Color;
import heraldry.render.paint.Paint;
import heraldry.render.path.CubicPathStep;
import heraldry.render.path.LinePathStep;
import heraldry.render.path.Path;
import heraldry.render.path.PathStep;
import heraldry.render.path.QuadraticPathStep;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.Collection;
import java.util.List;

@Value
@RequiredArgsConstructor
public final class Rendering
{
    private static final String SVG_ID_CONTOUR = "contour";
    private static final String SVG_ID_LIGHTING = "lighting";

    private final RenderContour contour;
    private final Collection<RenderShape> renderShapes;

    public static String buildPath(Surface surface, double offsetX, double offsetY)
    {
        String positive = Joiner.on(" ").join(surface.getPositives().stream().map(path -> buildPath(path, offsetX, offsetY)).iterator());
        String negative = Joiner.on(" ").join(surface.getNegatives().stream().map(path -> buildPath(path, offsetX, offsetY)).iterator());
        return (positive + " " + negative).trim();
    }

    public static String buildPath(Path path, double offsetX, double offsetY)
    {
        StringBuilder builder = new StringBuilder();
        List<PathStep> steps = path.getSteps();
        for (PathStep step : steps)
        {
            if (step instanceof LinePathStep)
            {
                LinePathStep line = (LinePathStep)step;
                if (builder.length() == 0)
                {
                    builder.append(String.format("M %s,%s ",
                            offsetX + line.getX1(), offsetY + line.getY1()
                    ));
                }
                builder.append(String.format("L %s,%s ",
                        offsetX + line.getX2(), offsetY + line.getY2()
                ));
            }
            else if (step instanceof QuadraticPathStep)
            {
                QuadraticPathStep quadratic = (QuadraticPathStep)step;
                if (builder.length() == 0)
                {
                    builder.append(String.format("M %s,%s ",
                            offsetX + quadratic.getX1(), offsetY + quadratic.getY1()
                    ));
                }
                builder.append(String.format("Q %s,%s %s,%s ",
                        offsetX + quadratic.getX2(), offsetY + quadratic.getY2(),
                        offsetX + quadratic.getX3(), offsetY + quadratic.getY3()
                ));
            }
            else if (step instanceof CubicPathStep)
            {
                CubicPathStep cubic = (CubicPathStep)step;
                if (builder.length() == 0)
                {
                    builder.append(String.format("M %s,%s ",
                            offsetX + cubic.getX1(), offsetY + cubic.getY1()
                    ));
                }
                builder.append(String.format("C %s,%s %s,%s %s,%s ",
                        offsetX + cubic.getX2(), offsetY + cubic.getY2(),
                        offsetX + cubic.getX3(), offsetY + cubic.getY3(),
                        offsetX + cubic.getX4(), offsetY + cubic.getY4()
                ));
            }
            else
            {
                throw new IllegalStateException();
            }
        }
        builder.append("Z");
        return builder.toString();
    }

    private static int to255(double value)
    {
        return (int)Math.round(255 * value);
    }

    public Document toSvg(double margin)
    {
        try
        {
            Box bounds = contour.getBounds();

            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element xmlSvg = document.createElement("svg");
            xmlSvg.setAttribute("xmlns", "http://www.w3.org/2000/svg");
            double fullWidth = 2 * margin + bounds.getWidth();
            double fullHeight = 2 * margin + bounds.getHeight();
            xmlSvg.setAttribute("width", String.valueOf(fullWidth));
            xmlSvg.setAttribute("height", String.valueOf(fullHeight));

            Element xmlDefs = document.createElement("defs");
            {
                Surface contourSurface = this.contour.getSurface();
                if (!contourSurface.isSingular())
                {
                    throw new IllegalStateException();
                }

                Element xmlClipPath = document.createElement("clipPath");
                {
                    xmlClipPath.setAttribute("id", SVG_ID_CONTOUR);
                    Element xmlPath = document.createElement("path");
                    xmlPath.setAttribute("d", buildPath(contourSurface.getPositives().get(0), 0, 0));
                    xmlClipPath.appendChild(xmlPath);
                    xmlDefs.appendChild(xmlClipPath);
                }

                Element xmlRadialGradient = document.createElement("radialGradient");
                {
                    xmlRadialGradient.setAttribute("id", SVG_ID_LIGHTING);
                    Element xmlStop1 = document.createElement("stop");
                    {
                        xmlStop1.setAttribute("offset", "25%");
                        xmlStop1.setAttribute("stop-color", "white");
                        xmlStop1.setAttribute("stop-opacity", "35%");
                        xmlRadialGradient.appendChild(xmlStop1);
                    }
                    Element xmlStop2 = document.createElement("stop");
                    {
                        xmlStop2.setAttribute("offset", "100%");
                        xmlStop2.setAttribute("stop-color", "white");
                        xmlStop2.setAttribute("stop-opacity", "0%");
                        xmlRadialGradient.appendChild(xmlStop2);
                    }
                    xmlDefs.appendChild(xmlRadialGradient);
                }
                xmlSvg.appendChild(xmlDefs);
            }

            Element xmlG = toSvgElement(document);
            xmlG.setAttribute("transform", String.format("translate(%s,%s)", margin, margin));
            xmlSvg.appendChild(xmlG);

            Element xmlRect = document.createElement("rect");
            xmlRect.setAttribute("x", String.valueOf(bounds.getX1()));
            xmlRect.setAttribute("y", String.valueOf(bounds.getY1()));
            xmlRect.setAttribute("width", String.valueOf(fullWidth));
            xmlRect.setAttribute("height", String.valueOf(fullHeight));
            xmlRect.setAttribute("fill", String.format("url(#%s)", SVG_ID_LIGHTING));
            xmlRect.setAttribute("clip-path", String.format("url(#%s)", SVG_ID_CONTOUR));
            //xmlSvg.appendChild(xmlRect);

            document.appendChild(xmlSvg);
            return document;
        }
        catch (ParserConfigurationException e)
        {
            throw new IllegalStateException(e);
        }
    }

    public Element toSvgElement(Document document)
    {
        Element svgGroup = document.createElement("g");
        renderShapes.forEach(renderShape -> {
            Surface surface = renderShape.getSurface().normalize();
            surface.getPositives().forEach(path -> {
                Element svgPath = document.createElement("path");
                svgPath.setAttribute("d", buildPath(surface, 0, 0));
                svgPath.setAttribute("style", String.format("fill-rule: evenodd; fill: %s; stroke-width: 1px; stroke: %s;", getSvgColor(renderShape.getFillPaint()), getSvgColor(renderShape.getBorderColor())));
                svgPath.setAttribute("clip-path", String.format("url(#%s)", SVG_ID_CONTOUR));
                svgPath.setAttribute("comment", renderShape.getLabel());
                svgGroup.appendChild(svgPath);
            });
        });
        return svgGroup;
    }

    private String getSvgColor(Paint paint)
    {
        if (paint == null)
        {
            return "none";
        }
        if (paint instanceof Color)
        {
            return formatSvgColor((Color)paint);
        }
        throw new IllegalStateException("Unhandled paint: " + paint);
    }

    private String formatSvgColor(@NonNull Color color)
    {
        double red = color.getRed();
        double green = color.getGreen();
        double blue = color.getBlue();
        return String.format("rgb(%s, %s, %s)", to255(red), to255(green), to255(blue));
    }
}
