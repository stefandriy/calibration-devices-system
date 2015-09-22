package com.softserve.edu.controller.provider;

import com.softserve.edu.controller.client.application.util.CatalogueDTOTransformer;
import com.softserve.edu.dto.application.ApplicationFieldDTO;
import com.softserve.edu.dto.application.RejectMailDTO;
import com.softserve.edu.dto.provider.OrganizationStageVerificationDTO;
import com.softserve.edu.entity.*;
import com.softserve.edu.entity.catalogue.District;
import com.softserve.edu.entity.catalogue.Region;
import com.softserve.edu.entity.util.ReadStatus;
import com.softserve.edu.entity.util.Status;
import com.softserve.edu.service.DeviceService;
import com.softserve.edu.service.MailServiceImpl;
import com.softserve.edu.service.SecurityUserDetailsService;
import com.softserve.edu.service.calibrator.CalibratorService;
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
import java.util.Set;

@RestController
@RequestMapping(value = "/provider/applications/")
public class ProviderApplicationController {

    @Autowired
    private RegionService regionService;

    @Autowired
    private VerificationService verificationService;

    @Autowired
    private CalibratorService calibratorService;

    @Autowired
    private ProviderService providerService;

    @Autowired
    private DistrictService districtService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private LocalityService localityService;

    @Autowired
    private MailServiceImpl mail;

    /**
     * Save verification in database
     *
     * @param verificationDTO object with verification data
     */
    @RequestMapping(value = "send", method = RequestMethod.POST)
    public String getInitiateVerification(@RequestBody OrganizationStageVerificationDTO verificationDTO) {
        ClientData clientData = new ClientData(
                verificationDTO.getFirstName(),
                verificationDTO.getLastName(),
                verificationDTO.getMiddleName(),
                verificationDTO.getEmail(),
                verificationDTO.getPhone(),
                verificationDTO.getSecondPhone(),
                new Address(
                        verificationDTO.getRegion(),
                        verificationDTO.getDistrict(),
                        verificationDTO.getLocality(),
                        verificationDTO.getStreet(),
                        verificationDTO.getBuilding(),
                        verificationDTO.getFlat()
                )
        );

        Organization provider = providerService.findById(verificationDTO.getProviderId());
        Organization calibrator = calibratorService.findById(verificationDTO.getCalibratorId());

        Device device = deviceService.getById(verificationDTO.getDeviceId());
        Verification verification = new Verification(new Date(), new Date(), clientData, provider, device, Status.SENT, ReadStatus.UNREAD, calibrator);

        verificationService.saveVerification(verification);
        String name = clientData.getFirstName() + " " + clientData.getLastName();
        mail.sendMailFromProvider(clientData.getEmail(), name, verification.getId(), verification.getProvider().getName(), verification.getDevice().getDeviceType().toString());

        return verification.getId();
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

        Organization provider = providerService.findById(employeeUser.getOrganizationId());
        Region region = regionService.getRegionByDesignation(provider.getAddress().getRegion());
        District district = districtService.findDistrictByDesignationAndRegion(
                provider.getAddress().getDistrict(),
                region.getId()
        );
        return CatalogueDTOTransformer.toDto(localityService.getLocalitiesCorrespondingDistrict(district.getId()));
    }

    @RequestMapping(value = "organizationType", method = RequestMethod.GET)
    public Long checkOrganizationType(@AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {
        Set<String> types = providerService.getTypesById(user.getOrganizationId());
        for (String type : types) {
            if (type.equalsIgnoreCase("CALIBRATOR")) {
                return user.getOrganizationId();
            }
        }
        return (long) -1;
    }

    @RequestMapping(value = "new/mail", method = RequestMethod.POST)
    public String sendReject(@RequestBody RejectMailDTO reject) {
        Verification verification = verificationService.findById(reject.getVerifID());
        String name = verification.getClientData().getFirstName();
        String sendTo = verification.getClientData().getEmail();
        //saving rejectMessage in database if verification is rejected
        if (verification.getStatus() == Status.REJECTED) {
            verification.setRejectedMessage(reject.getMsg());
            verificationService.saveVerification(verification);
        }
        mail.sendRejectMail(sendTo, name, reject.getVerifID(), reject.getMsg(), verification.getDevice().getDeviceType().toString());
        return reject.getVerifID();
    }
}
