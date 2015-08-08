package com.softserve.edu.controller.admin;

import com.softserve.edu.dto.PageDTO;
import com.softserve.edu.dto.admin.UsersPageItem;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.service.SecurityUserDetailsService;
import com.softserve.edu.service.admin.UsersService;
import com.softserve.edu.service.provider.ProviderEmployeeService;
import com.softserve.edu.service.utils.ListToPageTransformer;
import com.softserve.edu.service.verification.VerificationProviderEmployeeService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping(value = "/admin/users/")
public class UserController {

    @Autowired
    private UsersService userService;

    @Autowired
    private ProviderEmployeeService providerEmployeeService;

    @Autowired
    private VerificationProviderEmployeeService verificationProviderEmployeeService;

    Logger logger = Logger.getLogger(UserController.class);

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

    @RequestMapping(value = "{pageNumber}/{itemsPerPage}/{fieldToSort}", method = RequestMethod.GET)
    public PageDTO<UsersPageItem> getPaginationUsers(
            @PathVariable Integer pageNumber,
            @PathVariable Integer itemsPerPage,
            @PathVariable String fieldToSort,
            UsersPageItem search,
            @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {
        ListToPageTransformer<User> queryResult = providerEmployeeService.findPageOfAllProviderEmployeeAndCriteriaSearch(
                pageNumber, itemsPerPage, null, search.getUsername(), search.getRole(),
                search.getFirstName(), search.getLastName(), search.getOrganization(),
                search.getPhone(), fieldToSort);
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
                            providerEmployee.getIsAvaliable())
            );
        }
        return resultList;
    }

}
