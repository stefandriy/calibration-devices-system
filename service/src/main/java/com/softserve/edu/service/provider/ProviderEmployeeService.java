package com.softserve.edu.service.provider;

import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.user.UserRole;
import com.softserve.edu.repository.UserRepository;

import com.softserve.edu.repository.UserRoleRepository;
import com.softserve.edu.service.utils.ListToPageTransformer;
import com.softserve.edu.service.utils.ProviderEmployeeQuary;
import com.softserve.edu.service.verification.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProviderEmployeeService {
    @Autowired
    private UserRepository providerEmployeeRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private VerificationService verificationService;
    @Transactional
    public void addEmployee(User providerEmployee) {
        String passwordEncoded = new BCryptPasswordEncoder().encode(providerEmployee.getPassword());
        providerEmployee.setPassword(passwordEncoded);
        UserRole r= userRoleRepository.findByRole("PROVIDER_EMPLOYEE");
        providerEmployee.getUserRoles().add(r);
        providerEmployee.setCountOfWork(0l);       //assign count of work to zero
        providerEmployeeRepository.save(providerEmployee);

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

    @Transactional
    public String getRoleByUserName(String username) {
        return providerEmployeeRepository.getRoleByUserName(username);
    }

    @Transactional
    public ListToPageTransformer<User>
    findPageOfAllProviderEmployeeAndCriteriaSearch(int pageNumber, int itemsPerPage,long idOrganization, String userName,String role,String firstName,String lastName, String organization,
                                                   String telephone, Long numberOfWorks) {

        CriteriaQuery<User> criteriaQuery = ProviderEmployeeQuary.buildSearchQuery(userName, role, firstName,
                lastName, organization, telephone, numberOfWorks, em, idOrganization);

        Long count = em.createQuery(ProviderEmployeeQuary.buildCountQuery(userName, role, firstName,
                lastName, organization, telephone,numberOfWorks,idOrganization, em)).getSingleResult();

        TypedQuery<User> typedQuery = em.createQuery(criteriaQuery);
        typedQuery.setFirstResult((pageNumber - 1) * itemsPerPage);
        typedQuery.setMaxResults(itemsPerPage);
        List<User> providerEmployeeList = typedQuery.getResultList();

        ListToPageTransformer<User> result = new ListToPageTransformer<User>();
        result.setContent(providerEmployeeList);
        result.setTotalItems(count);
        return result;
    }

}
