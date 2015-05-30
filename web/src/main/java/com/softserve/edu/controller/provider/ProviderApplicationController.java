package com.softserve.edu.controller.provider;

import com.softserve.edu.dto.provider.ProviderStageVerificationDTO;
import com.softserve.edu.entity.Address;
import com.softserve.edu.entity.ClientData;
import com.softserve.edu.entity.Provider;
import com.softserve.edu.entity.Verification;
import com.softserve.edu.entity.catalogue.Locality;
import com.softserve.edu.entity.catalogue.Region;
import com.softserve.edu.entity.util.Status;
import com.softserve.edu.service.SecurityUserDetailsService;
import com.softserve.edu.service.catalogue.LocalityService;
import com.softserve.edu.service.catalogue.RegionService;
import com.softserve.edu.service.provider.ProviderService;
import com.softserve.edu.service.verification.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/provider/applications/")
public class ProviderApplicationController {
    @Autowired
    private RegionService regionService;
    @Autowired
    VerificationService verificationService;

    @Autowired
    ProviderService providerService;

    @Autowired
    LocalityService localityService;

    @RequestMapping(value = "send", method = RequestMethod.POST)
    public void getInitiateVerification(
            @RequestBody ProviderStageVerificationDTO verificationDTO,
            @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails employeeUser) {

        Provider provider = providerService.findById(employeeUser.getOrganizationId());

        Verification verification = new Verification(
                new Date(),
                new ClientData(
                        verificationDTO.getName(),
                        verificationDTO.getSurname(),
                        verificationDTO.getMiddleName(),
                        verificationDTO.getPhone(),
                        new Address(
                                provider.getAddress().getRegion(),
                                provider.getAddress().getDistrict(),
                                verificationDTO.getLocality(),
                                verificationDTO.getStreet(),
                                verificationDTO.getBuilding(),
                                verificationDTO.getFlat())),
                provider,
                Status.SENT);
        verificationService.saveVerification(verification);
    }

    @RequestMapping(value = "localities", method = RequestMethod.GET)
    public List<Locality> getStreetsCorrespondingProvider(
            @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails employeeUser) {

        Provider provider = providerService.findById(employeeUser.getOrganizationId());
        Region region = regionService
                .getRegionByDesignation(
                        provider.getAddress().getRegion());
        return new ArrayList<>();
    }
}
