package com.softserve.edu.controller.provider;


import com.softserve.edu.controller.provider.util.VerificationPageDTOTransformer;
import com.softserve.edu.dto.PageDTO;
import com.softserve.edu.dto.admin.UsersPageItem;
import com.softserve.edu.dto.provider.VerificationPageDTO;
import com.softserve.edu.entity.Organization;
import com.softserve.edu.entity.Verification;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.user.UserRole;
import com.softserve.edu.service.SecurityUserDetailsService;
import com.softserve.edu.service.admin.OrganizationsService;
import com.softserve.edu.service.admin.UsersService;
import com.softserve.edu.service.provider.ProviderEmployeeService;
import com.softserve.edu.service.utils.DataDtoField;
import com.softserve.edu.service.utils.ListToPageTransformer;
import com.softserve.edu.service.utils.ProviderEmployeeGraphic;
import com.softserve.edu.service.verification.VerificationProviderEmployeeService;
import com.softserve.edu.service.verification.VerificationService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "provider/admin/users/")
public class ProviderEmployeeController {

    Logger logger = Logger.getLogger(ProviderEmployeeController.class);

    @Autowired
    private UsersService userService;

    @Autowired
    private OrganizationsService organizationsService;

    @Autowired
    private ProviderEmployeeService providerEmployeeService;

    @Autowired
    private VerificationService verificationService;

    @Autowired
    private VerificationProviderEmployeeService verificationProviderEmployeeService;


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

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public ResponseEntity<HttpStatus> addEmployee(
            @RequestBody User providerEmployee,
            @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {

        Organization employeeOrganization = organizationsService.getOrganizationById(user.getOrganizationId());
        providerEmployee.setOrganization(employeeOrganization);
        providerEmployeeService.addEmployee(providerEmployee);
        return new ResponseEntity<HttpStatus>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "capacityOfEmployee/{username}", method = RequestMethod.GET)
    public PageDTO<VerificationPageDTO> getPaginationUsers(
            @PathVariable String username) {
        List<Verification> list = verificationProviderEmployeeService.getVerificationListbyProviderEmployee(username);
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
                pageNumber, itemsPerPage, idOrganization, usersPageItem.getUsername(), usersPageItem.getRole(), usersPageItem.getFirstName(),
                usersPageItem.getLastName(), usersPageItem.getOrganization(), usersPageItem.getPhone());
        List<UsersPageItem> resultList = toDTOFromListProviderEmployee(queryResult);

         return new PageDTO<UsersPageItem>(queryResult.getTotalItems(), resultList);

    }
    @RequestMapping(value = "graphic", method = RequestMethod.GET)
    public List<ProviderEmployeeGraphic>  graphic
            ( DataDtoField dataDtoField,
             @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user)
    {
        Long idOrganization = user.getOrganizationId();
        List<ProviderEmployeeGraphic> list= providerEmployeeService.getgraphicProviderEmployee(dataDtoField.getFromDate(),dataDtoField.getToDate(),idOrganization) ;
        return list;
    }




    private List<UsersPageItem> toDTOFromListProviderEmployee(ListToPageTransformer<User> queryResult) {
        List<UsersPageItem> resultList = new ArrayList<UsersPageItem>();
        for (User providerEmployee : queryResult.getContent()) {
            resultList.add(new UsersPageItem(
                            providerEmployee.getUsername(),
                            userService.getRoleByUserName(providerEmployee.getUsername()),
                            providerEmployee.getFirstName(),
                            providerEmployee.getLastName(),
                            providerEmployee.getMiddleName(),
                            providerEmployee.getPhone(),
                            providerEmployee.getOrganization().getName(),
                            verificationProviderEmployeeService.countByProviderEmployeeTasks(providerEmployee.getUsername())
                    )
            );
        }
        return resultList;
    }
}
