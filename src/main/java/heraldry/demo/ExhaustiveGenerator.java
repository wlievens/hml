package heraldry.demo;

import heraldry.model.ChargedBackgroundModel;
import heraldry.model.CoatOfArms;
import heraldry.model.FieldBackground;
import heraldry.model.Tincture;
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
        for (Tincture tincture : Tincture.values())
        {
            CoatOfArms coat = new CoatOfArms();
            coat.setShape("heater-shield");
            ChargedBackgroundModel model = new ChargedBackgroundModel();
            model.setBackground(new FieldBackground(tincture));
            model.setCharges(new ArrayList<>());
            coat.setModel(model);
            coats.add(coat);
        }

        int columns = Math.min(6, coats.size());
        int rows = (coats.size() + columns - 1) / columns;
        int spacing = 20;

        int shieldWidth = 200;
        int shieldHeight = 230;

        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element xmlSvg = document.createElement("svg");
        xmlSvg.setAttribute("xmlns", "http://www.w3.org/2000/svg");
        double fullWidth = columns * shieldWidth + (columns - 1) * spacing;
        double fullHeight = rows * shieldHeight + (rows - 1) * spacing;
        xmlSvg.setAttribute("width", String.valueOf(fullWidth));
        xmlSvg.setAttribute("height", String.valueOf(fullHeight));

        Element xmlG = document.createElement("g");
        for (int index = 0; index < coats.size(); ++index)
        {
            CoatOfArms coat = coats.get(index);
            int column = index % columns;
            int row = index / columns;
            Rendering rendering = coat.render();
            Element xmlG2 = document.createElement("g");
            xmlG2.appendChild(rendering.toSvgElement(document));
            xmlG2.setAttribute("transform", "translate(" + (column * (spacing + shieldWidth)) + "," + (row * (spacing + shieldHeight)) + ")");
            xmlG.appendChild(xmlG2);
        }
        xmlSvg.appendChild(xmlG);
        document.appendChild(xmlSvg);

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(document);
        try (FileWriter writer = new FileWriter(file))
        {
            transformer.transform(source, new StreamResult(writer));
        }
    }
}
