package parsers;

import model.Company;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import util.Printer;

import java.io.IOException;


public class FinVizParser {
    private static final Logger log = Logger.getLogger(FinVizParser.class.getSimpleName());
    private static final String URL_PAGE_OF_COMPANY = "https://finviz.com/quote.ashx?t=%s";

    private static Document getPageOfCompany(String tickerOfCompany) {
        String url = String.format(URL_PAGE_OF_COMPANY, tickerOfCompany);
        Document pageOfCompany = null;
        Connection connection = Jsoup.connect(url);
        connection.userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.132 Safari/537.36 OPR/67.0.3575.97");
        try {
            pageOfCompany = connection.get();
            log.info("connection established. Url: " + url);
        } catch (IOException e) {
            log.log(Level.ERROR, e.getMessage(), e);
            Printer.print("Не удалось получить страницу");
        }
        return pageOfCompany;
    }

    public boolean getStatForCompany(Company company) {
        boolean isSuccessful = false;
        Document pageOfCompany = getPageOfCompany(company.getTicker());
        if (pageOfCompany != null) {
            Element tableOfStats = pageOfCompany.getElementsByClass("snapshot-table2").first();
            if (tableOfStats != null) {
                log.info("Table received for company: " + company.getName());
                company.setIndicator("Dividend", tableOfStats.selectFirst("tbody > tr:nth-of-type(8) > td:nth-of-type(2)").text());
                company.setIndicator("P/E", tableOfStats.selectFirst("tbody > tr:nth-of-type(1) > td:nth-of-type(4)").text());
                company.setIndicator("P/B", tableOfStats.selectFirst("tbody > tr:nth-of-type(5) > td:nth-of-type(4)").text());
                company.setIndicator("Debt/Eq", tableOfStats.selectFirst("tbody > tr:nth-of-type(10) > td:nth-of-type(4)").text());
                company.setIndicator("ROA", tableOfStats.selectFirst("tbody > tr:nth-of-type(5) > td:nth-of-type(8)").text());
                company.setIndicator("ROE", tableOfStats.selectFirst("tbody > tr:nth-of-type(6) > td:nth-of-type(8)").text());
                company.setIndicator("Market Cap", tableOfStats.selectFirst("tbody > tr:nth-of-type(2) > td:nth-of-type(2)").text());
                company.setIndicator("PEG", tableOfStats.selectFirst("tbody > tr:nth-of-type(3) > td:nth-of-type(4)").text());
                //Если какой либо индикатор не найден или значение отсутствует удаляем его
                checkIndicators(company);
                isSuccessful = true;
                Printer.print(company);
            } else {
                log.error("Table not found. model.Company: " + company.getName());
                Printer.print("Таблица не найдена");
            }
        }
        return isSuccessful;
    }

    private void checkIndicators (Company company) {
        company.getAllIndicators().entrySet().removeIf(entry -> entry.getValue() == null || entry.getValue().isEmpty());
    }
}
