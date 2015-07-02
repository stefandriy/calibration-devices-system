package com.softserve.edu.controller.calibrator;

import com.softserve.edu.controller.provider.ProviderEmployeeController;
import com.softserve.edu.entity.Organization;
import com.softserve.edu.entity.user.CalibratorEmployee;
import com.softserve.edu.service.SecurityUserDetailsService;
import com.softserve.edu.service.UserService;
import com.softserve.edu.service.admin.OrganizationsService;
import com.softserve.edu.service.calibrator.CalibratorEmployeeService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "calibrator/admin/users/")
public class CalibratorEmployeeController {

        Logger logger = Logger.getLogger(ProviderEmployeeController.class);

        @Autowired
        private UserService userService;

        @Autowired
        private OrganizationsService organizationsService;

        @Autowired
        private CalibratorEmployeeService calibratorEmployeeService;
        
        @RequestMapping(value = "verificator", method = RequestMethod.GET)
        public String verification() {
        	System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
            return "admin";
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
            Organization employeeOrganization = organizationsService.findById(user.getOrganizationId());
            calibratorEmployee.setOrganization(employeeOrganization);
            calibratorEmployeeService.addEmployee(calibratorEmployee);

            return new ResponseEntity<HttpStatus>(HttpStatus.CREATED);
        }
}


