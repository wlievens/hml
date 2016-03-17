package heraldry.demo;

import heraldry.model.Charge;
import heraldry.model.ChargedBackgroundModel;
import heraldry.model.CoatOfArms;
import heraldry.model.Division;
import heraldry.model.DivisionBackground;
import heraldry.model.DivisionPart;
import heraldry.model.FieldBackground;
import heraldry.model.Line;
import heraldry.model.MobileCharge;
import heraldry.model.Ordinary;
import heraldry.model.OrdinaryCharge;
import heraldry.model.RepeatCharge;
import heraldry.model.SemyBackground;
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
        //for (String shape : new String[]{ "heater-shield", "horizontal-banner", "vertical-banner" })
        for (String shape : new String[]{ "heater-shield" })
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
            for (Line line : new Line[]{ Line.PLAIN, Line.WAVY })
            {
                for (Variation variation : Variation.values())
                {
                    CoatOfArms coat = new CoatOfArms();
                    coat.setShape(shape);
                    ChargedBackgroundModel model = new ChargedBackgroundModel();
                    model.setBackground(new VariationBackground(variation, Tincture.ARGENT, Tincture.AZURE, line, 0));
                    model.setCharges(new ArrayList<>());
                    coat.setModel(model);
                    coats.add(coat);
                }
                {
                    CoatOfArms coat = new CoatOfArms();
                    coat.setShape(shape);
                    ChargedBackgroundModel model = new ChargedBackgroundModel();
                    model.setBackground(new SemyBackground(new FieldBackground(Tincture.ARGENT), new OrdinaryCharge(Ordinary.LOZENGE, Tincture.AZURE)));
                    ArrayList<Charge> charges = new ArrayList<>();
                    model.setCharges(charges);
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
                    charges.add(new OrdinaryCharge(Ordinary.CROSS, Line.PLAIN, new FieldBackground(Tincture.SABLE), new ArrayList<Charge>()));
                    charges.add(new OrdinaryCharge(Ordinary.SALTIRE, Line.PLAIN, new FieldBackground(Tincture.SABLE), new ArrayList<Charge>()));
                    charges.add(new OrdinaryCharge(ordinary, line, new FieldBackground(Tincture.GULES), new ArrayList<Charge>()));
                    model.setCharges(charges);
                    coat.setModel(model);
                    coats.add(coat);
                }
                for (Ordinary ordinary : Ordinary.values())
                {
                    CoatOfArms coat = new CoatOfArms();
                    coat.setShape(shape);
                    ChargedBackgroundModel model = new ChargedBackgroundModel();
                    model.setBackground(new FieldBackground(Tincture.OR));
                    ArrayList<Charge> charges = new ArrayList<>();
                    charges.add(new RepeatCharge(3, new OrdinaryCharge(ordinary, line, new FieldBackground(Tincture.VERT), new ArrayList<Charge>())));
                    model.setCharges(charges);
                    coat.setModel(model);
                    coats.add(coat);
                }
                {
                    CoatOfArms coat = new CoatOfArms();
                    coat.setShape(shape);
                    ChargedBackgroundModel model = new ChargedBackgroundModel();
                    model.setBackground(new FieldBackground(Tincture.ARGENT));
                    ArrayList<Charge> charges = new ArrayList<>();
                    charges.add(new OrdinaryCharge(Ordinary.CROSS, Line.PLAIN, new FieldBackground(Tincture.SABLE), new ArrayList<Charge>()));
                    charges.add(new OrdinaryCharge(Ordinary.SALTIRE, Line.PLAIN, new FieldBackground(Tincture.SABLE), new ArrayList<Charge>()));
                    charges.add(new MobileCharge("fleur-de-lis", new FieldBackground(Tincture.GULES), new ArrayList<>()));
                    model.setCharges(charges);
                    coat.setModel(model);
                    coats.add(coat);
                }
                for (Division division : Division.values())
                {
                    CoatOfArms coat = new CoatOfArms();
                    coat.setShape(shape);
                    ChargedBackgroundModel model = new ChargedBackgroundModel();
                    List<DivisionPart> parts = new ArrayList<>();
                    for (int n = 0; n < division.getMaxPositions(); ++n)
                    {
                        parts.add(new DivisionPart(n + 1, Tincture.values()[n]));
                    }
                    model.setBackground(new DivisionBackground(division, parts));
                    model.setCharges(new ArrayList<>());
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
            int column = index % columns;
            int row = index / columns;
            CoatOfArms coat = coats.get(index);
            Element svgG2 = document.createElement("g");
            svgG2.setAttribute("transform", "translate(" + (margin + column * (spacingX + shieldWidth)) + "," + (margin + row * (spacingY + shieldHeight)) + ")");
            svgG.appendChild(svgG2);
            try
            {
                svgG2.appendChild(coat.render().toSvgElement(document));
            }
            catch (Throwable e)
            {
                e.printStackTrace();
            }
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
