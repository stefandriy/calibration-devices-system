package com.softserve.edu.controller.calibrator;

import com.softserve.edu.dto.PageDTO;
import com.softserve.edu.dto.VerificationUpdateDTO;
import com.softserve.edu.dto.admin.OrganizationDTO;
import com.softserve.edu.dto.calibrator.NotStandardVerificationDTO;
import com.softserve.edu.entity.enumeration.organization.OrganizationType;
import com.softserve.edu.entity.enumeration.verification.Status;
import com.softserve.edu.entity.organization.Organization;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.verification.Verification;
import com.softserve.edu.service.admin.OrganizationService;
import com.softserve.edu.service.calibrator.CalibratorEmployeeService;
import com.softserve.edu.service.calibrator.CalibratorService;
import com.softserve.edu.service.provider.ProviderService;
import com.softserve.edu.service.user.SecurityUserDetailsService;
import com.softserve.edu.service.verification.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/calibrator/not-standard-verifications/")
public class NotStandardVerificationCalibratorController {

    @Autowired
    VerificationService verificationService;

    @Autowired
    CalibratorService calibratorService;

    @Autowired
    OrganizationService organizationService;
    @Autowired
    ProviderService providerService;
    @Autowired
    CalibratorEmployeeService calibratorEmployeeService;

    /**
     * This method calls service whiche returns the page of verifications. The controller transform them with the help of
     * toDTOFromList to list of NotStandardVerificationDTO. It's done to sent to the client only the necessary data.
     *
     * @param pageNumber
     * @param itemsPerPage
     * @param employeeUser
     * @return Page of NotStandardVerificationDTO - data for table with protocols
     */
    @RequestMapping(value = "{pageNumber}/{itemsPerPage}", method = RequestMethod.GET)
    public PageDTO<NotStandardVerificationDTO> getPageOfVerificationsCreatedByCalibrator(
            @PathVariable Integer pageNumber, @PathVariable Integer itemsPerPage,
            @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails employeeUser) {

        User calibratorEmployee = calibratorEmployeeService.oneCalibratorEmployee(employeeUser.getUsername());
        Status status = Status.CREATED_BY_CALIBRATOR;
        List<Verification> verifications = verificationService.findPageOfVerificationsByCalibratorEmployeeAndStatus(
                calibratorEmployee, pageNumber, itemsPerPage, status);
        Long count = verificationService.countByCalibratorEmployeeUsernameAndStatus(calibratorEmployee, status);
        List<NotStandardVerificationDTO> content = toDTOFromList(verifications);

        return new PageDTO<>(count, content);
    }

    /**
     * Get providers that have agreements with this calibrator
     *
     * @param user
     * @return Set of OrganizationDTO
     */
    @RequestMapping(value = "providers", method = RequestMethod.GET)
    public Set<OrganizationDTO> getProviders(@AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {

        Organization userOrganization = organizationService.getOrganizationById(user.getOrganizationId());
        return organizationService.findCustomersByIdAndTypeAndActiveAgreementDeviceType(user.getOrganizationId(),
                OrganizationType.PROVIDER, userOrganization.getDeviceTypes().iterator().next().toString()).stream()
                .map(organization -> new OrganizationDTO(organization.getId(), organization.getName()))
                .collect(Collectors.toSet());
    }

    /**
     * Change status for verification when it is sent to provider
     *
     * @param verificationUpdateDTO
     */
    @RequestMapping(value = "send", method = RequestMethod.PUT)
    public void updateVerification(@RequestBody VerificationUpdateDTO verificationUpdateDTO) {

        for (String verificationId : verificationUpdateDTO.getIdsOfVerifications()) {
            Long idProvider = verificationUpdateDTO.getOrganizationId();
            Organization provider = providerService.findById(idProvider);
            verificationService.sendVerificationTo(verificationId, provider, Status.SENT_TO_PROVIDER);
        }
    }


    private  List<NotStandardVerificationDTO> toDTOFromList(List<Verification> verifications) {

        List<NotStandardVerificationDTO> resultList = new ArrayList<>();
        for(Verification verification : verifications) {
            resultList.add(new NotStandardVerificationDTO(
                    verification.getId(),
                    verification.getInitialDate(),
                    verification.getClientData().getClientAddress(),
                    verification.getClientData().getFirstName(),
                    verification.getClientData().getLastName(),
                    verification.getClientData().getMiddleName(),
                    verification.getCounter(),
                    verification.getCalibrationTests().iterator().next())); //In archive one verification has only one test
        }
        return resultList;
    }
}

