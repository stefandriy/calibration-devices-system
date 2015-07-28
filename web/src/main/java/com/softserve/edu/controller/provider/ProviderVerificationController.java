package com.softserve.edu.controller.provider;

import com.softserve.edu.controller.provider.util.VerificationPageDTOTransformer;
import com.softserve.edu.dto.provider.*;
import com.softserve.edu.dto.ArchiveVerificationsSearch;
import com.softserve.edu.dto.NewVerificationsSearch;
import com.softserve.edu.dto.PageDTO;
import com.softserve.edu.entity.Organization;
import com.softserve.edu.entity.Verification;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.util.Status;
import com.softserve.edu.service.SecurityUserDetailsService;
import com.softserve.edu.service.admin.UsersService;
import com.softserve.edu.service.calibrator.CalibratorService;
import com.softserve.edu.service.provider.ProviderEmployeeService;
import com.softserve.edu.service.provider.ProviderService;
import com.softserve.edu.service.utils.*;
import com.softserve.edu.service.verification.VerificationProviderEmployeeService;
import com.softserve.edu.service.verification.VerificationService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/provider/verifications/")
public class ProviderVerificationController {

    @Autowired
    VerificationService verificationService;

    @Autowired
    ProviderService providerService;

    @Autowired
    ProviderEmployeeService providerEmployeeService;

    @Autowired
    CalibratorService calibratorService;

    @Autowired
    private UsersService userService;

    @Autowired
    VerificationProviderEmployeeService verificationProviderEmployeeService;

    private final Logger logger = Logger.getLogger(ProviderVerificationController.class);

    @RequestMapping(value = "archive/{pageNumber}/{itemsPerPage}", method = RequestMethod.GET)
    public PageDTO<VerificationPageDTO> getPageOfArchivalVerificationsByOrganizationId(@PathVariable Integer pageNumber, @PathVariable Integer itemsPerPage,
                                                                                       ArchiveVerificationsSearch searchData, @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails employeeUser) {

      User providerEmployee = providerEmployeeService.oneProviderEmployee(employeeUser.getUsername());
      ListToPageTransformer<Verification> queryResult = verificationService.findPageOfArchiveVerificationsByProviderId(
              employeeUser.getOrganizationId(),
              pageNumber,
              itemsPerPage,
              searchData.getFormattedDate(),
              searchData.getIdText(),
              searchData.getLastNameText(),
              searchData.getStreetText(),
              searchData.getStatus(),
              searchData.getEmployee(),
              providerEmployee
      );
      List<VerificationPageDTO> content = VerificationPageDTOTransformer.toDtoFromList(queryResult.getContent());
      return new PageDTO<VerificationPageDTO>(queryResult.getTotalItems(), content);
  }

    /**
     * Find page of verifications by specific criterias
     *
     * @param pageNumber
     * @param itemsPerPage
     * @param verifDate    (optional)
     * @param verifId      (optional)
     * @param lastName     (optional)
     * @param street       (optional)
     * @param employeeUser
     * @return PageDTO<VerificationPageDTO>
     */
    @RequestMapping(value = "new/{pageNumber}/{itemsPerPage}", method = RequestMethod.GET)
    public PageDTO<VerificationPageDTO> getPageOfAllSentVerificationsByProviderIdAndSearch(@PathVariable Integer pageNumber, @PathVariable Integer itemsPerPage,
                                                                                           NewVerificationsSearch searchData, @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails employeeUser) {

      User providerEmployee = providerEmployeeService.oneProviderEmployee(employeeUser.getUsername());
      ListToPageTransformer<Verification> queryResult = verificationService.findPageOfSentVerificationsByProviderIdAndCriteriaSearch(
              employeeUser.getOrganizationId(),
              pageNumber,
              itemsPerPage,
              searchData.getFormattedDate(),
              searchData.getIdText(),
              searchData.getLastNameText(),
              searchData.getStreetText(),
              searchData.getStatus(),
              providerEmployee
      );
      List<VerificationPageDTO> content = VerificationPageDTOTransformer.toDtoFromList(queryResult.getContent());
      return new PageDTO<VerificationPageDTO>(queryResult.getTotalItems(), content);
  }


