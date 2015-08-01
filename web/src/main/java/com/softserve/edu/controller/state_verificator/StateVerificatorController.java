package com.softserve.edu.controller.state_verificator;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.softserve.edu.controller.provider.util.VerificationPageDTOTransformer;
import com.softserve.edu.dto.ArchiveVerificationsSearch;
import com.softserve.edu.dto.CalibrationTestDTO;
import com.softserve.edu.dto.NewVerificationsSearch;
import com.softserve.edu.dto.PageDTO;
import com.softserve.edu.dto.provider.VerificationDTO;
import com.softserve.edu.dto.provider.VerificationPageDTO;
import com.softserve.edu.dto.provider.VerificationReadStatusUpdateDTO;
import com.softserve.edu.dto.state_verificator.VerificationUpdatingDTO;
import com.softserve.edu.entity.CalibrationTest;
import com.softserve.edu.entity.Organization;
import com.softserve.edu.entity.Verification;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.util.Status;
import com.softserve.edu.service.CalibrationTestService;
import com.softserve.edu.service.SecurityUserDetailsService;
import com.softserve.edu.service.calibrator.CalibratorService;
import com.softserve.edu.service.provider.ProviderService;
import com.softserve.edu.service.state.verificator.StateVerificatorEmployeeService;
import com.softserve.edu.service.state.verificator.StateVerificatorEmployeeService;
import com.softserve.edu.service.state.verificator.StateVerificatorService;
import com.softserve.edu.service.utils.ListToPageTransformer;
import com.softserve.edu.service.utils.ListToPageTransformer;
import com.softserve.edu.service.verification.VerificationService;

@RestController
@RequestMapping(value = "/verificator/verifications/")
public class StateVerificatorController {

    @Autowired
    VerificationService verificationService;

    @Autowired
    CalibrationTestService testService;

    @Autowired
    ProviderService providerService;

    @Autowired
    CalibratorService calibratorService;

    @Autowired
    StateVerificatorEmployeeService verificatorEmployeeService;

    @Autowired
    StateVerificatorService verificatorService;
    
    @Autowired
    StateVerificatorEmployeeService employeeService;

    private final Logger logger = Logger.getLogger(StateVerificatorController.class);

    @RequestMapping(value = "new/{pageNumber}/{itemsPerPage}", method = RequestMethod.GET)
    public PageDTO<VerificationPageDTO> getPageOfAllSentVerificationsByStateVerificatorIdAndSearch(
            @PathVariable Integer pageNumber,
            @PathVariable Integer itemsPerPage,
            NewVerificationsSearch searchData,
            @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails employeeUser) {

        User verificatorEmployee = verificatorEmployeeService.oneProviderEmployee(employeeUser.getUsername());
        ListToPageTransformer<Verification> queryResult = verificationService.findPageOfVerificationsByVerificatorIdAndCriteriaSearch(
                employeeUser.getOrganizationId(),
                pageNumber,
                itemsPerPage,
                searchData.getFormattedDate(),
                searchData.getIdText(),
                searchData.getLastNameText(),
                searchData.getStreetText(),
                searchData.getStatus(),
                verificatorEmployee
        );
        List<VerificationPageDTO> content = VerificationPageDTOTransformer.toDtoFromList(queryResult.getContent());
        return new PageDTO<VerificationPageDTO>(queryResult.getTotalItems(), content);
    }


    /**
     * Find providers by district which correspond stateVerificator district
     *
     * @return provider
     */
    @RequestMapping(value = "new/providers", method = RequestMethod.GET)
    public List<Organization> getMatchingVerificators(@AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {
        return providerService.findByDistrict(verificatorService.findById(user.getOrganizationId()).getAddress().getDistrict(), "PROVIDER");
    }

    @RequestMapping(value = "new/count/verificator", method = RequestMethod.GET)
    public Long getCountOfNewVerificationsByStateVerificatorId(@AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) { 
        if( user != null) {
        	return verificationService.findCountOfNewVerificationsByStateVerificatorId(user.getOrganizationId());	
        } else {
        	return null;
        }
    }

    @RequestMapping(value = "new/read", method = RequestMethod.PUT)
    public void markVerificationAsRead(@RequestBody VerificationReadStatusUpdateDTO verificationDto) {
        verificationService.updateVerificationReadStatus(verificationDto.getVerificationId(), verificationDto.getReadStatus());
    }

    @RequestMapping(value = "new/update", method = RequestMethod.PUT)
    public void sendVerification(@RequestBody VerificationUpdatingDTO verificationUpdatingDTO) {
        for (String verificationId : verificationUpdatingDTO.getIdsOfVerifications()) {
            Long idProvider = verificationUpdatingDTO.getIdsOfProviders();
            Organization provider = providerService.findById(idProvider);
            verificationService.sendVerificationTo(verificationId, provider, Status.TEST_OK);
        }
    }

    @RequestMapping(value = "new/notOk", method = RequestMethod.PUT)
    public void sendWithNotOkStatus(@RequestBody VerificationUpdatingDTO verificationUpdatingDTO) {
        for (String verificationId : verificationUpdatingDTO.getIdsOfVerifications()) {
            Long idProvider = verificationUpdatingDTO.getIdsOfProviders();
            Organization provider = providerService.findById(idProvider);
            verificationService.sendVerificationTo(verificationId, provider, Status.TEST_NOK);
        }
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


    @RequestMapping(value = "show/{verificationId}", method = RequestMethod.GET)
    public CalibrationTestDTO getCalibraionTestDetails(@PathVariable String verificationId) {
        CalibrationTest calibrationTest = testService.findByVerificationId(verificationId);
        return new CalibrationTestDTO(calibrationTest);

    }
    
    @RequestMapping(value = "archive/{pageNumber}/{itemsPerPage}", method = RequestMethod.GET)
    public PageDTO<VerificationPageDTO> getPageOfArchivalVerificationsByOrganizationId(@PathVariable Integer pageNumber, @PathVariable Integer itemsPerPage,
                                                                                       ArchiveVerificationsSearch searchData, @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails employeeUser) {

      User verificatorEmployee = employeeService.oneProviderEmployee(employeeUser.getUsername());
      ListToPageTransformer<Verification> queryResult = verificationService.findPageOfArchiveVerificationsByVerificatorId(
              employeeUser.getOrganizationId(),
              pageNumber,
              itemsPerPage,
              searchData.getFormattedDate(),
              searchData.getIdText(),
              searchData.getLastNameText(),
              searchData.getStreetText(),
              searchData.getStatus(),
              searchData.getEmployee(),
              verificatorEmployee
      );
      List<VerificationPageDTO> content = VerificationPageDTOTransformer.toDtoFromList(queryResult.getContent());
      return new PageDTO<VerificationPageDTO>(queryResult.getTotalItems(), content);
  }
    
    @RequestMapping(value = "archive/{verificationId}", method = RequestMethod.GET)
    public VerificationDTO getArchivalVerificationDetailsById( @PathVariable String verificationId, @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {
    	Verification verification = verificationService.findByIdAndStateVerificatorId(verificationId, user.getOrganizationId());
    	return new VerificationDTO( verification.getClientData(), verification.getId(), verification.getInitialDate(),
                verification.getExpirationDate(),verification.getStatus(), verification.getCalibrator(),
                verification.getCalibratorEmployee(), verification.getDevice(),verification.getProvider(),
                verification.getProviderEmployee(), verification.getStateVerificator(),
                verification.getStateVerificatorEmployee()
        );
    }


}
