package com.softserve.edu.service.provider;


import com.softserve.edu.entity.user.User;
import com.softserve.edu.service.provider.buildGraphic.ProviderEmployeeGraphic;
import com.softserve.edu.service.utils.EmployeeDTO;
import com.softserve.edu.service.utils.ListToPageTransformer;

import java.util.Date;
import java.util.List;

public interface ProviderEmployeeService {

    void addEmployee(User providerEmployee);

    void updateEmployee(User providerEmployee);

    User oneProviderEmployee(String username);

    List<EmployeeDTO> getAllProviders(List<String> role, User employee);

    User findByUsername(String userName);

    List<String> getRoleByUserNam(String username);

    ListToPageTransformer<User>
    findPageOfAllProviderEmployeeAndCriteriaSearch(int pageNumber, int itemsPerPage, Long idOrganization, String userName,
                                                   String role, String firstName, String lastName, String organization,
                                                   String telephone, String fieldToSort);

    List<ProviderEmployeeGraphic> buildGraphic(Date from, Date to, Long idOrganization, List<User> listOfEmployee);

    List<ProviderEmployeeGraphic> buidGraphicMainPanel(Date from, Date to, Long idOrganization);

    Date convertToDate(String date) throws IllegalArgumentException;

    List<User> getAllProviderEmployee(Long idOrganization);
}
