package com.softserve.edu.service.provider;

import com.softserve.edu.entity.Verification;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.user.UserRole;
import com.softserve.edu.repository.UserRepository;
import com.softserve.edu.repository.UserRoleRepository;
import com.softserve.edu.service.utils.ListToPageTransformer;
import com.softserve.edu.service.utils.ProviderEmployeeGraphic;
import com.softserve.edu.service.utils.ProviderEmployeeQuary;
import com.softserve.edu.service.utils.TransformStringsToMonths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
    private UserRoleRepository userRoleRepository;

    @PersistenceContext
    private EntityManager em;


    @Transactional
    public void addEmployee(User providerEmployee) {
        String passwordEncoded = new BCryptPasswordEncoder().encode(providerEmployee.getPassword());
        providerEmployee.setPassword(passwordEncoded);
        UserRole r = userRoleRepository.findByRole("PROVIDER_EMPLOYEE");
        providerEmployee.getUserRoles().add(r);
        providerEmployeeRepository.save(providerEmployee);
        userRoleRepository.save(r);

    }

    @Transactional
    public Page<? extends User> getUsersPagination(Long idOrganization, int pageNumber, int itemsPerPage, String search, String role) {
        PageRequest pageRequest = new PageRequest(pageNumber - 1, itemsPerPage);
        if (search == null) {
            return providerEmployeeRepository.findByRoleAndOrganizationId(role, idOrganization, pageRequest);
        } else {
            return providerEmployeeRepository.findByOrganizationIdAndRoleAndLastNameLikeIgnoreCase(role, idOrganization, "%" + search + "%", pageRequest);
        }
    }

    @Transactional
    public User oneProviderEmployee(String username) {
        return providerEmployeeRepository.getUserByUserName(username);
    }

    @Transactional
    public List<User> getAllProviders(String role, Long id) {
        return providerEmployeeRepository.getAllProviderUsers(role, id);
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
    public String getRoleByUserName(String username) {
        return providerEmployeeRepository.getRoleByUserName(username);
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
    public List<ProviderEmployeeGraphic> getgraphicProviderEmployee(String fromDate, String toDate, Long idOrganization) throws ParseException {
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
            countOfWork = transformStringsToMonths.identifyProviderEmployee(arr,list);

            resultList.add(new ProviderEmployeeGraphic(employee.toString(), countOfWork, listMonths));
        }
        return resultList;
    }
}
