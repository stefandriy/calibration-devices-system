package com.softserve.edu.controller.state_verificator;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.softserve.edu.controller.provider.util.VerificationPageDTOTransformer;
import com.softserve.edu.dto.PageDTO;
import com.softserve.edu.dto.application.ClientStageVerificationDTO;
import com.softserve.edu.dto.provider.VerificationPageDTO;
import com.softserve.edu.dto.provider.VerificationReadStatusUpdateDTO;
import com.softserve.edu.entity.Address;
import com.softserve.edu.entity.ClientData;
import com.softserve.edu.entity.Organization;
import com.softserve.edu.entity.Verification;
import com.softserve.edu.service.SecurityUserDetailsService;
import com.softserve.edu.service.calibrator.CalibratorService;
import com.softserve.edu.service.provider.ProviderService;
import com.softserve.edu.service.state.verificator.StateVerificatorService;
import com.softserve.edu.service.verification.VerificationService;

@RestController
@RequestMapping(value = "/verificator/verifications/")
public class StateVerificatorController {

	@Autowired
	VerificationService verificationService;

	@Autowired
	ProviderService providerService;

	@Autowired
	CalibratorService calibratorService;

	@Autowired
	StateVerificatorService verificatorService;

	private final Logger logger = Logger
			.getLogger(StateVerificatorController.class);

	@RequestMapping(value = "new/{pageNumber}/{itemsPerPage}", method = RequestMethod.GET)
	public PageDTO<VerificationPageDTO> getPageOfAllSentVerificationsByStateVerificatorId(
			@PathVariable Integer pageNumber,
			@PathVariable Integer itemsPerPage,
			@AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails employeeUser) {
		Page<VerificationPageDTO> page = VerificationPageDTOTransformer
				.toDTO(verificationService
						.findPageOfSentVerificationsByStateVerificatorId(
								employeeUser.getOrganizationId(), pageNumber,
								itemsPerPage));

		return new PageDTO<>(page.getTotalElements(), page.getContent());
	}
	
	 /**
     * Find providers by district which correspond stateVerificator district
     *
     * @return provider
     */
    @RequestMapping(value = "new/providers", method = RequestMethod.GET)
    public List<Organization> getMatchingVerificators(
            @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {

        return providerService.findByDistrict(verificatorService.findById(user.getOrganizationId()).getAddress().getDistrict(), "PROVIDER");
    }
    
    @RequestMapping(value = "new/count/verificator", method = RequestMethod.GET)
    public Long getCountOfNewVerificationsByStateVerificatorId( @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {
    	return verificationService.findCountOfNewVerificationsByStateVerificatorId(user.getOrganizationId());
    }
    
    @RequestMapping(value = "new/read", method = RequestMethod.PUT)
    public void markVerificationAsRead(@RequestBody VerificationReadStatusUpdateDTO verificationDto) {
    	 verificationService.updateVerificationReadStatus(verificationDto.getVerificationId(), verificationDto.getReadStatus());
    }
    
    @RequestMapping(value = "new/{verificationId}", method = RequestMethod.GET)
    public ClientStageVerificationDTO getNewVerificationDetailsById(
            @PathVariable String verificationId,
            @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {

        Verification verification = verificationService
                .findByIdAndStateVerificatorId(
                        verificationId,
                        user.getOrganizationId()
                );

        ClientData clientData = verification.getClientData();
        Address address = clientData.getClientAddress();

        return new ClientStageVerificationDTO(clientData, address,  null);
    }
    

}
