package model;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class Company {
    private String name;
    private String industry;
    private String ticker;
    private Map<String, String> indicators = new LinkedHashMap<>();

    public Company() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public void setIndicator(String indicator, String value) {
        indicators.put(indicator, value);
    }

    public String getIndicator(String indicator) {
        if (indicators.containsKey(indicator)) return indicators.get(indicator);
        return null;
    }

    public Map<String, String> getAllIndicators() {
        if (!indicators.isEmpty()) return indicators;
        return null;
    }

    @Override
    public String toString() {
        StringBuilder stringOfIndicators = new StringBuilder();
        for (Map.Entry<String, String> entry: indicators.entrySet()) {
            stringOfIndicators.append(" " + entry.getKey() + " " + entry.getValue() + " ");
        }
        return "Company: " + name + " Industry: " + industry + "   | " + stringOfIndicators + " |";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Company company = (Company) o;
        return ticker.equals(company.ticker);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticker);
    }
}
