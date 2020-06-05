package parsers;

import model.Company;
import util.Printer;

import java.util.Iterator;
import java.util.List;

public class CompanyParser {
    private FinVizParser finVizParser = new FinVizParser();
    private WikiParser wikiParser = new WikiParser();

    public List<Company> getListOfCompanyWithData() {
        //Поулчаем список компаний СП500
        List<Company> listOfCompany = wikiParser.getListOfCompany();
        Iterator<Company> companyIterator = listOfCompany.iterator();
        while (companyIterator.hasNext()) {
            Company currentCompany = companyIterator.next();
            Printer.print(currentCompany.getTicker());
            finVizParser.getStatForCompany(currentCompany);
            //Если не удалось считать показатели компании - удаяем ее из списка дял анализа
            if (currentCompany.getAllIndicators() == null || currentCompany.getAllIndicators().isEmpty()) {
                Printer.print("Удалена компания: " + currentCompany.getTicker());
                companyIterator.remove();
            }
        }
        return listOfCompany;
    }
}
