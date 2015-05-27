package com.softserve.edu.controller.application;

import com.softserve.edu.dto.application.ClientApplicationDTO;
import com.softserve.edu.dto.application.ClientApplicationFieldDTO;
import com.softserve.edu.entity.ClientData;
import com.softserve.edu.entity.Verification;
import com.softserve.edu.entity.util.Status;
import com.softserve.edu.service.MailService;
import com.softserve.edu.service.ProviderService;
import com.softserve.edu.service.VerificationService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.stream.Collectors;

import static com.softserve.edu.controller.application.util.ApplicationDTOTransformer.parseApplicationDTOToClientData;

/**
 * This is plain MVC controller.
 * Use this controller to render yours .jsp views.
 * Other controllers annotated as @RestControllers maintain RESTful API,
 * so that's why we recommend you to implement rendering .jsp methods below.
 */
@RestController
public class ApplicationController {

    private Logger logger = Logger.getLogger(ApplicationController.class);

    @Autowired
    private VerificationService verificationService;

    @Autowired
    private ProviderService providerService;
    
    @Value("${site.protocol}")
    private String protocol;

    @Autowired
    private MailService mail;

    @RequestMapping(value = "/application/add", method = RequestMethod.POST)
    public String saveApplication(@RequestBody ClientApplicationDTO clientApplicationDTO) {
        ClientData clientData = parseApplicationDTOToClientData((clientApplicationDTO));
        Verification verification = new Verification(clientData,
                providerService.findById(clientApplicationDTO.getProviderId()), Status.SENT);
        verificationService.saveVerification(verification);
        String name = clientData.getFirstName() + clientData.getLastName();
        mail.sendMail(clientData.getEmail(), name, verification.getId());
        return verification.getId();
    }

    @RequestMapping(value = "/application/check/{clientCode}", method = RequestMethod.GET)
    public String getClientCode(@PathVariable String clientCode) {
        Verification verification = verificationService.findByCode(clientCode);
        return verification == null ? "NOT_FOUND" : verification.getStatus().name();
    }

    @RequestMapping(value = "/application/providers/{district}", method = RequestMethod.GET)
    public List<ClientApplicationFieldDTO> getProvidersCorrespondingDistrict(@PathVariable String district)
            throws UnsupportedEncodingException {
        return providerService.findByDistrictDesignation(district)
                .stream()
                .map(provider -> new ClientApplicationFieldDTO(provider.getId(), provider.getName()))
                .collect(Collectors.toList());
    }
}
