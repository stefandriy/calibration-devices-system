package com.softserve.edu.service.admin.impl;

import com.softserve.edu.entity.Address;
import com.softserve.edu.entity.organization.Organization;
import com.softserve.edu.entity.organization.OrganizationChangesHistory;
import com.softserve.edu.entity.enumeration.organization.OrganizationType;
import com.softserve.edu.entity.catalogue.Locality;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.enumeration.user.UserRole;
import com.softserve.edu.repository.OrganizationChangesHistoryRepository;
import com.softserve.edu.repository.OrganizationRepository;
import com.softserve.edu.repository.UserRepository;
import com.softserve.edu.service.catalogue.LocalityService;
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
import java.util.Set;

@Service
public class OrganizationServiceImpl implements OrganizationService {

    private final Logger logger = Logger.getLogger(OrganizationServiceImpl.class);

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private ProviderEmployeeService providerEmployeeService;

    @Autowired
    private OrganizationChangesHistoryRepository organizationChangesHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailServiceImpl mail;

    @Autowired
    private LocalityService localityService;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void addOrganizationWithAdmin(String name, String email, String phone, List<String> types, Integer employeesCapacity,
                                         Integer maxProcessTime, String firstName, String lastName, String middleName,
                                         String username, String password, Address address, String adminName, Long[] localityIdList) {

        Organization organization = new Organization(name, email, phone, employeesCapacity, maxProcessTime, address);
        String passwordEncoded = new BCryptPasswordEncoder().encode(password);
        User employeeAdmin = new User(firstName, lastName, middleName, username, passwordEncoded, organization);

        for (String type : types) {
            OrganizationType organizationType = OrganizationType.valueOf(type);
            employeeAdmin.addRole(UserRole.valueOf(organizationType + "_ADMIN"));

            organization.addOrganizationType(organizationType);
            organization.addUser(employeeAdmin);
        }

        for (Long localityId : localityIdList) {
            Locality locality = localityService.findById(localityId);
            organization.addLocality(locality);
        }

        organizationRepository.save(organization);


        String stringOrganizationTypes = String.join(",", types);

        Date date = new Date();

        OrganizationChangesHistory organizationChangesHistory = new OrganizationChangesHistory(date, name, email, phone, employeesCapacity,
                maxProcessTime, stringOrganizationTypes, username, firstName, lastName, middleName, organization, address, adminName);

        organizationChangesHistoryRepository.save(organizationChangesHistory);
        organization.addOrganizationChangeHistory(organizationChangesHistory);
        organizationRepository.save(organization);
    }

    @Override
    @Transactional(readOnly = true)
    public ListToPageTransformer<Organization> getOrganizationsBySearchAndPagination(
            int pageNumber, int itemsPerPage, String name, String email,
            String number, String type, String region, String district, String locality,
            String streetToSearch, String sortCriteria, String sortOrder
    ) {

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


        User employeeAdmin = userRepository.findOne(username);
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

        String stringOrganizationTypes = String.join(",", types);

        logger.info("password===========");
        logger.info(employeeAdmin.getPassword());
        Date date = new Date();

        OrganizationChangesHistory organizationChangesHistory = new OrganizationChangesHistory(date, name, email, phone, employeesCapacity,
                maxProcessTime, stringOrganizationTypes, username, firstName, lastName, middleName, organization, address, adminName);

        organizationChangesHistoryRepository.save(organizationChangesHistory);
        organization.addOrganizationChangeHistory(organizationChangesHistory);
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
    public List<OrganizationChangesHistory> getHistoryByOrganizationId(Long organizationId) {
        return organizationChangesHistoryRepository.findByOrganizationId(organizationId);
    }

    @Override
    @Transactional
    public List<Organization> findAllByLocalityId(Long localityId) {
        return organizationRepository.findOrganizationByLocalityId(localityId);
    }

    @Override
    public List<Organization> findAllByLocalityIdAndTypeId(Long localityId, OrganizationType typeId) {
        return organizationRepository.findOrganizationByLocalityIdAndType(localityId, typeId);
    }

    @Override
    public Set<OrganizationType> findOrganizationTypesById(Long id) {
        return organizationRepository.findOrganizationTypesById(id);
    }

}
