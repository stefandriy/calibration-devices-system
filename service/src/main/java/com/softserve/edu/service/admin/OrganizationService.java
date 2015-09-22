package com.softserve.edu.service.admin;

import com.softserve.edu.entity.Address;
import com.softserve.edu.entity.Organization;
import com.softserve.edu.entity.OrganizationType;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.user.UserRole;
import com.softserve.edu.repository.OrganizationRepository;
import com.softserve.edu.repository.UserRepository;
import com.softserve.edu.service.MailService;
import com.softserve.edu.service.provider.ProviderEmployeeService;
import com.softserve.edu.service.utils.ArchivalOrganizationsQueryConstructorAdmin;
import com.softserve.edu.service.utils.ListToPageTransformer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

@Service
public class OrganizationService {

    private final Logger logger = Logger.getLogger(OrganizationService.class);

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private ProviderEmployeeService providerEmployeeService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailService mail;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void addOrganizationWithAdmin(String name, String email, String phone, String[] types, Integer employeesCapacity,
                                         Integer maxProcessTime, String firstName, String lastName, String middleName,
                                         String username, String password, Address address) {

        Organization organization = new Organization(name, email, phone, employeesCapacity, maxProcessTime, address);
        String passwordEncoded = new BCryptPasswordEncoder().encode(password);
        User employeeAdmin = new User(firstName, lastName, middleName, username, passwordEncoded, organization);

        for (String type : types) { //TODO
            OrganizationType organizationType = OrganizationType.valueOf(type);
            organization.addOrganizationType(organizationType);
            String strRole = organizationType + "_ADMIN";
            UserRole userRole = userRepository.getUserRole(strRole);
            employeeAdmin.getUserRoles().add(userRole);
        }

        organizationRepository.save(organization);
        userRepository.save(employeeAdmin);
    }

    @Transactional(readOnly = true)
    public ListToPageTransformer<Organization> getOrganizationsBySearchAndPagination(int pageNumber, int itemsPerPage, String name,
                                                                                     String email, String number, String type, String region, String district, String locality, String streetToSearch, String sortCriteria, String sortOrder) {

        CriteriaQuery<Organization> criteriaQuery = ArchivalOrganizationsQueryConstructorAdmin.buildSearchQuery(name, email, number, type, region, district, locality, streetToSearch, sortCriteria, sortOrder, entityManager);

        Long count = entityManager.createQuery(ArchivalOrganizationsQueryConstructorAdmin.buildCountQuery(name, email, number, type, region, district, locality, streetToSearch, sortCriteria, sortOrder, entityManager)).getSingleResult();

        TypedQuery<Organization> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult((pageNumber - 1) * itemsPerPage);
        typedQuery.setMaxResults(itemsPerPage);
        List<Organization> OrganizationList = typedQuery.getResultList();

        ListToPageTransformer<Organization> result = new ListToPageTransformer<Organization>();
        result.setContent(OrganizationList);
        result.setTotalItems(count);
        return result;
    }

    @Transactional
    public Organization getOrganizationById(Long id) {
        return organizationRepository.findOne(id);
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public void editOrganization(Long organizationId, String name,
                                 String phone, String email, List<String> types, Integer employeesCapacity, Integer maxProcessTime, Address address, String password, String username, String firstName, String lastName, String middleName) {

        Organization organization = organizationRepository.findOne(organizationId);

        logger.debug(organization);

        organization.setName(name);
        organization.setPhone(phone);
        organization.setEmail(email);
        organization.setEmployeesCapacity(employeesCapacity);
        organization.setMaxProcessTime(maxProcessTime);
        organization.setAddress(address);

        types.
                stream()
                .map(OrganizationType::valueOf)
                .forEach(organization::addOrganizationType);


        User employeeAdmin = userRepository.getUserByUserName(username);
        logger.info("==========employeeAdmin=============");
        logger.info(employeeAdmin);
        employeeAdmin.setFirstName(firstName);
        employeeAdmin.setLastName(lastName);
        employeeAdmin.setMiddleName(middleName);

        employeeAdmin.setPassword(password != null && password.equals("generate") ? "generate" : employeeAdmin.getPassword());

        userRepository.save(employeeAdmin);
        providerEmployeeService.updateEmployee(employeeAdmin);

        logger.info("password===========");
        logger.info(employeeAdmin.getPassword());
        organizationRepository.save(organization);
    }

    @Transactional
    public Integer getOrganizationEmployeesCapacity(Long organizationId) {
        return organizationRepository.findOne(organizationId).getEmployeesCapacity();
    }

    @Transactional
    public void sendOrganizationChanges (Long organizationId, String username){
        mail.sendOrganizationChanges(organizationId, username);
    }
}
