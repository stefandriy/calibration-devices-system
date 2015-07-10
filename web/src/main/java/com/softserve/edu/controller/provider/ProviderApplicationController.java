package com.softserve.edu.controller.provider;

import com.softserve.edu.controller.client.application.util.CatalogueDTOTransformer;
import com.softserve.edu.dto.application.ApplicationFieldDTO;
import com.softserve.edu.dto.provider.ProviderStageVerificationDTO;
import com.softserve.edu.entity.Address;
import com.softserve.edu.entity.ClientData;
import com.softserve.edu.entity.Provider;
import com.softserve.edu.entity.Verification;
import com.softserve.edu.entity.catalogue.District;
import com.softserve.edu.entity.catalogue.Region;
import com.softserve.edu.entity.util.ReadStatus;
import com.softserve.edu.entity.util.Status;
import com.softserve.edu.service.SecurityUserDetailsService;
import com.softserve.edu.service.catalogue.DistrictService;
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

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/provider/applications/")
public class ProviderApplicationController {

    @Autowired
    private RegionService regionService;

    @Autowired
    private VerificationService verificationService;

    @Autowired
    private ProviderService providerService;

    @Autowired
    private DistrictService districtService;

    @Autowired
    private LocalityService localityService;


    /**
     * Save verification in database
     *
     * @param verificationDTO object with verification data
     */
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
                Status.IN_PROGRESS, ReadStatus.UNREAD, verificationDTO.getCalibrator());
        verificationService.saveVerification(verification);
    }

    /**
     * Find provider by id, finds region corresponding to provider region, finds district
     * corresponding to provider district and id
     *
     * @return ApplicationFieldDTO which contains id and designation corresponding to
     * locality id an designation
     */
    @RequestMapping(value = "localities", method = RequestMethod.GET)
    public List<ApplicationFieldDTO> getLocalityCorrespondingProvider(
            @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails employeeUser) {

        Provider provider = providerService.findById(employeeUser.getOrganizationId());
        Region region = regionService.getRegionByDesignation(provider.getAddress().getRegion());
        District district = districtService.findDistrictByDesignationAndRegion(
                provider.getAddress().getDistrict(),
                region.getId()
        );
        return CatalogueDTOTransformer.toDto(localityService.getLocalitiesCorrespondingDistrict(district.getId()));
    }
}
