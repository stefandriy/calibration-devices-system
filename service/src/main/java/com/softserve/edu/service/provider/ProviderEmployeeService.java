package com.softserve.edu.service.provider;

import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.user.UserRole;
import com.softserve.edu.entity.util.Roles;
import com.softserve.edu.repository.UserRepository;
import com.softserve.edu.repository.UserRoleRepository;
import com.softserve.edu.service.MailService;
import com.softserve.edu.service.utils.*;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import java.text.ParseException;
import java.util.*;

@Service
public class ProviderEmployeeService {
    @Autowired
    private UserRepository providerEmployeeRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private MailService mail;

    @PersistenceContext
    private EntityManager em;


    @Transactional
    public void addEmployee(User providerEmployee) {
        String passwordEncoded = new BCryptPasswordEncoder().encode(providerEmployee.getPassword());
        providerEmployee.setPassword(passwordEncoded);
        providerEmployeeRepository.save(providerEmployee);
    }

    @Transactional
    public void updateEmployee(User providerEmployee) {
        if(providerEmployee.getPassword() == "generate") {
            Random rand = new Random();
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
            List<User> list = providerEmployeeRepository.getAllProviderUsers(Roles.PROVIDER_EMPLOYEE.name(),
                    employee.getOrganization().getId());
            providerListEmployee = EmployeeProvider.giveListOfProviders(list);
        } else {
            EmployeeProvider userPage = new EmployeeProvider(employee.getUsername(), employee.getFirstName(),
                    employee.getLastName(), employee.getMiddleName(),role.get(0));
            providerListEmployee.add(userPage);
        }
        return providerListEmployee;
    }

    @Transactional()
    public User findByUserame(String userName) {
        return providerEmployeeRepository.findByUsername(userName);
    }

    // two next methods is the same !!!!!!!

    @Transactional
    public List<UserRole> getRoleByUserNam(String username) {
        return providerEmployeeRepository.getRoleByUserNam(username);
    }


    @Transactional
    public ListToPageTransformer<User>
    findPageOfAllProviderEmployeeAndCriteriaSearch(int pageNumber, int itemsPerPage, long idOrganization, String userName,
                                                   String role, String firstName, String lastName, String organization,
                                                   String telephone) {
        CriteriaQuery<User> criteriaQuery = ProviderEmployeeQuary.buildSearchQuery(userName, role, firstName,
                lastName, organization, telephone, em, idOrganization);

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
    public List<ProviderEmployeeGraphic> getGraphicProviderEmployee(String fromDate, String toDate, Long idOrganization) throws ParseException {
        TransformStringsToMonths transformStringsToMonths = new TransformStringsToMonths();

        List<String> listMonths = transformStringsToMonths.transferToMonthArray(fromDate, toDate);
        Date dateFrom = transformStringsToMonths.convertToData(fromDate);
        Date dateTo = transformStringsToMonths.convertToData(toDate);
        String providerUsername = transformStringsToMonths.getQueryproviderUsername();
        String toGrafic = transformStringsToMonths.getQuerytoGrafic();
        int[] arr = transformStringsToMonths.parser(fromDate, toDate);

        List<ProviderEmployeeGraphic> resultList = graficBuilder(providerUsername, toGrafic, idOrganization,
                dateFrom, dateTo, arr, listMonths);
        return resultList;
    }


    public List<ProviderEmployeeGraphic> graficBuilder(String providerUsername, String toGrafic,
                                                       Long idOrganization, Date dateFrom, Date dateTo,
                                                       int[] arr, List<String> listMonths) {
        List<Object[]> list = null;
        List<Double> countOfWork = null;
        List<ProviderEmployeeGraphic> resultList = new ArrayList<>();
        TransformStringsToMonths transformStringsToMonths = new TransformStringsToMonths();
        Query queryEmployee = em.createNativeQuery(providerUsername);
        queryEmployee.setParameter(1, idOrganization);
        List empList = queryEmployee.getResultList();

        for (Object employee : empList) {
            Query quer = em.createNativeQuery(toGrafic);
            quer.setParameter(1, employee.toString());
            quer.setParameter(2, dateFrom);
            quer.setParameter(3, dateTo);
            list = quer.getResultList();
            countOfWork = transformStringsToMonths.identifyProviderEmployee(arr, list);

            resultList.add(new ProviderEmployeeGraphic(employee.toString(), countOfWork, listMonths));
        }
        return resultList;
    }
}
