package parsers;

import model.Company;
import manipulators.HtmlWriter;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import util.Printer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WikiParser {
    private static final String URL = "https://en.wikipedia.org/wiki/List_of_S%26P_500_companies";

    private Document getPage() {
        Document document = null;
        if (new File("Wiki.html").exists()) {
            try {
                document = Jsoup.parse(new File("Wiki.html"), "UTF-8");
            } catch (IOException e) {
                Printer.print("Не удалось получить страницу");
            }
        } else {
            Connection connection = Jsoup.connect(URL)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.132 Safari/537.36 OPR/67.0.3575.97");
            try {
                document = connection.get();
            } catch (IOException e) {
                Printer.print("Не удалось получить страницу");
            }
            HtmlWriter.writeToFile(document, "Wiki.html");
        }
        return document;
    }

    public List<Company> getListOfCompany() {
        ArrayList<Company> listOfCompany = new ArrayList<Company>();
        Document document = getPage();
        if (document != null) {
            Element table = document.getElementsByAttributeValue("id", "constituents").first();
            Elements lines = table.getElementsByTag("tr");
            lines.remove(0);
            for (Element line : lines) {
                Company company = new Company();
                company.setName(line.getElementsByTag("td").get(1).text());
                company.setIndustry(line.getElementsByTag("td").get(3).text());
                company.setTicker(line.getElementsByTag("td").first().text());
                listOfCompany.add(company);
            }
        } else {
            Printer.print("Страница не найдена");
            return null;
        }
        return listOfCompany;
    }
}