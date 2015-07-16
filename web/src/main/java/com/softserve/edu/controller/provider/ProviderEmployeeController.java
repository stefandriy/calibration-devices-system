package com.softserve.edu.controller.provider;


import com.softserve.edu.controller.provider.util.ProviderEmployeePageDTOTransformer;
import com.softserve.edu.controller.provider.util.VerificationPageDTOTransformer;
import com.softserve.edu.dto.PageDTO;
import com.softserve.edu.dto.admin.UsersPageItem;
import com.softserve.edu.dto.provider.VerificationPageDTO;
import com.softserve.edu.entity.Organization;
import com.softserve.edu.entity.Verification;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.service.SecurityUserDetailsService;
import com.softserve.edu.service.admin.OrganizationsService;
import com.softserve.edu.service.admin.UsersService;
import com.softserve.edu.service.provider.ProviderEmployeeService;
import com.softserve.edu.service.provider.ProviderService;
import com.softserve.edu.service.utils.ListToPageTransformer;
import com.softserve.edu.service.verification.VerificationProviderEmployeeService;
import com.softserve.edu.service.verification.VerificationService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
     * @return role
     */

    @RequestMapping(value = "verificator", method = RequestMethod.GET)
    public String verification(@AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {
       String s= providerEmployeeService.getRoleByUserName(user.getUsername());
        return s;
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

//    @RequestMapping(value = "{pageNumber}/{itemsPerPage}/{idOrganization}/{search}", method = RequestMethod.GET)
//         public PageDTO<UsersPageItem> pageSearchUsers(
//            @PathVariable Integer pageNumber,
//            @PathVariable Integer itemsPerPage,
//            @PathVariable Long idOrganization,
//            @PathVariable String search) {
//        Page<UsersPageItem> page = providerEmployeeService
//                .getUsersPagination(idOrganization, pageNumber, itemsPerPage, search,"PROVIDER_EMPLOYEE")
//                .map(
//                        user -> {
//                            UsersPageItem usPage = null;
//                            if (user instanceof User) {
//                                usPage = new UsersPageItem();
//                                usPage.setUsername(user.getUsername());
//                                usPage.setRole(providerEmployeeService.getRoleByUserName(usPage.getUsername()));
//                                usPage.setFirstName(((User) user).getFirstName());
//                                usPage.setLastName(((User) user).getLastName());
//                                usPage.setOrganization(((User) user).getOrganization().getName());
//                                usPage.setPhone(((User) user).getPhone());
//                                usPage.setCountOfVarification(verificationService.countByProviderEmployeeTasks(user.getUsername()));
//                            }
//                            return usPage;
//                        }
//                );
//        return new PageDTO<>(page.getTotalElements(), page.getContent(), idOrganization);
//    }

//    @RequestMapping(value = "{pageNumber}/{itemsPerPage}/{idOrganization}", method = RequestMethod.GET)
//    public PageDTO<UsersPageItem> getUsersPage(
//            @PathVariable Integer pageNumber,
//            @PathVariable Integer itemsPerPage,
//            @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {
//        Long idOrganization = user.getOrganizationId();
//        return pageSearchUsers(pageNumber, itemsPerPage, idOrganization, null);
//    }

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
       // String role = userService.getRoleByUserName(user.getUsername());
        Long idOrganization = user.getOrganizationId();
        ListToPageTransformer<User> queryResult = providerEmployeeService.findPageOfAllProviderEmployeeAndCriteriaSearch(
                pageNumber, itemsPerPage, idOrganization, usersPageItem.getUsername(), "PROVIDER_EMPLOYEE", usersPageItem.getFirstName(),
                usersPageItem.getLastName(), usersPageItem.getOrganization(), usersPageItem.getPhone(), usersPageItem.getCountOfVarification());

        List<UsersPageItem> content = ProviderEmployeePageDTOTransformer.toDtoFromList(queryResult.getContent(),"PROVIDER_EMPLOYEE");
        return new PageDTO<UsersPageItem>(queryResult.getTotalItems(), content);

    }
}
