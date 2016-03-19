package heraldry.render;

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
    private final RenderContour contour;
    private final Collection<RenderShape> paths;

    public static String buildPath(List<PathStep> steps, double offsetX, double offsetY)
    {
        StringBuilder builder = new StringBuilder();
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
                Element xmlClipPath = document.createElement("clipPath");
                {
                    xmlClipPath.setAttribute("id", "contour");
                    Element xmlPath = document.createElement("path");
                    xmlPath.setAttribute("d", buildPath(contour.getSteps(), margin, margin));
                    xmlClipPath.appendChild(xmlPath);
                    xmlDefs.appendChild(xmlClipPath);
                }

                Element xmlRadialGradient = document.createElement("radialGradient");
                {
                    xmlRadialGradient.setAttribute("id", "lighting");
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
            xmlRect.setAttribute("fill", "url(#lighting)");
            xmlRect.setAttribute("clip-path", "url(#contour)");
            xmlSvg.appendChild(xmlRect);

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
        for (RenderShape path : paths)
        {
            Element svgPath = document.createElement("path");
            svgPath.setAttribute("d", buildPath(path.getSteps(), 0, 0));
            svgPath.setAttribute("style", String.format("fill: %s; stroke-width: 1px; stroke: %s;", getSvgColor(path.getFillPaint()), getSvgColor(path.getBorderColor())));
            svgGroup.appendChild(svgPath);
        }
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
