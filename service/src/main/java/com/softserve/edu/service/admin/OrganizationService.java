package com.softserve.edu.service.admin;

import com.softserve.edu.entity.Address;
import com.softserve.edu.entity.Organization;
import com.softserve.edu.entity.OrganizationChangeHistory;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.service.utils.ListToPageTransformer;

import java.util.List;

public interface OrganizationService {

     void addOrganizationWithAdmin(String name, String email, String phone, List<String> types, Integer employeesCapacity,
                                         Integer maxProcessTime, String firstName, String lastName, String middleName,
                                         String username, String password, Address address, String adminName) ;

     ListToPageTransformer<Organization> getOrganizationsBySearchAndPagination(int pageNumber, int itemsPerPage, String name,
                                                                                     String email, String number, String type, String region,
                                                                                     String district, String locality, String streetToSearch,
                                                                                     String sortCriteria, String sortOrder);

     Organization getOrganizationById(Long id);

     void editOrganization(Long organizationId, String name, String phone, String email, List<String> types,
                                 Integer employeesCapacity, Integer maxProcessTime, Address address, String password,
                                 String username, String firstName, String lastName, String middleName, String adminName);

     Integer getOrganizationEmployeesCapacity(Long organizationId);

     void sendOrganizationChanges (Organization organization, User admin);

    public List<OrganizationChangeHistory> getOrganizationEditHistoryById (Long organizationId);
}
