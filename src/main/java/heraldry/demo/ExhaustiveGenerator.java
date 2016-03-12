package heraldry.demo;

import heraldry.model.Charge;
import heraldry.model.ChargedBackgroundModel;
import heraldry.model.CoatOfArms;
import heraldry.model.FieldBackground;
import heraldry.model.Line;
import heraldry.model.Ordinary;
import heraldry.model.OrdinaryCharge;
import heraldry.model.Tincture;
import heraldry.model.Variation;
import heraldry.model.VariationBackground;
import heraldry.render.Rendering;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExhaustiveGenerator
{
    public static void main(String[] args) throws TransformerException, IOException, ParserConfigurationException
    {
        File file = new File(String.format("target/test-output/%s/output.svg", ExhaustiveGenerator.class.getSimpleName()));
        file.getParentFile().mkdirs();

        List<CoatOfArms> coats = new ArrayList<>();
        for (String shape : new String[]{ "heater-shield", "horizontal-banner", "vertical-banner" })
        {
            for (Tincture tincture : Tincture.values())
            {
                CoatOfArms coat = new CoatOfArms();
                coat.setShape(shape);
                ChargedBackgroundModel model = new ChargedBackgroundModel();
                model.setBackground(new FieldBackground(tincture));
                model.setCharges(new ArrayList<>());
                coat.setModel(model);
                coats.add(coat);
            }
            for (Line line : new Line[]{ Line.PLAIN, Line.WAVY, Line.ENGRAILED })
            {
                for (Variation variation : Variation.values())
                {
                    CoatOfArms coat = new CoatOfArms();
                    coat.setShape(shape);
                    ChargedBackgroundModel model = new ChargedBackgroundModel();
                    model.setBackground(new VariationBackground(variation, Tincture.ARGENT, Tincture.AZURE, line));
                    model.setCharges(new ArrayList<>());
                    coat.setModel(model);
                    coats.add(coat);
                }
                for (Ordinary ordinary : Ordinary.values())
                {
                    CoatOfArms coat = new CoatOfArms();
                    coat.setShape(shape);
                    ChargedBackgroundModel model = new ChargedBackgroundModel();
                    model.setBackground(new FieldBackground(Tincture.ARGENT));
                    ArrayList<Charge> charges = new ArrayList<>();
                    if (ordinary != null)
                    {
                        charges.add(new OrdinaryCharge(Ordinary.SALTIRE, Line.PLAIN, new FieldBackground(Tincture.SABLE), new ArrayList<Charge>()));
                        charges.add(new OrdinaryCharge(ordinary, line, new FieldBackground(Tincture.GULES), new ArrayList<Charge>()));
                    }
                    model.setCharges(charges);
                    coat.setModel(model);
                    coats.add(coat);
                }
            }
        }

        int columns = Math.min(7, coats.size());
        int rows = (coats.size() + columns - 1) / columns;
        int margin = 10;
        int spacingX = 20;
        int spacingY = 45;

        int shieldWidth = 200;
        int shieldHeight = 230;

        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element svg = document.createElement("svg");
        svg.setAttribute("xmlns", "http://www.w3.org/2000/svg");
        double fullWidth = columns * shieldWidth + (columns - 1) * spacingX + 2 * margin;
        double fullHeight = rows * shieldHeight + (rows - 1) * spacingY + 2 * margin + spacingY;
        svg.setAttribute("width", String.valueOf(fullWidth));
        svg.setAttribute("height", String.valueOf(fullHeight));

        Element svgG = document.createElement("g");
        for (int index = 0; index < coats.size(); ++index)
        {
            CoatOfArms coat = coats.get(index);
            int column = index % columns;
            int row = index / columns;
            Rendering rendering = coat.render();
            Element svgG2 = document.createElement("g");
            svgG2.appendChild(rendering.toSvgElement(document));
            svgG2.setAttribute("transform", "translate(" + (margin + column * (spacingX + shieldWidth)) + "," + (margin + row * (spacingY + shieldHeight)) + ")");
            svgG.appendChild(svgG2);
            Element svgText = document.createElement("text");
            svgText.setTextContent(coat.generateBlazon());
            svgText.setAttribute("x", Integer.toString(shieldWidth / 2));
            svgText.setAttribute("y", Integer.toString(shieldHeight + 15 + 15 * (column % 2)));
            svgText.setAttribute("style", "fill:black;");
            svgText.setAttribute("text-anchor", "middle");
            svgG2.appendChild(svgText);
        }
        svg.appendChild(svgG);
        document.appendChild(svg);

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(document);
        try (FileWriter writer = new FileWriter(file))
        {
            transformer.transform(source, new StreamResult(writer));
        }
    }
}
