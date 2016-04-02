package heraldry.demo;

import heraldry.io.CoatOfArmsReader;
import heraldry.model.CoatOfArms;
import heraldry.render.Rendering;
import org.w3c.dom.Document;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileWriter;

public class Demo
{
    public static void main(String[] args)
    {
        File path = new File("examples");
        for (File input : path.listFiles())
        {
            if (input.getName().toLowerCase().endsWith(".xml"))
            {
                try
                {
                    CoatOfArms coatOfArms = new CoatOfArmsReader().readCoatOfArms(input);
                    System.out.println(input);
                    System.out.println("\t" + coatOfArms.getBlazon());
                    System.out.println("\t" + coatOfArms);
                    System.out.println("\t" + coatOfArms.generateBlazon());
                    Rendering rendering = coatOfArms.render();
                    File output = new File(input.getParentFile() + "/output/" + input.getName().replace(".xml", ".svg"));
                    output.getParentFile().mkdirs();
                    Document svg = rendering.toSvg(8);
                    Transformer transformer = TransformerFactory.newInstance().newTransformer();
                    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                    DOMSource source = new DOMSource(svg);
                    try (FileWriter writer = new FileWriter(output))
                    {
                        StreamResult result = new StreamResult(writer);
                        transformer.transform(source, result);
                    }
                }
                catch (Throwable e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
