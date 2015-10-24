package com.softserve.edu.controller.client.application;


import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.softserve.edu.controller.client.application.util.DeviceLightDTO;
import com.softserve.edu.entity.device.Device;
import com.softserve.edu.entity.enumeration.organization.OrganizationType;
import com.softserve.edu.entity.organization.Organization;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.verification.ClientData;
import com.softserve.edu.entity.verification.Verification;
import com.softserve.edu.service.admin.OrganizationService;
import com.softserve.edu.service.tool.MailService;
import com.softserve.edu.service.user.SecurityUserDetailsService;
import com.softserve.edu.service.user.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.softserve.edu.dto.application.ApplicationFieldDTO;
import com.softserve.edu.dto.application.ClientMailDTO;
import com.softserve.edu.dto.application.ClientStageVerificationDTO;
import com.softserve.edu.dto.provider.VerificationDTO;
import com.softserve.edu.entity.*;
import com.softserve.edu.entity.enumeration.verification.ReadStatus;
import com.softserve.edu.entity.enumeration.verification.Status;
import com.softserve.edu.service.tool.DeviceService;
import com.softserve.edu.service.calibrator.CalibratorService;
import com.softserve.edu.service.provider.ProviderService;
import com.softserve.edu.service.verification.VerificationService;

@RestController
@RequestMapping(value = "/application/")
/**
 * Used in main application form (application-sending.html)
 * for creating verifications
 * and sending notifications about that to customerID's email
 */
public class ClientApplicationController {

