package com.softserve.edu.controller.state_verificator;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.softserve.edu.controller.provider.ProviderEmployeeController;
import com.softserve.edu.entity.Organization;
import com.softserve.edu.entity.user.StateVerificatorEmployee;
import com.softserve.edu.service.SecurityUserDetailsService;
import com.softserve.edu.service.UserService;
import com.softserve.edu.service.admin.OrganizationsService;
import com.softserve.edu.service.state.verificator.StateVerificatorEmployeeService;

@RestController
@RequestMapping(value = "verificator/admin/users")
public class StateVerificatorEmployeeController {

	Logger logger = Logger.getLogger(ProviderEmployeeController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private OrganizationsService organizationsService;

	@Autowired
	private StateVerificatorEmployeeService stateVerificatorEmployeeService;
	
    @RequestMapping(value = "verificator", method = RequestMethod.GET)
    public String verification() {
        return "admin";
    }

	/**
	 * Check whereas {@code username} is available, i.e. it is possible to
	 * create new user with this {@code username}
	 *
	 * @param username  username         
	 * @return {@literal true} if {@code username} available or else  {@literal false}     
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
			@RequestBody StateVerificatorEmployee stateVerificatorEmployee,
			@AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {
		Organization employeeOrganization = organizationsService.getOrganizationById(user.getOrganizationId());
		stateVerificatorEmployee.setOrganization(employeeOrganization);
		
		stateVerificatorEmployeeService.addEmployee(stateVerificatorEmployee);

		return new ResponseEntity<HttpStatus>(HttpStatus.CREATED);
	}
}
