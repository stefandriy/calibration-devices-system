package com.softserve.edu.service.provider;

import com.softserve.edu.entity.Organization;
import com.softserve.edu.entity.Verification;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.user.UserRole;
import com.softserve.edu.entity.util.Roles;
import com.softserve.edu.repository.OrganizationRepository;
import com.softserve.edu.repository.UserRepository;
import com.softserve.edu.repository.VerificationRepository;
import com.softserve.edu.service.MailService;
import com.softserve.edu.service.provider.buildGraphic.GraphicBuilder;
import com.softserve.edu.service.provider.buildGraphic.MonthOfYear;
import com.softserve.edu.service.provider.buildGraphic.ProviderEmployeeGraphic;
import com.softserve.edu.service.utils.EmployeeProvider;
import com.softserve.edu.service.utils.ListToPageTransformer;
import com.softserve.edu.service.utils.ProviderEmployeeQuary;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProviderEmployeeService {
    @Autowired
    private UserRepository providerEmployeeRepository;

    @Autowired
    private VerificationRepository verificationRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private MailService mail;

    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    private EntityManager em;

    private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");


    Logger logger = Logger.getLogger(ProviderEmployeeService.class);

    @Transactional
    public void addEmployee(User providerEmployee) {
        String passwordEncoded = new BCryptPasswordEncoder().encode(providerEmployee.getPassword());
        providerEmployee.setPassword(passwordEncoded);
        providerEmployeeRepository.save(providerEmployee);
    }

    @Transactional
    public void updateEmployee(User providerEmployee) {
        if (providerEmployee.getPassword().equals("generate")) {
            String newPassword = RandomStringUtils.randomAlphanumeric(5);
            mail.sendNewPasswordMail(providerEmployee.getEmail(), providerEmployee.getFirstName(), newPassword);
            String passwordEncoded = new BCryptPasswordEncoder().encode(newPassword);
            providerEmployee.setPassword(passwordEncoded);
        }
        providerEmployeeRepository.save(providerEmployee);
    }

    @Transactional
    public User oneProviderEmployee(String username) {
        return providerEmployeeRepository.getUserByUserName(username);
    }

    @Transactional
    public List<EmployeeProvider> getAllProviders(List<String> role, User employee) {
        List<EmployeeProvider> providerListEmployee = new ArrayList<>();
        if (role.contains(Roles.PROVIDER_ADMIN.name())) {
            List<User> list = providerEmployeeRepository.getAllProviderUsersList(Roles.PROVIDER_EMPLOYEE.name(),
                    employee.getOrganization().getId(), true);
            providerListEmployee = EmployeeProvider.giveListOfProviders(list);
        } else {
            EmployeeProvider userPage = new EmployeeProvider(employee.getUsername(), employee.getFirstName(),
                    employee.getLastName(), employee.getMiddleName(), role.get(0));
            providerListEmployee.add(userPage);
        }
        return providerListEmployee;
    }

    @Transactional()
    public User findByUserame(String userName) {
        return providerEmployeeRepository.findByUsername(userName);
    }


    @Transactional
    public List<UserRole> getRoleByUserNam(String username) {
        return providerEmployeeRepository.getRoleByUserNam(username);
    }


    @Transactional
    public ListToPageTransformer<User>
    findPageOfAllProviderEmployeeAndCriteriaSearch(int pageNumber, int itemsPerPage, long idOrganization, String userName,
                                                   String role, String firstName, String lastName, String organization,
                                                   String telephone, String fieldToSort) {
        CriteriaQuery<User> criteriaQuery = ProviderEmployeeQuary.buildSearchQuery(userName, role, firstName,
                lastName, organization, telephone, em, idOrganization, fieldToSort);

        Long count = em.createQuery(ProviderEmployeeQuary.buildCountQuery(userName, role, firstName,
                lastName, organization, telephone, idOrganization, em)).getSingleResult();

        TypedQuery<User> typedQuery = em.createQuery(criteriaQuery);
        typedQuery.setFirstResult((pageNumber - 1) * itemsPerPage);
        typedQuery.setMaxResults(itemsPerPage);
        List<User> providerEmployeeList = typedQuery.getResultList();

        ListToPageTransformer<User> result = new ListToPageTransformer<User>();
        result.setContent(providerEmployeeList);
        result.setTotalItems(count);
        return result;
    }

    @Transactional
    public List<ProviderEmployeeGraphic> buildGraphic(Date from, Date to, Long idOrganization, List<User> listOfEmployee) {
        Organization organization = organizationRepository.findOne(idOrganization);
        List<Verification> verifications = verificationRepository.
                findByProviderEmployeeIsNotNullAndProviderAndSentToCalibratorDateBetween
                        (organization, from, to);
        List<ProviderEmployeeGraphic> graficData = null;
        try {
            List<MonthOfYear> monthList = GraphicBuilder.listOfMonths(from, to);
            graficData = GraphicBuilder.builderData(verifications, monthList, listOfEmployee);

        } catch (ParseException e) {
            logger.error(e.getMessage());
        }
        return graficData;
    }

    public Date convertToDate(String date) throws IllegalArgumentException {
        Date result = null;
        if (StringUtils.isNotBlank(date)) {
            try {
                result = DATE_FORMAT.parse(date);
            } catch (ParseException e) {
                logger.error(e.getMessage());
            }
        } else {
            throw new IllegalArgumentException("input date is not correct");
        }
        return result;
    }


    @Transactional
    public List<User> getAllProviderEmployee(Long idOrganization) {
        return userRepository.getAllProviderUsers(Roles.PROVIDER_EMPLOYEE.name(), idOrganization);
    }
}