    Logger logger = Logger.getLogger(ClientApplicationController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private VerificationService verificationService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private ProviderService providerService;

    @Autowired
    private CalibratorService calibratorService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private MailService mail;

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String saveApplication(@RequestBody ClientStageVerificationDTO verificationDTO) {

        ClientData clientData = new ClientData(verificationDTO.getFirstName(),
                verificationDTO.getLastName(),
                verificationDTO.getMiddleName(),
                verificationDTO.getEmail(),
                verificationDTO.getPhone(),
                verificationDTO.getSecondPhone(),

                new Address(verificationDTO.getRegion(),
                        verificationDTO.getDistrict(),
                        verificationDTO.getLocality(),
                        verificationDTO.getStreet(),
                        verificationDTO.getBuilding(),
                        verificationDTO.getFlat()));
        Organization provider = providerService.findById(verificationDTO.getProviderId());
        Device device = deviceService.getById(verificationDTO.getDeviceId());
        Verification verification = new Verification(new Date(), new Date(), clientData, provider, device, Status.SENT, ReadStatus.UNREAD, null, verificationDTO.getComment());

        verificationService.saveVerification(verification);
        String name = clientData.getFirstName() + " " + clientData.getLastName();
        mail.sendMail(clientData.getEmail(), name, verification.getId(), verification.getProvider().getName(), verification.getDevice().getDeviceType().toString());
        return verification.getId();
    }


    @RequestMapping(value = "check/{verificationId}", method = RequestMethod.GET)
    public String getClientCode(@PathVariable String verificationId) {

        Verification verification = verificationService.findById(verificationId);
        return verification == null ? "NOT_FOUND" : verification.getStatus().name();
    }

    @RequestMapping(value = "verification/{verificationId}", method = RequestMethod.GET)
    public VerificationDTO getVerificationCode(@PathVariable String verificationId) {
        Verification verification = verificationService.findById(verificationId);
        if (verification != null) {
            logger.trace(verification.getRejectedMessage());
            return new VerificationDTO(verification.getClientData(),
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
                    verification.getStateVerificatorEmployee(),
                    verification.getRejectedMessage(),
                    verification.getComment());
        } else {
            return null;
        }
    }

    /**
     * Find Providers corresponding to Locality
     *
     * @param localityId
     * @return
     */
    @RequestMapping(value = "providersInLocality/{localityId}", method = RequestMethod.GET)
    public List<ApplicationFieldDTO> getProvidersCorrespondingLocality(@PathVariable Long localityId) {

        return organizationService.findAllByLocalityIdAndTypeId(localityId, OrganizationType.PROVIDER).stream()
                .map(provider -> new ApplicationFieldDTO(provider.getId(), provider.getName()))
                .collect(Collectors.toList());
    }

    /**
     * Return all providers in locality by device type
     *
     * @param localityId
     * @param deviceType
     * @return
     */
    @RequestMapping(value = "providers/{localityId}/{deviceType}", method = RequestMethod.GET)
    public List<ApplicationFieldDTO> getProvidersCorrespondingLocalityAndType(@PathVariable Long localityId, @PathVariable String deviceType) {
        return organizationService.findByLocalityIdAndTypeAndDevice(localityId, OrganizationType.PROVIDER, Device.DeviceType.valueOf(deviceType))
                .stream().map(provider -> new ApplicationFieldDTO(provider.getId(), provider.getName()))
                .collect(Collectors.toList());
    }

    /**
     * Return calibrators corresponding organization and device type
     * @param type type of device.
     * @param user user of current organization
     * @return set of ApplicationFieldDTO where stored organization id and name
     */
    @RequestMapping(value = "calibrators/{type}", method = RequestMethod.GET)
    public Set<ApplicationFieldDTO> getCalibratorsCorrespondingDeviceType(@PathVariable String type,
                                                                        @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {
        //todo agreement
        return organizationService.findByIdAndTypeAndActiveAgreementDeviceType(user.getOrganizationId(), OrganizationType.CALIBRATOR, Device.DeviceType.valueOf(type))
                .stream()
                .map(organization -> new ApplicationFieldDTO(organization.getId(), organization.getName()))
                .collect(Collectors.toSet());
    }

    /**
     * return all devices
     *
     * @return
     */
    @RequestMapping(value = "devices", method = RequestMethod.GET)
    public List<DeviceLightDTO> getAll() {
        return deviceService.getAll().stream()
                .map(device -> new DeviceLightDTO(device.getId(), device.getDeviceName(), device.getDeviceType().name()))
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "devices/{deviceType}", method = RequestMethod.GET)
    public List<ApplicationFieldDTO> getAllByType(@PathVariable String deviceType) {
        return deviceService.getAllByType(deviceType).stream()
                .map(device -> new ApplicationFieldDTO(device.getId(), device.getDeviceName()))
                .collect(Collectors.toList());
    }

    /**
     * Sends email to System Administrator from client with verification application
     *
     * @param mailDto
     * @return
     */
    @RequestMapping(value = "clientMessage", method = RequestMethod.POST)
    public String sentMailFromClient(@RequestBody ClientMailDTO mailDto) {
        Verification verification = verificationService.findById(mailDto.getVerifID());
        String name = verification.getClientData().getFirstName();
        String surname = verification.getClientData().getLastName();
        String sendFrom = verification.getClientData().getEmail();

        List<User> adminList = userService.findByRole("SYS_ADMIN");
        if (!adminList.isEmpty() && adminList.get(0).getEmail() != null) {
            mail.sendClientMail(adminList.get(0).getEmail(), sendFrom, name, surname, mailDto.getVerifID(), mailDto.getMsg());
        } else {
            mail.sendClientMail("metrology.calibration.devices@gmail.com", sendFrom, name, surname, mailDto.getVerifID(), mailDto.getMsg());
        }
        return "SUCCESS";
    }

    /**
     * Sends email to System Administrator from client
     * when there is no provider in database for specified location (for example district)
     * and client wants to send a message
     *
     * @param mailDto
     * @return
     */
    @RequestMapping(value = "clientMessageNoProvider", method = RequestMethod.POST)
    public String sentMailFromClientNoProvider(@RequestBody ClientMailDTO mailDto) {

        // TODO We'd send email to some configured email address
        List<User> adminList = userService.findByRole("SYS_ADMIN");
        if (!adminList.isEmpty() && adminList.get(0).getEmail() != null) {
            mail.sendClientMail(adminList.get(0).getEmail(), mailDto.getEmail(), mailDto.getName(), mailDto.getSurname(), mailDto.getVerifID(), mailDto.getMsg());
            logger.trace("Email send to:" + adminList.get(0).getEmail());
        } else {
            mail.sendClientMail("metrology.calibration.devices@gmail.com", mailDto.getEmail(), mailDto.getName(), mailDto.getSurname(), mailDto.getVerifID(), mailDto.getMsg());
        }
        return "SUCCESS";
    }
}
