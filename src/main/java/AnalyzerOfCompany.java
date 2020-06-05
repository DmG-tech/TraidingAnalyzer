import model.Company;
import util.FloatParser;

import java.util.*;
import java.util.stream.Collectors;

public class AnalyzerOfCompany {
    private Map<Company, Integer> ratingMap = new HashMap<>();

    public Map<Company, Integer> getRatingMap() {
        return ratingMap;
    }

    public void compareByAllMark(List<Company> listOfCompany) {
        Set<String> setOfIndicator = new HashSet<>();

        //Получаем список доступных показателей компании
        for (Company company : listOfCompany) setOfIndicator.addAll(company.getAllIndicators().keySet());

        //Производим оценку по каждому найденому показателю
        for (String indicator : setOfIndicator) {
            sortByIndicator(listOfCompany, indicator);
        }

        //Добавляем к показателям компании Рейтинг
        addRatingToCompany();
    }

    public Map<String, List<Company>> getGroupCompany (List<Company> listOfCompany) {

        //Группировка компаний по отрасли и сортировка по рейтингу(от высокого к низкому) внутри отрасли
        Map<String, List<Company>> groupByIndustryMap = listOfCompany.stream().collect(Collectors.groupingBy(Company::getIndustry, Collectors.toList()));
        String indicator = "Rating";
        for (List<Company> list : groupByIndustryMap.values()) {
            //Если показатель отсутствует - удаляем компанию из списка
            checkValue(list, indicator);

            //Сортируем оставшиеся компании по рейтингу
            sortByMarkerFromHighToLow(list, indicator);
        }
        return groupByIndustryMap;
    }

    private void addRatingToCompany() {
        for (Map.Entry<Company, Integer> entry : ratingMap.entrySet()) {
            entry.getKey().setIndicator("Rating", entry.getValue().toString());
        }
    }

    //Начисление рейтинга
    private void giveGrades(List<Company> listOfCompany) {
        if (listOfCompany != null) {
            for (Company company : listOfCompany) {
                //Если компания уже есть в мапе - плюсуем рейтинг
                if (ratingMap.containsKey(company)) {
                    ratingMap.put(company, ratingMap.get(company) + listOfCompany.indexOf(company));
                }
                //Иначе заносим новую пару
                else {
                    ratingMap.put(company, listOfCompany.indexOf(company));
                }
            }
        }
    }

    //Сортировка по возрастанию
    private void sortByMarkerFromLowToHigh(List<Company> listOfCompany, String indicator) {
        listOfCompany.sort((comThis, comOther) -> Float.compare(FloatParser.parse(comThis.getIndicator(indicator)), FloatParser.parse(comOther.getIndicator(indicator))));
    }

    //Сортировка по убыванию
    private void sortByMarkerFromHighToLow(List<Company> listOfCompany, String indicator) {
        listOfCompany.sort((comThis, comOther) -> Float.compare(FloatParser.parse(comOther.getIndicator(indicator)), FloatParser.parse(comThis.getIndicator(indicator))));
    }

    //Удатяет из списка компании, чей текущий показатель отсутствует или по нему нет данных
    private void checkValue(List<Company> listOfCompany, String indicator) {
        listOfCompany.removeIf(nextCompany -> nextCompany.getIndicator(indicator) == null || nextCompany.getIndicator(indicator).equals("-"));
    }

    private void sortByIndicator(List<Company> listOfCompany, String indicator) {
        List<Company> forSort = new ArrayList<>(listOfCompany);
        checkValue(forSort, indicator);
        switch (indicator) {
            case "Dividend":
                sortByMarkerFromLowToHigh(forSort, indicator);
                break;
            case "P/E":
                //Удаляем все компании из списка, чей P/E меньше 0 или больше 30
                forSort.removeIf(company -> (FloatParser.parse(company.getIndicator(indicator)) < 0 | FloatParser.parse(company.getIndicator(indicator)) > 30));
                sortByMarkerFromHighToLow(forSort, indicator);
                break;
            case "P/B":
                //Удаляем все компании из списка, чей P/B больше 5
                forSort.removeIf(company -> (FloatParser.parse(company.getIndicator(indicator)) > 5));
                sortByMarkerFromHighToLow(forSort, indicator);
                break;
            case "Debt/Eq":
                //Удаляем все компании из списка, чей Debt/Eq больше 3
                forSort.removeIf(company -> (FloatParser.parse(company.getIndicator(indicator)) > 3));
                forSort.sort((comThis, comOther) -> Float.compare(1 - FloatParser.parse(comOther.getIndicator(indicator)), 1 - FloatParser.parse(comThis.getIndicator(indicator))));
                break;
            case "ROA":
                //Удаляем все компании из списка, чей ROA меньше или равен 0
                forSort.removeIf(company -> (FloatParser.parse(company.getIndicator(indicator)) <= 0));
                sortByMarkerFromLowToHigh(forSort, indicator);
                break;
            case "ROE":
                //Удаляем все компании из списка, чей ROE меньше или равен 0 или больше 100
                forSort.removeIf(company -> (FloatParser.parse(company.getIndicator(indicator)) <= 0) | FloatParser.parse(company.getIndicator(indicator)) > 100);
                sortByMarkerFromLowToHigh(forSort, indicator);
                break;
            case "PEG":
                //Удаляем все компании из списка, чей PEG больше 3
                forSort.removeIf(company -> (FloatParser.parse(company.getIndicator(indicator)) > 3));
                sortByMarkerFromHighToLow(forSort, indicator);
                break;
            default:
                forSort = null;
                break;
        }
        //Начисление рейтинга
        giveGrades(forSort);
    }

}
