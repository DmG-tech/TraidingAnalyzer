package manipulators;

import model.Company;

import java.util.List;

public interface Manipulator {
    void writeDataOfCompany(Company company);
    void writeDataForAllCompany(List<Company> listOfCompany);
}
