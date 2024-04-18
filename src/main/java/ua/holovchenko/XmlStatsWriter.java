package ua.holovchenko;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class XmlStatsWriter {
    final String attribute;

    public XmlStatsWriter(String attribute) {
        this.attribute = attribute;
    }

    public void writeStatsToXML(Map<String, Integer> statistic) {
        File xml = new File("statistics_by_" + attribute + ".xml");
        try(PrintWriter writer = new PrintWriter(new FileWriter(xml))) {
            writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.println("<statistics>");
            statistic.forEach((value, count) -> {
                writer.println("\t<item>");
                    writer.println("\t\t<value>" + value + "</value>");
                    writer.println("\t\t<count>" + count + "</count>");
                writer.println("\t</item>");
            });
            writer.println("</statistics>");
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