    /**
     * Find count of new verifications that have Read Status "UNREAD"
     *
     * @return Long count
     */
    @RequestMapping(value = "new/count/provider", method = RequestMethod.GET)
    public Long getCountOfNewVerificationsByProviderId(@AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {
        if (user != null) {
            return verificationService.findCountOfNewVerificationsByProviderId(user.getOrganizationId());
        } else {
            return null;
        }
    }


    /**
     * Find calibrators by district which correspond provider district
     *
     * @return calibrator
     */
    @RequestMapping(value = "new/calibrators", method = RequestMethod.GET)
    public List<Organization> updateVerification(@AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {
        return calibratorService.findByDistrict(providerService.findById(user.getOrganizationId()).getAddress().getDistrict(), "CALIBRATOR");
    }


    @RequestMapping(value = "new/providerEmployees", method = RequestMethod.GET)
    public List<EmployeeProvider> employeeVerification(
            @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {
        User employee = providerEmployeeService.oneProviderEmployee(user.getUsername());
        List<String> role = userService.getRoles(user.getUsername());
        List<EmployeeProvider> providerListEmployee = providerEmployeeService
                .getAllProviders(role, employee);
        return providerListEmployee;
    }

    /**
     * Update verificationsproviderListEmployee
     */
    @RequestMapping(value = "new/update", method = RequestMethod.PUT)
    public void updateVerification(
            @RequestBody VerificationUpdatingDTO verificationUpdatingDTO) {

        for (String verificationId : verificationUpdatingDTO.getIdsOfVerifications()) {
            Long idCalibrator = verificationUpdatingDTO.getIdsOfCalibrators();
            Organization calibrator = calibratorService.findById(idCalibrator);
            verificationService.sendVerificationTo(verificationId, calibrator, Status.IN_PROGRESS);
        }
    }


    @RequestMapping(value = "new/reject", method = RequestMethod.PUT)
    public void rejectVerification(@RequestBody VerificationStatusUpdateDTO verificationReadStatusUpdateDTO) {
        verificationService.updateVerificationStatus(verificationReadStatusUpdateDTO.getVerificationId(),
                Status.valueOf(verificationReadStatusUpdateDTO.getStatus().toUpperCase()));
    }

    @RequestMapping(value = "new/accept", method = RequestMethod.PUT)
    public void acceptVerification(@RequestBody VerificationStatusUpdateDTO verificationReadStatusUpdateDTO) {
        verificationService.updateVerificationStatus(verificationReadStatusUpdateDTO.getVerificationId(),
                Status.valueOf(verificationReadStatusUpdateDTO.getStatus().toUpperCase()));
    }

    /**
     * change read status of verification when Provider user reads it
     *
     * @param verificationDto
     */
    @RequestMapping(value = "new/read", method = RequestMethod.PUT)
    public void markVerificationAsRead(@RequestBody VerificationReadStatusUpdateDTO verificationDto) {
        verificationService.updateVerificationReadStatus(verificationDto.getVerificationId(), verificationDto.getReadStatus());
    }

    @RequestMapping(value = "assign/providerEmployee", method = RequestMethod.PUT)
    public void assignProviderEmployee(@RequestBody VerificationProviderEmployeeDTO verificationProviderEmployeeDTO) {
        User providerEmployee = new User();
        System.err.println("assign controller");
        String idVerification = verificationProviderEmployeeDTO.getIdVerification();
        providerEmployee.setUsername(verificationProviderEmployeeDTO.getEmployeeProvider().getUsername());
        verificationProviderEmployeeService.assignProviderEmployee(idVerification, providerEmployee);
    }

    @RequestMapping(value = "remove/providerEmployee", method = RequestMethod.PUT)
    public void removeProviderEmployee(@RequestBody VerificationProviderEmployeeDTO verificationUpdatingDTO) {
        String idVerification = verificationUpdatingDTO.getIdVerification();
        verificationProviderEmployeeService.assignProviderEmployee(idVerification, null);
    }


    @RequestMapping(value = "new/{verificationId}", method = RequestMethod.GET)
    public VerificationDTO getNewVerificationDetailsById(@PathVariable String verificationId) {
        Verification verification = verificationService.findById(verificationId);
        if (verification != null) {
            return new VerificationDTO(verification.getClientData(), verification.getId(),
                    verification.getInitialDate(), verification.getExpirationDate(), verification.getStatus(),
                    verification.getCalibrator(), verification.getCalibratorEmployee(), verification.getDevice(),
                    verification.getProvider(), verification.getProviderEmployee(), verification.getStateVerificator(),
                    verification.getStateVerificatorEmployee());
        } else {
            return null;
        }
    }


    @RequestMapping(value = "archive/{verificationId}", method = RequestMethod.GET)
    public VerificationDTO getArchivalVerificationDetailsById( @PathVariable String verificationId, @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {

        Verification verification = verificationService.findByIdAndProviderId(verificationId, user.getOrganizationId());

        return new VerificationDTO(verification.getClientData(), verification.getId(), verification.getInitialDate(), verification.getExpirationDate(),
        							verification.getStatus(), verification.getCalibrator(), verification.getCalibratorEmployee(), verification.getDevice(),
        							verification.getProvider(), verification.getProviderEmployee(), verification.getStateVerificator(), verification.getStateVerificatorEmployee()
        );
    }
}
