package com.softserve.edu.controller.calibrator;

import com.softserve.edu.controller.provider.ProviderEmployeeController;
import com.softserve.edu.dto.provider.VerificationProviderEmployeeDTO;
import com.softserve.edu.entity.organization.Organization;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.service.user.SecurityUserDetailsService;
import com.softserve.edu.service.admin.OrganizationService;
import com.softserve.edu.service.admin.UserService;
import com.softserve.edu.service.calibrator.CalibratorEmployeeService;
import com.softserve.edu.service.calibrator.CalibratorService;
import com.softserve.edu.service.utils.EmployeeDTO;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

;


@RestController
@RequestMapping(value = "calibrator/admin/users/")
public class CalibratorEmployeeController {

    Logger logger = Logger.getLogger(ProviderEmployeeController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private OrganizationService organizationsService;

    @Autowired
    private CalibratorEmployeeService calibratorEmployeeService;

    @Autowired
    private CalibratorService calibratorService;


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
            @RequestBody User calibratorEmployee,
            @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {
        Organization employeeOrganization = organizationsService.getOrganizationById(user.getOrganizationId());
        calibratorEmployee.setOrganization(employeeOrganization);
        calibratorEmployeeService.addEmployee(calibratorEmployee);
        return new ResponseEntity<HttpStatus>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "new/calibratorEmployees", method = RequestMethod.GET)
    public List<EmployeeDTO> employeeCalibratorVerification(
            @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {
        User employee = calibratorService.oneCalibratorEmployee(user.getUsername());
        List<String> role = userService.getRoles(user.getUsername());
        return calibratorService.getAllCalibratorEmployee(role, employee);
    }

    @RequestMapping(value = "assign/calibratorEmployee", method = RequestMethod.PUT)
    public void assignCalibratorEmployee(@RequestBody VerificationProviderEmployeeDTO verificationProviderEmployeeDTO) {
        String userNameCalibrator = verificationProviderEmployeeDTO.getEmployeeCalibrator().getUsername();
        String idVerification = verificationProviderEmployeeDTO.getIdVerification();
        User employeeCalibrator = calibratorService.oneCalibratorEmployee(userNameCalibrator);
        calibratorService.assignCalibratorEmployee(idVerification, employeeCalibrator);
    }

    @RequestMapping(value = "remove/calibratorEmployee", method = RequestMethod.PUT)
    public void removeCalibratorEmployee(@RequestBody VerificationProviderEmployeeDTO verificationUpdatingDTO) {
        String idVerification = verificationUpdatingDTO.getIdVerification();
        calibratorService.assignCalibratorEmployee(idVerification, null);
    }

}


