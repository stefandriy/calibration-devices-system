package com.softserve.edu.controller;


import com.softserve.edu.controller.provider.util.UserDTO;
import com.softserve.edu.controller.provider.util.VerificationPageDTOTransformer;
import com.softserve.edu.dto.PageDTO;
import com.softserve.edu.dto.admin.UsersPageItem;
import com.softserve.edu.dto.provider.VerificationPageDTO;
import com.softserve.edu.entity.util.AddEmployeeBuilderNew;
import com.softserve.edu.entity.verification.Verification;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.enumeration.user.UserRole;
import com.softserve.edu.service.user.SecurityUserDetailsService;
import com.softserve.edu.service.admin.OrganizationService;
import com.softserve.edu.service.admin.UserService;
import com.softserve.edu.service.provider.ProviderEmployeeService;
import com.softserve.edu.service.utils.ListToPageTransformer;
import com.softserve.edu.service.verification.VerificationProviderEmployeeService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "employee/admin/users/")
public class EmployeeController {

    Logger logger = Logger.getLogger(EmployeeController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private OrganizationService organizationsService;

    @Autowired
    private ProviderEmployeeService providerEmployeeService;

    @Autowired
    private VerificationProviderEmployeeService verificationProviderEmployeeService;


    @RequestMapping(value = "organizationCapacity", method = RequestMethod.GET)
    public Integer getOrganizationCapacity(@AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {
        Long organizationId = user.getOrganizationId();
        return organizationsService.getOrganizationEmployeesCapacity(organizationId);
    }

    User temporalUser;

    /**
     * Spatial security service
     * Find the role of the login user
     *
     * @return role
     */
    @RequestMapping(value = "verificator", method = RequestMethod.GET)
    public List<String> verification(@AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {
        return providerEmployeeService.getRoleByUserNam(user.getUsername());
    }

    /**
     * Check whereas {@code username} is available,
     * i.e. it is possible to create new user with this {@code username}
     *
     * @param username username
     * @return {@literal true} if {@code username} available or else {@literal false}
     */
    @RequestMapping(value = "available/{username}", method = RequestMethod.GET)
    public Boolean isValidUsername(@PathVariable String username) {
        boolean isAvailable = false;
        if (username != null) {
            isAvailable = userService.existsWithUsername(username);
        }
        return isAvailable;
    }

    /**
     * Find user for update
     *
     * @param username
     * @return DTO
     */
    @RequestMapping(value = "getUser/{username}", method = RequestMethod.GET)
    public UserDTO userEmployeeEdit(@PathVariable String username) {
        temporalUser = providerEmployeeService.oneProviderEmployee(username);
        UserDTO userFromDataBase = new UserDTO();
        userFromDataBase.setFirstName(temporalUser.getFirstName());
        userFromDataBase.setLastName(temporalUser.getLastName());
        userFromDataBase.setMiddleName(temporalUser.getMiddleName());
        userFromDataBase.setPhone(temporalUser.getPhone());
        userFromDataBase.setSecondPhone(temporalUser.getSecondPhone());
        userFromDataBase.setEmail(temporalUser.getEmail());
        userFromDataBase.setAddress(temporalUser.getAddress());
        userFromDataBase.setUsername(temporalUser.getUsername());
        userFromDataBase.setIsAvaliable(temporalUser.getIsAvailable());
        userFromDataBase.setUserRoles(new HashSet<String>(userService.getRoles(username)));
        return userFromDataBase;
    }

    /**
     * Update user
     *
     * @param providerEmployee
     * @return status
     */

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public ResponseEntity<HttpStatus> updateEmployee(
            @RequestBody UserDTO providerEmployee) {

        User newUser = providerEmployeeService.oneProviderEmployee(providerEmployee.getUsername());

        if (!providerEmployee.getIsAvaliable()) {
            newUser.setIsAvailable(providerEmployee.getIsAvaliable());
            providerEmployeeService.updateEmployee(newUser);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            newUser.setIsAvailable(true);
        }
        newUser.setFirstName(providerEmployee.getFirstName());
        newUser.setLastName(providerEmployee.getLastName());
        newUser.setMiddleName(providerEmployee.getMiddleName());
        newUser.setEmail(providerEmployee.getEmail());
        newUser.setPhone(providerEmployee.getPhone());
        newUser.setSecondPhone(providerEmployee.getSecondPhone());
        newUser.setUsername(providerEmployee.getUsername());

        String password = providerEmployee.getPassword();
        if (password != null && password.equals("generate")) {
            newUser.setPassword("generate");
        }
        //else newUser.setPassword(newUser.getPassword());

        if (!providerEmployee.getUserRoles().isEmpty()) {
            newUser.removeAllRoles();

            for (String role : providerEmployee.getUserRoles()) {
                UserRole userRole = UserRole.valueOf(role);
                newUser.addRole(userRole);
            }
        }


        providerEmployeeService.updateEmployee(newUser);
        return new ResponseEntity<HttpStatus>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public ResponseEntity<HttpStatus> addEmployee(
            @RequestBody UserDTO employee,
            @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {
        User newUser = new AddEmployeeBuilderNew().username(employee.getUsername())
                .password(employee.getPassword())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .middleName(employee.getMiddleName())
                .phone(employee.getPhone())
                .secondPhone(employee.getSecondPhone())
                .email(employee.getEmail())
                .address(employee.getAddress())
                .isAvailable(employee.getIsAvaliable())
                .build();

        for (String role : employee.getUserRoles()) {
            UserRole userRole = UserRole.valueOf(role);
            newUser.addRole(userRole);
        }
        newUser.setOrganization(organizationsService.getOrganizationById(user.getOrganizationId()));
        providerEmployeeService.addEmployee(newUser);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "capacityOfEmployee/{username}", method = RequestMethod.GET)
    public PageDTO<VerificationPageDTO> capacityEmployeeData(
            @PathVariable String username) {
        List<String> role = userService.getRoles(username);
        List<Verification> list = null;
        if (role.contains(UserRole.PROVIDER_EMPLOYEE.name())) {
            list = verificationProviderEmployeeService.getVerificationListByProviderEmployee(username);
        }
        if (role.contains(UserRole.CALIBRATOR_EMPLOYEE.name())) {
            list = verificationProviderEmployeeService.getVerificationListByCalibratorEmployee(username);
        }
        List<VerificationPageDTO> content = VerificationPageDTOTransformer.toDtoFromList(list);
        return new PageDTO<>(content);
    }

    @RequestMapping(value = "{pageNumber}/{itemsPerPage}/{fieldToSort}", method = RequestMethod.GET)
    public PageDTO<UsersPageItem> getPaginationUsers(
            @PathVariable Integer pageNumber,
            @PathVariable Integer itemsPerPage,
            @PathVariable String fieldToSort,
            UsersPageItem search,
            @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {
        Long idOrganization = user.getOrganizationId();
        ListToPageTransformer<User> queryResult = providerEmployeeService.findPageOfAllProviderEmployeeAndCriteriaSearch(
                pageNumber, itemsPerPage, idOrganization, search.getUsername(), search.getRole(),
                search.getFirstName(), search.getLastName(), search.getOrganization(),
                search.getPhone(), search.getSecondPhone(), fieldToSort);
        List<UsersPageItem> resultList = toDTOFromListProviderEmployee(queryResult);
        return new PageDTO<>(queryResult.getTotalItems(), resultList);
    }

    /**
     * return data about admin employees.
     * return only employees, without admins.
     *
     * @param queryResult
     * @return page with employees of current admin.
     */
    private List<UsersPageItem> toDTOFromListProviderEmployee(ListToPageTransformer<User> queryResult) {
        List<UsersPageItem> resultList = new ArrayList<>();
        for (User providerEmployee : queryResult.getContent()) {

            //hide information about PROVIDER_ADMIN, CALIBRATOR_ADMIN, STATE_VERIFICATOR_ADMIN
            List<String> userRoles = userService.getRoles(providerEmployee.getUsername())
                    .stream()
                    .distinct()
                    .collect(Collectors.toList());

            boolean isProviderAdmin = userRoles.contains(UserRole.PROVIDER_ADMIN.name());
            boolean isCalibratorAdmin = userRoles.contains(UserRole.CALIBRATOR_ADMIN.name());
            boolean isStateVerificatorAdmin = userRoles.contains(UserRole.STATE_VERIFICATOR_ADMIN.name());

            if (!isProviderAdmin && !isCalibratorAdmin && !isStateVerificatorAdmin) {
                resultList.add(new UsersPageItem(
                                providerEmployee.getUsername(),
                                userRoles,
                                providerEmployee.getFirstName(),
                                providerEmployee.getLastName(),
                                providerEmployee.getMiddleName(),
                                providerEmployee.getPhone(),
                                providerEmployee.getSecondPhone(),
                                providerEmployee.getOrganization().getName(),
                                verificationProviderEmployeeService.countByProviderEmployeeTasks(providerEmployee.getUsername()),
                                verificationProviderEmployeeService.countByCalibratorEmployeeTasks(providerEmployee.getUsername()),
                                providerEmployee.getIsAvailable()
                        )
                );
            }
        }
        return resultList;
    }

    @RequestMapping(value = "/{pageNumber}/{itemsPerPage}", method = RequestMethod.GET)
    private List<UserInfoDTO> getUsersByOrganizationId(
            @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails userDetails,
            @PathVariable int pageNumber,
            @PathVariable int itemsPerPage
    ) {
        return userService.findByOrganizationId(userDetails.getOrganizationId(), pageNumber, itemsPerPage)
                .stream()
                .map(user -> new UserInfoDTO(
                                user.getUsername(),
                                user.getUserRoles()
                                        .stream()
                                        .map(UserRole::name)
                                        .collect(Collectors.toList()),
                                user.getFirstName(),
                                user.getLastName(),
                                user.getPhone(),
                                0L
                        )
                )
                .collect(Collectors.toList());
    }
}

