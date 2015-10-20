package com.softserve.edu.service.admin;

import com.softserve.edu.entity.Address;
import com.softserve.edu.entity.enumeration.user.EmployeeRole;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.service.utils.ListToPageTransformer;

import java.util.List;

public interface UserService {

    boolean existsWithUsername(String username);

    List<String> getRoles(String username);

    void addSysAdmin( String  username, String password, String firstName, String lastName, String middleName, String phone,
                             String email,  Address address, Boolean isAvailable);

    List<User> findByOrganizationId(Long organizationId, int pageNumber, int itemsPerPage);

    Long getCountOfVerifications(EmployeeRole employeeRole, String username);

    User findOne(String username);

    void deleteSysAdmin(String username);

    void editSysAdmin( String  username, String password, String firstName, String lastName, String middleName, String phone,
                       String email,  Address address, Boolean isAvailable);

    ListToPageTransformer<User>
    findPageOfAllEmployees(int pageNumber, int itemsPerPage, String userName,
                           String role, String firstName, String lastName, String organization,
                           String telephone,  String sortCriteria, String sortOrder);
}
