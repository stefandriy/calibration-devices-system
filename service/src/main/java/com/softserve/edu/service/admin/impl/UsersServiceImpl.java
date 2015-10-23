package com.softserve.edu.service.admin.impl;


import com.softserve.edu.entity.Address;
import com.softserve.edu.entity.enumeration.user.UserRole;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.util.AddEmployeeBuilder;
import com.softserve.edu.entity.util.ConvertUserRoleToString;
import com.softserve.edu.repository.UserRepository;
import com.softserve.edu.service.admin.UserService;
import com.softserve.edu.service.tool.MailService;
import com.softserve.edu.service.utils.ArchivalEmployeeQueryConstructorAdmin;
import com.softserve.edu.service.utils.ListToPageTransformer;
import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsersServiceImpl implements UserService  {

    @Autowired
    private MailService mail;

    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    private EntityManager em;
    /**
     * Check whereas user with {@code username} exist in database
     *
     * @param username must not be non {@literal null}
     * @return {@literal true} if user with {@code username} doesn't exist in
     * database, else {@literal false}
     */
    @Override
    public boolean existsWithUsername(String username) {
        return userRepository.findOne(username) == null;
    }

    @Override
    @Transactional
    public List<String> getRoles(String username){
        return ConvertUserRoleToString
                .convertToListString(userRepository.getRolesByUserName(username));
    }

    @Override
    @Transactional
    public ListToPageTransformer<User>
    findPageOfAllEmployees(int pageNumber, int itemsPerPage,  String userName,
                           String role, String firstName, String lastName, String organization,
                           String telephone,  String sortCriteria, String sortOrder){
        CriteriaQuery<User> criteriaQuery = ArchivalEmployeeQueryConstructorAdmin.buildSearchQuery(userName, role, firstName,
                lastName, organization, telephone, sortCriteria, sortOrder, em);

        Long count = em.createQuery(ArchivalEmployeeQueryConstructorAdmin.buildCountQuery(userName, role, firstName,
                lastName, organization, telephone, em)).getSingleResult();

        TypedQuery<User> typedQuery = em.createQuery(criteriaQuery);
        typedQuery.setFirstResult((pageNumber - 1) * itemsPerPage);
        typedQuery.setMaxResults(itemsPerPage);
        List<User> employeeList = typedQuery.getResultList();

        ListToPageTransformer<User> result = new ListToPageTransformer<>();
        result.setContent(employeeList);
        result.setTotalItems(count);

        return result;
    }

    @Override
    @Transactional
    public void addSysAdmin( String  username, String password, String firstName, String lastName, String middleName, String phone,
                             String email,  Address address, Boolean isAvailable) {

        User newUser = new AddEmployeeBuilder().username(username)
                .password(password)
                .firstName(firstName)
                .lastName(lastName)
                .middleName(middleName)
                .phone(phone)
                .email(email)
                .address(address)
                .setIsAvailable(isAvailable)
                .build();


            newUser.addRole(UserRole.SYS_ADMIN);


        String passwordEncoded = new BCryptPasswordEncoder().encode(newUser.getPassword());
        newUser.setPassword(passwordEncoded);

        userRepository.save(newUser);
    }


    @Override
    @Transactional
    public ListToPageTransformer<User> findAllSysAdmins() {

//        CriteriaBuilder cb = em.getCriteriaBuilder();
//
//        CriteriaQuery<User> criteriaQuery = cb.createQuery(User.class);
//                Root<User> root = criteriaQuery.from(User.class);
//        Predicate queryPredicate = cb.conjunction();
//        queryPredicate = cb.and(cb.isMember(UserRole.SYS_ADMIN, root.get("userRoles")), queryPredicate);
//        criteriaQuery.select(root).distinct(true);
//        criteriaQuery.where(queryPredicate);
//        TypedQuery<User> typedQuery = em.createQuery(criteriaQuery);
//        List<User> providerEmployeeList = typedQuery.getResultList();
//
//        ListToPageTransformer<User> result = new ListToPageTransformer<>();
//        result.setContent(providerEmployeeList);
//        result.setTotalItems(7L);
        userRepository.findByUserRoleAllIgnoreCase(UserRole.SYS_ADMIN);
        ListToPageTransformer<User> result = new ListToPageTransformer<>();
        result.setContent(userRepository.findByUserRoleAllIgnoreCase(UserRole.SYS_ADMIN).stream().distinct().collect(Collectors.toList()));
        result.setTotalItems(7L);

        return result;
    }

    @Override
    @Transactional
    public void deleteSysAdmin(String username) {
        userRepository.delete(username);
    }

    @Override
    @Transactional
    public void editSysAdmin( String  username, String password, String firstName, String lastName, String middleName, String phone,
                              String email,  Address address, Boolean isAvailable) {
        User sysAdmin = userRepository.findOne(username);

        sysAdmin.setAddress(address);
        sysAdmin.setEmail(email);
        sysAdmin.setFirstName(firstName);
        sysAdmin.setLastName(lastName);
        sysAdmin.setMiddleName(middleName);
        sysAdmin.setPhone(phone);
        sysAdmin.setIsAvailable(isAvailable);

        sysAdmin.setPassword(password != null && password.equals("generate") ? "generate" : sysAdmin.getPassword());

        if (sysAdmin.getPassword().equals("generate")) {
            String newPassword = RandomStringUtils.randomAlphanumeric(5);
            System.out.println(sysAdmin.getEmail());
            System.out.println(newPassword);
            mail.sendNewPasswordMail(sysAdmin.getEmail(), sysAdmin.getFirstName(), newPassword);
            String passwordEncoded = new BCryptPasswordEncoder().encode(newPassword);
            sysAdmin.setPassword(passwordEncoded);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<User> findByOrganizationId(Long organizationId, int pageNumber, int itemsPerPage) {
        return IteratorUtils.toList(
                userRepository.findByOrganizationId(organizationId, new PageRequest(pageNumber, itemsPerPage)).iterator()
        );
    }

    @Override
    public Long countVerifications(User user) {
        String username = user.getUsername();
        return user
                .getUserRoles()
                .stream()
                .mapToLong(userRole -> userRepository.countEmployeeVerifications(userRole, username))
                .sum();
    }

    @Override
    public User findOne(String username) {
        return userRepository.findOne(username);
    }
}
