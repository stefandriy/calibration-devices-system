package com.softserve.edu.controller.calibrator;

import com.softserve.edu.controller.provider.ProviderEmployeeController;
import com.softserve.edu.dto.admin.UsersPageItem;
import com.softserve.edu.entity.Organization;
import com.softserve.edu.entity.user.CalibratorEmployee;
import com.softserve.edu.service.SecurityUserDetailsService;
import com.softserve.edu.service.admin.OrganizationsService;
import com.softserve.edu.service.admin.UsersService;
import com.softserve.edu.service.calibrator.CalibratorEmployeeService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "calibrator/admin/users/")
public class CalibratorEmployeeController {

        Logger logger = Logger.getLogger(ProviderEmployeeController.class);

        @Autowired
        private UsersService userService;

        @Autowired
        private OrganizationsService organizationsService;

        @Autowired
        private CalibratorEmployeeService calibratorEmployeeService;

        
        /**
         * Spatial security service
         * Find the role of the login user
         * @return role
         */

        @RequestMapping(value = "verificator", method = RequestMethod.GET)
        public String verification(@AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {
            return calibratorEmployeeService.findByUserame(user.getUsername()).getRole();
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
            @RequestBody CalibratorEmployee calibratorEmployee,
            @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {
        Organization employeeOrganization = organizationsService.getOrganizationById(user.getOrganizationId());
        calibratorEmployee.setOrganization(employeeOrganization);
        calibratorEmployeeService.addEmployee(calibratorEmployee);

        return new ResponseEntity<HttpStatus>(HttpStatus.CREATED);
    }


    @RequestMapping(value = "{pageNumber}/{itemsPerPage}/{search}", method = RequestMethod.GET)
    public Page<UsersPageItem> pageSearchUsers(
            @PathVariable Integer pageNumber,
            @PathVariable Integer itemsPerPage,
            @PathVariable String search) {
        Page<UsersPageItem> page =  null;
        return  page;
    }


    @RequestMapping(value = "{pageNumber}/{itemsPerPage}", method = RequestMethod.GET)
    public Page<UsersPageItem> getUsersPage(@PathVariable Integer pageNumber,
                                            @PathVariable Integer itemsPerPage) {
        return pageSearchUsers(pageNumber, itemsPerPage, null);
    }
}


