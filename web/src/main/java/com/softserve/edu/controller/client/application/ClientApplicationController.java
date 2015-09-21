package com.softserve.edu.controller.client.application;

import com.softserve.edu.dto.application.ApplicationFieldDTO;
import com.softserve.edu.dto.application.ClientMailDTO;
import com.softserve.edu.dto.application.ClientStageVerificationDTO;
import com.softserve.edu.dto.provider.VerificationDTO;
import com.softserve.edu.entity.*;
import com.softserve.edu.entity.util.ReadStatus;
import com.softserve.edu.entity.util.Status;
import com.softserve.edu.service.DeviceService;
import com.softserve.edu.service.MailService;
import com.softserve.edu.service.calibrator.CalibratorService;
import com.softserve.edu.service.provider.ProviderService;
import com.softserve.edu.service.verification.VerificationService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/application/")
public class ClientApplicationController {

    Logger logger = Logger.getLogger(ClientApplicationController.class);

    @Autowired
    private VerificationService verificationService;

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

    @RequestMapping(value = "providers/{district}", method = RequestMethod.GET)
    public List<ApplicationFieldDTO> getProvidersCorrespondingDistrict(@PathVariable String district) {

        return providerService.findByTypeAndDistrict(district, "PROVIDER").stream()
                .map(provider -> new ApplicationFieldDTO(provider.getId(), provider.getName()))
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "calibrators/{district}", method = RequestMethod.GET)
    public List<ApplicationFieldDTO> getCalibratorsCorrespondingDistrict(@PathVariable String district) {

        return calibratorService.findByDistrict(district, "CALIBRATOR")
                .stream()
                .map(calibrator -> new ApplicationFieldDTO(calibrator.getId(), calibrator.getName()))
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "devices", method = RequestMethod.GET)
    public List<ApplicationFieldDTO> getAll() {
        return deviceService.getAll().stream()
                .map(device -> new ApplicationFieldDTO(device.getId(), device.getDeviceName()))
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "devices/{deviceType}", method = RequestMethod.GET)
    public List<ApplicationFieldDTO> getAllByType(@PathVariable String deviceType) {
        return deviceService.getAllByType(deviceType).stream()
                .map(device -> new ApplicationFieldDTO(device.getId(), device.getDeviceName()))
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "clientMessage", method = RequestMethod.POST)
    public String sentMailFromClient(@RequestBody ClientMailDTO mailDto) {
        Verification verification = verificationService.findById(mailDto.getVerifID());
        String name = verification.getClientData().getFirstName();
        String surname = verification.getClientData().getLastName();
        String sendFrom = verification.getClientData().getEmail();
        mail.sendClientMail(sendFrom, name, surname, mailDto.getVerifID(), mailDto.getMsg());

        return "SUCCESS";
    }

    @RequestMapping(value = "clientMessageNoProvider", method = RequestMethod.POST)
    public String sentMailFromClientNoprovider(@RequestBody ClientMailDTO mailDto) {

        mail.sendClientMail(mailDto.getEmail(), mailDto.getName(), mailDto.getSurname(), mailDto.getVerifID(), mailDto.getMsg());

        return "SUCCESS";
    }
}
