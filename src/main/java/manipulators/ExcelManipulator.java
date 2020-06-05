package manipulators;

import model.Company;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import util.FloatParser;
import util.Printer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExcelManipulator implements Manipulator {
    private static final String FILE_NAME = "Companies.xlsx";
    private static final String SHEET_NAME = "Companies";

    private XSSFWorkbook getBook() throws IOException {
        if (new File(FILE_NAME).exists()) {
            FileInputStream fileInputStream = new FileInputStream(FILE_NAME);
            return new XSSFWorkbook(fileInputStream);
        }
        XSSFWorkbook workbook = new XSSFWorkbook();
        workbook.createSheet(SHEET_NAME);
        FileOutputStream out = new FileOutputStream(new File(FILE_NAME));
        workbook.write(out);
        out.close();
        return workbook;
    }

    @Override
    public void writeDataOfCompany(Company company) {
        XSSFWorkbook workbook = null;
        try {
            workbook = getBook();
        } catch (IOException e) {
            Printer.print("Не удалось получить книгу.");
        }
        if (workbook != null) {
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            int rowIndex = 1;
            while (rowIterator.hasNext()) {
                rowIterator.next();
                rowIndex++;
            }
            writeDataOfCompanyToRow(company, sheet.createRow(rowIndex));
            try {
                updateBook(workbook);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //TODO Нужно переписать метод
    @Override
    public void writeDataForAllCompany(List<Company> listOfCompany) {
        listOfCompany.forEach(this::writeDataOfCompany);
    }

    private void writeDataOfCompanyToRow(Company company, Row row) {
        int cellIndex = 0;
        row.createCell(cellIndex).setCellValue(row.getRowNum());
        cellIndex++;
        row.createCell(cellIndex).setCellValue(company.getName());
        cellIndex++;
        row.createCell(cellIndex).setCellValue(company.getIndustry());
        cellIndex++;
        row.createCell(cellIndex).setCellValue(company.getTicker());
        cellIndex++;
        for (String s : company.getAllIndicators().values()) {
            row.createCell(cellIndex).setCellValue(s);
            cellIndex++;
        }
    }

    private void updateBook(XSSFWorkbook workbook) throws IOException {
        FileOutputStream out = new FileOutputStream(new File(FILE_NAME));
        workbook.write(out);
        out.close();
    }
}
