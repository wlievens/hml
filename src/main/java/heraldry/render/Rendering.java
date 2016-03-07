package heraldry.render;

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

            Element xmlG = document.createElement("g");

            for (RenderShape path : paths)
            {
                Element xmlPath = document.createElement("path");
                xmlPath.setAttribute("d", buildPath(path.getSteps(), margin, margin));
                xmlPath.setAttribute("style", String.format("fill: %s; stroke-width: 1px; stroke: %s;", path.getFillPaint() == null ? "none" : toSvgColor(path.getFillPaint()), path.getBorderColor() == null ? "none" : toSvgColor(path.getBorderColor())));
                //xmlPath.setAttribute("clip-path", "url(#contour)");
                xmlG.appendChild(xmlPath);
            }
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

    private String buildPath(List<PathStep> steps, double offsetX, double offsetY)
    {
        StringBuilder builder = new StringBuilder();
        for (PathStep step : steps)
        {
            if (step instanceof LinePathStep)
            {
                LinePathStep lineStep = (LinePathStep)step;
                if (builder.length() == 0)
                {
                    builder.append(String.format("M %s,%s ",
                            offsetX + lineStep.getX1(), offsetY + lineStep.getY1()
                    ));
                }
                builder.append(String.format("L %s,%s ",
                        offsetX + lineStep.getX2(), offsetY + lineStep.getY2()
                ));
            }
            else if (step instanceof CubicPathStep)
            {
                CubicPathStep cubicStep = (CubicPathStep)step;
                if (builder.length() == 0)
                {
                    builder.append(String.format("M %s,%s ",
                            offsetX + cubicStep.getX1(), offsetY + cubicStep.getY1()
                    ));
                }
                builder.append(String.format("C %s,%s %s,%s %s,%s ",
                        offsetX + cubicStep.getX2(), offsetY + cubicStep.getY2(),
                        offsetX + cubicStep.getX3(), offsetY + cubicStep.getY3(),
                        offsetX + cubicStep.getX4(), offsetY + cubicStep.getY4()
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

    private String toSvgColor(Paint paint)
    {
        if (paint instanceof Color)
        {
            Color color = (Color)paint;
            double red = color.getRed();
            double green = color.getGreen();
            double blue = color.getBlue();
            return String.format("rgb(%s, %s, %s)", to255(red), to255(green), to255(blue));
        }
        throw new IllegalStateException();
    }

    private static int to255(double value)
    {
        return (int)Math.round(255 * value);
    }
}
