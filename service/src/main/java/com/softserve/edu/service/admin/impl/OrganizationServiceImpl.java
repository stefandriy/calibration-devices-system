package com.softserve.edu.service.admin.impl;

import com.softserve.edu.entity.Address;
import com.softserve.edu.entity.Organization;
import com.softserve.edu.entity.OrganizationChangeHistory;
import com.softserve.edu.entity.OrganizationType;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.user.UserRole;
import com.softserve.edu.entity.util.OrganizationChangeHistoryPK;
import com.softserve.edu.repository.OrganizationChangeHistoryRepository;
import com.softserve.edu.repository.OrganizationRepository;
import com.softserve.edu.repository.UserRepository;
import com.softserve.edu.service.tool.impl.MailServiceImpl;
import com.softserve.edu.service.admin.OrganizationService;
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
import java.util.Date;
import java.util.List;

@Service
public class OrganizationServiceImpl implements OrganizationService {

    private final Logger logger = Logger.getLogger(OrganizationServiceImpl.class);

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private ProviderEmployeeService providerEmployeeService;

    @Autowired
    private OrganizationChangeHistoryRepository organizationChangeHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailServiceImpl mail;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void addOrganizationWithAdmin(String name, String email, String phone, String[] types, Integer employeesCapacity,
                                         Integer maxProcessTime, String firstName, String lastName, String middleName,
                                         String username, String password, Address address, String adminName) {

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

        String stringOrganizationTypes = String.join(",", types);

        OrganizationChangeHistoryPK organizationChangeHistoryPK = new OrganizationChangeHistoryPK(new Date(), organization.getId());

        OrganizationChangeHistory organizationChangeHistory = new OrganizationChangeHistory(name, organizationChangeHistoryPK, email, phone, employeesCapacity,
                maxProcessTime, stringOrganizationTypes, username, firstName, lastName, middleName, organization, address, adminName);

        organizationChangeHistoryRepository.save(organizationChangeHistory);
        organization.addOrganizationChangeHistory(organizationChangeHistory);
        organizationRepository.save(organization);
    }


    @Override
    @Transactional(readOnly = true)
    public ListToPageTransformer<Organization> getOrganizationsBySearchAndPagination(int pageNumber, int itemsPerPage, String name,
                                                                                     String email, String number, String type, String
                                                                                     region, String district, String locality, String streetToSearch,
                                                                                     String sortCriteria, String sortOrder) {

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

    @Override
    @Transactional
    public Organization getOrganizationById(Long id) {
        return organizationRepository.findOne(id);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public void editOrganization(Long organizationId, String name,
                                 String phone, String email, List<String> types, Integer employeesCapacity, Integer maxProcessTime, Address address, String password, String username, String firstName, String lastName, String middleName, String adminName) {

        Organization organization = organizationRepository.findOne(organizationId);

        logger.debug(organization);

        organization.setName(name);
        organization.setPhone(phone);
        organization.setEmail(email);
        organization.setEmployeesCapacity(employeesCapacity);
        organization.setMaxProcessTime(maxProcessTime);
        organization.setAddress(address);

        organization.removeOrganizationTypes();
        types.
                stream()
                .map(OrganizationType::valueOf)
                .forEach(organization::addOrganizationType);


        User employeeAdmin = userRepository.getUserByUserName(username);
        employeeAdmin.setFirstName(firstName);
        employeeAdmin.setLastName(lastName);
        employeeAdmin.setMiddleName(middleName);

        employeeAdmin.setPassword(password != null && password.equals("generate") ? "generate" : employeeAdmin.getPassword());

        userRepository.save(employeeAdmin);
        providerEmployeeService.updateEmployee(employeeAdmin);

        logger.info("password===========");
        logger.info(employeeAdmin.getPassword());
        organizationRepository.save(organization);

        String stringOrganizationTypes = String.join(",", types);

        OrganizationChangeHistoryPK organizationChangeHistoryPK = new OrganizationChangeHistoryPK(new Date(), organizationId);

        OrganizationChangeHistory organizationChangeHistory = new OrganizationChangeHistory(name, organizationChangeHistoryPK, email, phone, employeesCapacity,
                maxProcessTime, stringOrganizationTypes, username, firstName, lastName, middleName, organization, address, adminName);

        organizationChangeHistoryRepository.save(organizationChangeHistory);
        organization.addOrganizationChangeHistory(organizationChangeHistory);
        organizationRepository.save(organization);
    }

    @Override
    @Transactional
    public Integer getOrganizationEmployeesCapacity(Long organizationId) {
        return organizationRepository.findOne(organizationId).getEmployeesCapacity();
    }

    @Override
    @Transactional
    public void sendOrganizationChanges(Organization organization, User admin) {
        mail.sendOrganizationChanges(organization, admin);
    }

    @Override
    @Transactional
    public List<OrganizationChangeHistory> getOrganizationEditHistoryById (Long organizationId){
        return organizationChangeHistoryRepository.getById(organizationId);
    }

}
