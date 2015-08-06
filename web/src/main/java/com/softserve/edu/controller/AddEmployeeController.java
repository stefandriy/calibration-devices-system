package com.softserve.edu.controller;


import com.softserve.edu.controller.provider.util.UserDTO;
import com.softserve.edu.controller.provider.util.VerificationPageDTOTransformer;
import com.softserve.edu.dto.PageDTO;
import com.softserve.edu.dto.admin.UsersPageItem;
import com.softserve.edu.dto.provider.VerificationPageDTO;
import com.softserve.edu.entity.AddEmployeeBuilderNew;
import com.softserve.edu.entity.Organization;
import com.softserve.edu.entity.Verification;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.user.UserRole;
import com.softserve.edu.entity.util.Roles;
import com.softserve.edu.repository.UserRepository;
import com.softserve.edu.service.SecurityUserDetailsService;
import com.softserve.edu.service.admin.OrganizationsService;
import com.softserve.edu.service.admin.UsersService;
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

@RestController
@RequestMapping(value = "employee/admin/users/")
public class AddEmployeeController {

    Logger logger = Logger.getLogger(AddEmployeeController.class);

    @Autowired
    private UsersService userService;

    @Autowired
    private OrganizationsService organizationsService;

    @Autowired
    private ProviderEmployeeService providerEmployeeService;

    @Autowired
    private UserRepository userRepository;

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
    public List<UserRole> verification(@AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {
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

    @RequestMapping(value = "getUser/{username}", method = RequestMethod.GET)
    public UserDTO userEmployeeEdit(@PathVariable String username) {
        temporalUser = providerEmployeeService.oneProviderEmployee(username);
        UserDTO userFromDataBase = new UserDTO();
        userFromDataBase.setFirstName(temporalUser.getFirstName());
        userFromDataBase.setLastName(temporalUser.getLastName());
        userFromDataBase.setMiddleName(temporalUser.getMiddleName());
        userFromDataBase.setPhone(temporalUser.getPhone());
        userFromDataBase.setEmail(temporalUser.getEmail());
        userFromDataBase.setAddress(temporalUser.getAddress());
        userFromDataBase.setUsername(temporalUser.getUsername());
        userFromDataBase.setIsAvaliable(temporalUser.getIsAvaliable());
        userFromDataBase.setUserRoles(new HashSet<String>(userService.getRoles(username)));
        return userFromDataBase;
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public ResponseEntity<HttpStatus> updateEmployee(
            @RequestBody UserDTO providerEmployee,
            @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {
        User newUser = providerEmployeeService.oneProviderEmployee(temporalUser.getUsername());
        if (providerEmployee.getIsAvaliable().equals(false)) {
            newUser.setIsAvaliable(providerEmployee.getIsAvaliable());
            providerEmployeeService.updateEmployee(newUser);
            return new ResponseEntity<HttpStatus>(HttpStatus.CREATED);
        } else {
            newUser.setIsAvaliable(true);
        }
        newUser.setFirstName(providerEmployee.getFirstName());
        newUser.setLastName(providerEmployee.getLastName());
        newUser.setMiddleName(providerEmployee.getMiddleName());
        newUser.setEmail(providerEmployee.getEmail());
        newUser.setPhone(providerEmployee.getPhone());
        newUser.setUsername(providerEmployee.getUsername());
        newUser.setAddress(providerEmployee.getAddress().getDistrict() != null ?
                providerEmployee.getAddress() : newUser.getAddress());
        String p = providerEmployee.getPassword();
        newUser.setPassword(providerEmployee.getPassword() != null && providerEmployee.getPassword().equals("generate") ?
                "generate" : newUser.getPassword());
        newUser.deleteAllUsersRoles();
        for (String tmp : providerEmployee.getUserRoles()) {
            UserRole userRole = userRepository.getUserRole(tmp);
            newUser.addUserRole(userRole);
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
                .email(employee.getEmail())
                .address(employee.getAddress())
                .isAveliable(employee.getIsAvaliable())
                .build();
        for (String tmp : employee.getUserRoles()) {
            UserRole userRole = userRepository.getUserRole(tmp);
            newUser.addUserRole(userRole);
        }
        newUser.setOrganization(organizationsService.getOrganizationById(user.getOrganizationId()));
        providerEmployeeService.addEmployee(newUser);
        return new ResponseEntity<HttpStatus>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "capacityOfEmployee/{username}", method = RequestMethod.GET)
    public PageDTO<VerificationPageDTO> capacityEmployeeData(
            @PathVariable String username) {
        List<String> role = userService.getRoles(username);
        List<Verification> list = null;
        if (role.contains(Roles.PROVIDER_EMPLOYEE.name())) {
            list = verificationProviderEmployeeService.getVerificationListbyProviderEmployee(username);
        }
        if (role.contains(Roles.CALIBRATOR_EMPLOYEE.name())) {
            list = verificationProviderEmployeeService.getVerificationListbyCalibratormployee(username);
        }

        List<VerificationPageDTO> content = VerificationPageDTOTransformer.toDtoFromList(list);
        return new PageDTO<>(content);
    }

    @RequestMapping(value = "{pageNumber}/{itemsPerPage}", method = RequestMethod.GET)
    public PageDTO<UsersPageItem> getPaginationUsers(
            @PathVariable Integer pageNumber,
            @PathVariable Integer itemsPerPage,
            UsersPageItem usersPageItem,
            @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {
        Long idOrganization = user.getOrganizationId();
        ListToPageTransformer<User> queryResult = providerEmployeeService.findPageOfAllProviderEmployeeAndCriteriaSearch(
                pageNumber, itemsPerPage, idOrganization, usersPageItem.getUsername(), usersPageItem.getRole(),
                usersPageItem.getFirstName(), usersPageItem.getLastName(), usersPageItem.getOrganization(),
                usersPageItem.getPhone());
        List<UsersPageItem> resultList = toDTOFromListProviderEmployee(queryResult);
        return new PageDTO<UsersPageItem>(queryResult.getTotalItems(), resultList);

    }

    private List<UsersPageItem> toDTOFromListProviderEmployee(ListToPageTransformer<User> queryResult) {
        List<UsersPageItem> resultList = new ArrayList<UsersPageItem>();
        for (User providerEmployee : queryResult.getContent()) {
            resultList.add(new UsersPageItem(
                            providerEmployee.getUsername(),
                            userService.getRoles(providerEmployee.getUsername()),
                            providerEmployee.getFirstName(),
                            providerEmployee.getLastName(),
                            providerEmployee.getMiddleName(),
                            providerEmployee.getPhone(),
                            providerEmployee.getOrganization().getName(),
                            verificationProviderEmployeeService.countByProviderEmployeeTasks(providerEmployee.getUsername()),
                            verificationProviderEmployeeService.countByCalibratorEmployeeTasks(providerEmployee.getUsername()),
                            providerEmployee.getIsAvaliable()

                    )
            );
        }
        return resultList;
    }


}