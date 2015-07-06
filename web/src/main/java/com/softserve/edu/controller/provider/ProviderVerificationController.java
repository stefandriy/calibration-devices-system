package com.softserve.edu.controller.provider;

import com.softserve.edu.controller.provider.util.VerificationPageDTOTransformer;
import com.softserve.edu.dto.provider.VerificationDTO;
import com.softserve.edu.dto.provider.VerificationReadStatusUpdateDTO;
import com.softserve.edu.dto.provider.VerificationSearchDTO;
import com.softserve.edu.dto.provider.VerificationUpdatingDTO;
import com.softserve.edu.dto.PageDTO;
import com.softserve.edu.dto.admin.EmployeeProvider;
import com.softserve.edu.dto.application.ClientStageVerificationDTO;
import com.softserve.edu.dto.provider.VerificationPageDTO;
import com.softserve.edu.dto.provider.VerificationUpdatingDTO;
import com.softserve.edu.entity.Address;
import com.softserve.edu.entity.Calibrator;
import com.softserve.edu.entity.ClientData;
import com.softserve.edu.entity.Verification;
import com.softserve.edu.entity.user.ProviderEmployee;
import com.softserve.edu.service.SecurityUserDetailsService;
import com.softserve.edu.service.calibrator.CalibratorService;
import com.softserve.edu.service.provider.ProviderEmployeeService;
import com.softserve.edu.service.provider.ProviderService;
import com.softserve.edu.service.utils.ListToPageTransformer;
import com.softserve.edu.service.verification.VerificationService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

    private final Logger logger = Logger.getLogger(ProviderVerificationController.class);

    @RequestMapping(value = "archive/{pageNumber}/{itemsPerPage}", method = RequestMethod.GET)
    public PageDTO<VerificationPageDTO> getPageOfAllVerificationsByProviderId(
            @PathVariable Integer pageNumber,
            @PathVariable Integer itemsPerPage,
            @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {

        Page<VerificationPageDTO> page = VerificationPageDTOTransformer
                .toDTO(verificationService
                        .findPageOfAllVerificationsByProviderId(
                                user.getOrganizationId(),
                                pageNumber,
                                itemsPerPage
                        ));

        return new PageDTO<>(page.getTotalElements(), page.getContent());
    }

    @RequestMapping(value = "new/{pageNumber}/{itemsPerPage}", method = RequestMethod.GET)
    public PageDTO<VerificationPageDTO> getPageOfAllSentVerificationsByProviderId(
            @PathVariable Integer pageNumber,
            @PathVariable Integer itemsPerPage,
            @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {

        Page<VerificationPageDTO> page = VerificationPageDTOTransformer
                .toDTO(verificationService
                        .findPageOfSentVerificationsByProviderId(
                                user.getOrganizationId(),
                                pageNumber,
                                itemsPerPage));
        return new PageDTO<>(page.getTotalElements(), page.getContent());
    }

    @RequestMapping(value = "new/search", method = RequestMethod.POST)
    public PageDTO<VerificationPageDTO> getPageOfAllSentVerificationsByProviderIdAndSearch(
            @RequestBody VerificationSearchDTO verificationSearchDto,
            @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails employeeUser) {
       ProviderEmployee providerEmployee= providerEmployeeService.oneProviderEmployee(employeeUser.getUsername());
        ListToPageTransformer<Verification> queryResult = verificationService.findPageOfSentVerificationsByProviderIdAndCriteriaSearch(
                employeeUser.getOrganizationId(),
                verificationSearchDto.getPageNumber(),
                verificationSearchDto.getItemsPerPage(),
                verificationSearchDto.getSearchByDate(),
                verificationSearchDto.getSearchById(),
                verificationSearchDto.getSearchByLastName(),
                verificationSearchDto.getSearchByStreet(),
               providerEmployee

        );

        List<VerificationPageDTO> content = VerificationPageDTOTransformer.toDtoFromList(queryResult.getContent());
        return new PageDTO<VerificationPageDTO>(queryResult.getTotalItems(), content);

    }


    @RequestMapping(value = "new/count/provider", method = RequestMethod.GET)
    public Long getCountOfNewVerificationsByProviderId( @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {
        	return verificationService.findCountOfNewVerificationsByProviderId(user.getOrganizationId());
    }


    /**
     * Find calibrators by district which correspond provider district
     *
     * @return calibrator
     */
    @RequestMapping(value = "new/calibrators", method = RequestMethod.GET)
    public List<Calibrator> updateVerification(
            @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {

        return calibratorService.findByDistrict(
                providerService
                        .findById(user.getOrganizationId())
                        .getAddress()
                        .getDistrict()
        );
    }

    @RequestMapping(value = "new/providerEmployees", method = RequestMethod.GET)
    public List<EmployeeProvider> employeeVerification(
            @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {
        ProviderEmployee employee = providerEmployeeService.oneProviderEmployee(user.getUsername());
        List<EmployeeProvider> providerListEmployee = new ArrayList<>();

        if (employee.getRole().equalsIgnoreCase("PROVIDER_ADMIN")) {
            List<ProviderEmployee> list = providerEmployeeService.getAllProviders("PROVIDER_EMPLOYEE", employee.getOrganization().getId());
            EmployeeProvider.giveListOfProvidors(list);
            providerListEmployee = EmployeeProvider.giveListOfProvidors(list);
        } else {
            EmployeeProvider userPage = new EmployeeProvider(employee.getUsername(), employee.getFirstName(), employee.getLastName(), employee.getMiddleName(), employee.getRole());
            providerListEmployee.add(userPage);
        }
        return providerListEmployee;
    }

    /**
     * Update verificationsproviderListEmployee
     */
    @RequestMapping(value = "new/update", method = RequestMethod.PUT)
    public void updateVerification(
            @RequestBody VerificationUpdatingDTO verificationUpdatingDTO) {
        for (String verificationId : verificationUpdatingDTO.getIdsOfVerifications()) {
            verificationService.updateVerification(verificationId, verificationUpdatingDTO.getCalibrator());
        }
    }

    @RequestMapping(value = "new/read", method = RequestMethod.PUT)
    public void markVerificationAsRead(@RequestBody VerificationReadStatusUpdateDTO verificationDto) {
        System.out.println("inside controller to update");
        verificationService.updateVerificationReadStatus(verificationDto.getVerificationId(), verificationDto.getReadStatus());
    }

    @RequestMapping(value = "assign/providerEmployee", method = RequestMethod.PUT)
    public void assignProviderEmployee(@RequestBody VerificationUpdatingDTO verificationUpdatingDTO) {
        ProviderEmployee providerEmployee = new ProviderEmployee();
        String idVerif=verificationUpdatingDTO.getIdVerification();
        providerEmployee.setUsername(verificationUpdatingDTO.getEmployeeProvider().getUsername());
        verificationService.assignProviderEmployee(idVerif, providerEmployee);
    }
    @RequestMapping(value = "remove/providerEmployee", method = RequestMethod.PUT)
    public void removeProviderEmployee(@RequestBody VerificationUpdatingDTO verificationUpdatingDTO) {
        verificationService.assignProviderEmployee(verificationUpdatingDTO.getIdVerification(), null);
    }


    @RequestMapping(value = "new/{verificationId}", method = RequestMethod.GET)
    public ClientStageVerificationDTO getNewVerificationDetailsById(
            @PathVariable String verificationId,
            @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {

        Verification verification = verificationService
                .findByIdAndProviderId(
                        verificationId,
                        user.getOrganizationId()
                );

        ClientData clientData = verification.getClientData();
        Address address = clientData.getClientAddress();

        return new ClientStageVerificationDTO(clientData, address, null);
    }

    @RequestMapping(value = "archive/{verificationId}", method = RequestMethod.GET)
    public VerificationDTO getArchivalVerificationDetailsById(
            @PathVariable String verificationId,
            @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {

        Verification verification = verificationService
                .findByIdAndProviderId(verificationId, user.getOrganizationId());

        return new VerificationDTO(
                verification.getClientData(),
                verification.getId(),
                verification.getInitialDate(),
                verification.getExpirationDate(),
                verification.getStatus(),
                verification.getCalibrator(),
                verification.getCalibratorEmployee(),
                verification.getDevice(),
                verification.getProvider(),
                verification.getProviderEmployee(),
                verification.getStateVerificator(),
                verification.getStateVerificatorEmployee()
        );
    }
}
