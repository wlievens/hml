package heraldry.io;

import heraldry.model.Background;
import heraldry.model.Charge;
import heraldry.model.ChargedBackgroundModel;
import heraldry.model.CoatOfArms;
import heraldry.model.Division;
import heraldry.model.DivisionBackground;
import heraldry.model.DivisionPart;
import heraldry.model.FieldBackground;
import heraldry.model.InescutcheonCharge;
import heraldry.model.Line;
import heraldry.model.MobileCharge;
import heraldry.model.Ordinary;
import heraldry.model.OrdinaryCharge;
import heraldry.model.RepeatCharge;
import heraldry.model.Tincture;
import heraldry.model.Variation;
import heraldry.model.VariationBackground;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CoatOfArmsReader
{
    
    private Schema schema;
    
    public CoatOfArmsReader()
    {
        loadSchema();
    }
    
    public CoatOfArms readCoatOfArms(File file) throws IOException
    {
        try
        {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);
            schema.newValidator().validate(new DOMSource(document));
            return readCoatOfArms(document.getDocumentElement());
        }
        catch (SAXException | ParserConfigurationException e)
        {
            throw new IllegalStateException(e);
        }
    }
    
    private void loadSchema()
    {
        try (InputStream stream = CoatOfArmsReader.class.getResourceAsStream("/xsd/heraldry.xsd"))
        {
            this.schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(new StreamSource(stream));
        }
        catch (IOException | SAXException e)
        {
            throw new IllegalStateException();
        }
    }
    
    private CoatOfArms readCoatOfArms(Element element)
    {
        assertNodeName("coatOfArms", element);
        CoatOfArms coatOfArms = new CoatOfArms();
        getChildElement(element, "title").map(Element::getTextContent).ifPresent(coatOfArms::setTitle);
        getChildElement(element, "blazon").map(Element::getTextContent).map(this::strip).ifPresent(coatOfArms::setBlazon);
        getChildElement(element, "shape").map(Element::getTextContent).ifPresent(coatOfArms::setShape);
        coatOfArms.setModel(readChargedBackgroundModel(element));
        return coatOfArms;
    }

    private String strip(String text)
    {
        // TODO do this with a regex
        text = text.replace("\n", " ");
        text = text.replace("\r", " ");
        while (text.contains("  "))
        {
            text = text.replace("  ", " ");
        }
        return text.trim();
    }

    private ChargedBackgroundModel readChargedBackgroundModel(Element element)
    {
        ChargedBackgroundModel model = new ChargedBackgroundModel();
        getChildElement(element, "background").map(this::readBackground).ifPresent(model::setBackground);
        model.setCharges(getChildElement(element, "charges").map(this::readCharges).orElse(Collections.emptyList()));
        return model;
    }
    
    private List<Charge> readCharges(Element element)
    {
        return getChildElements(element).stream()
                .map(this::readCharge)
                .collect(Collectors.toList());
    }
    
    private Charge readCharge(Element element)
    {
        String tag = element.getNodeName();
        switch (tag)
        {
            case "ordinary":
                return readOrdinaryCharge(element);
            case "repeat":
                return readRepeatCharge(element);
            case "inescutcheon":
                return readInescutcheonCharge(element);
            case "mobile":
                return readMobileCharge(element);
            default:
                throw new IllegalStateException(String.format("Undefined charge type '%s'", tag));
        }
    }
    
    private RepeatCharge readRepeatCharge(Element element)
    {
        int number = Integer.parseInt(element.getAttribute("number"));
        if (number < 1)
        {
            throw new IllegalStateException();
        }
        Charge charge = readCharge(getChildElements(element).get(0));
        return new RepeatCharge(number, charge);
    }
    
    private InescutcheonCharge readInescutcheonCharge(Element element)
    {
        return new InescutcheonCharge(readChargedBackgroundModel(element));
    }
    
    private MobileCharge readMobileCharge(Element element)
    {
        Tincture tincture = element.hasAttribute("tincture") ? readTincture(element.getAttribute("tincture")) : null;
        String figure = element.getAttribute("figure");
        return new MobileCharge(tincture, figure);
    }
    
    private OrdinaryCharge readOrdinaryCharge(Element element)
    {
        Ordinary type = readOrdinary(element.getAttribute("type"));
        Line line = readLine(element.getAttribute("line"));
        Tincture tincture = readTincture(element.getAttribute("tincture"));
        List<Charge> charges = getChildElement(element, "charges").map(this::readCharges).orElse(Collections.emptyList());
        return new OrdinaryCharge(type, line, tincture, charges);
    }
    
    private List<Element> getChildElements(Element element)
    {
        List<Element> list = new ArrayList<>();
        NodeList children = element.getChildNodes();
        for (int index = 0; index < children.getLength(); ++index)
        {
            Node child = children.item(index);
            if (child.getNodeType() == Node.ELEMENT_NODE)
            {
                list.add((Element)child);
            }
        }
        return list;
    }
    
    private Background readBackground(Element element)
    {
        NodeList children = element.getChildNodes();
        for (int index = 0; index < children.getLength(); ++index)
        {
            Node child = children.item(index);
            if (child.getNodeType() == Node.ELEMENT_NODE)
            {
                String tag = child.getNodeName();
                Element childElement = (Element)child;
                switch (tag)
                {
                    case "field":
                        return readFieldBackground(childElement);
                    case "variation":
                        return readVariationBackground(childElement);
                    case "division":
                        return readDivisionBackground(childElement);
                    default:
                        throw new IllegalStateException(String.format("Undefined background type '%s'", tag));
                }
            }
        }
        throw new IllegalStateException(String.format("No background present"));
    }
    
    private DivisionBackground readDivisionBackground(Element element)
    {
        List<DivisionPart> parts = getChildElements(element).stream()
                .map(this::readDivisionPart)
                .collect(Collectors.toList());
        return new DivisionBackground(readDivision(element.getAttribute("type")), parts);
    }
    
    private DivisionPart readDivisionPart(Element element)
    {
        List<Integer> positions = readIntegerList(element.getAttribute("position"));
        ChargedBackgroundModel model = readChargedBackgroundModel(element);
        return new DivisionPart(positions, model);
    }

    private List<Integer> readIntegerList(String text)
    {
        return Arrays.stream(text.split(",")).map(Integer::parseInt).collect(Collectors.toList());
    }
    
    private FieldBackground readFieldBackground(Element element)
    {
        Tincture tincture = readTincture(element.getAttribute("tincture"));
        return new FieldBackground(tincture);
    }

    private VariationBackground readVariationBackground(Element element)
    {
        Variation variation = readVariation(element.getAttribute("type"));
        Tincture tincture1 = readTincture(element.getAttribute("firstTincture"));
        Tincture tincture2 = readTincture(element.getAttribute("secondTincture"));
        return new VariationBackground(variation, tincture1, tincture2);
    }

    private Variation readVariation(String name)
    {
        return readEnumValue(Variation.class, name);
    }
    
    private Ordinary readOrdinary(String name)
    {
        return readEnumValue(Ordinary.class, name);
    }

    private <T extends Enum<T>> T readEnumValue(Class<T> type, String name)
    {
        try
        {
            return (T)Arrays.stream(type.getDeclaredMethods())
                    .filter(method -> method.getName().equals("valueOf"))
                    .filter(method -> Modifier.isStatic(method.getModifiers()) && Modifier.isPublic(method.getModifiers()))
                    .findAny()
                    .get()
                    .invoke(null, name.toUpperCase());
        }
        catch (IllegalAccessException e)
        {
            throw new IllegalStateException(e);
        }
        catch (InvocationTargetException e)
        {
            if (e.getCause() instanceof IllegalArgumentException)
            {
                throw new IllegalStateException(String.format("Invalid %s value '%s'", type.getSimpleName(), name));
            }
            throw new IllegalStateException(e.getCause());
        }
    }
    
    private Division readDivision(String name)
    {
        try
        {
            return Division.valueOf(name.toUpperCase());
        }
        catch (IllegalArgumentException e)
        {
            throw new IllegalStateException(String.format("Invalid Division value '%s'", name));
        }
    }
    
    private Tincture readTincture(String name)
    {
        return readEnumValue(Tincture.class, name);
    }
    
    private Line readLine(String name)
    {
        return (name == null || name.length() == 0) ? Line.PLAIN : readEnumValue(Line.class, name);
    }
    
    private Optional<Element> getChildElement(Element element, String name)
    {
        NodeList children = element.getChildNodes();
        for (int index = 0; index < children.getLength(); ++index)
        {
            Node child = children.item(index);
            if (child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals(name))
            {
                return Optional.of((Element)child);
            }
        }
        return Optional.empty();
    }
    
    private void assertNodeName(String name, Element element)
    {
        if (!name.equals(element.getNodeName()))
        {
            throw new IllegalStateException();
        }
    }
}