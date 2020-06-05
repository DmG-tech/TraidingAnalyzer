package manipulators;

import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class HtmlWriter {
    public static void writeToFile(Document document, String fileName) {
        File file = new File(fileName);
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file);
            fileWriter.write(document.outerHtml());
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
