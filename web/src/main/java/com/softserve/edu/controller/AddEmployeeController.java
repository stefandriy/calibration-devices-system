package com.softserve.edu.controller;


import com.softserve.edu.controller.provider.util.UserDTO;
import com.softserve.edu.entity.Organization;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.user.UserRole;
import com.softserve.edu.repository.UserRepository;
import com.softserve.edu.service.SecurityUserDetailsService;
import com.softserve.edu.service.admin.OrganizationsService;
import com.softserve.edu.service.admin.UsersService;
import com.softserve.edu.service.provider.ProviderEmployeeService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
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
             @RequestBody UserDTO providerEmployee,
            @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {
            User newUser = new User();
        System.out.println(providerEmployee.toString());
            newUser.setAddress(providerEmployee.getAddress());
            newUser.setFirstName(providerEmployee.getFirstName());
            newUser.setLastName(providerEmployee.getLastName());
            newUser.setMiddleName(providerEmployee.getMiddleName());
            newUser.setEmail(providerEmployee.getEmail());
            newUser.setPhone(providerEmployee.getPhone());
            newUser.setUsername(providerEmployee.getUsername());
            newUser.setPassword(providerEmployee.getPassword());
        for (String tmp : providerEmployee.getUserRoles() ){
                UserRole userRole = userRepository.getUserRole(tmp);
            newUser.addUserRole(userRole);
        }


            Organization employeeOrganization = organizationsService.getOrganizationById(user.getOrganizationId());
            newUser.setOrganization(employeeOrganization);
        System.out.println(providerEmployee.toString());
             providerEmployeeService.addEmployee(newUser);
        return new ResponseEntity<HttpStatus>(HttpStatus.CREATED);
    }


}