import manipulators.ExcelManipulator;
import manipulators.Manipulator;
import model.Company;
import parsers.CompanyParser;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        CompanyParser companyParser = new CompanyParser();
        AnalyzerOfCompany analyzer = new AnalyzerOfCompany();
        Manipulator manipulator = new ExcelManipulator();

        List<Company> listOfCompany = companyParser.getListOfCompanyWithData();
        analyzer.compareByAllMark(listOfCompany);

        //Записываем данные в файл excel
        for (Map.Entry<String, List<Company>> entry : analyzer.getGroupCompany(listOfCompany).entrySet()) {
            manipulator.writeDataForAllCompany(entry.getValue());
        }
    }
}
