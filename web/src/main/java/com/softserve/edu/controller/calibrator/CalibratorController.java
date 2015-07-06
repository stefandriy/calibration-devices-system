package com.softserve.edu.controller.calibrator;

import com.softserve.edu.controller.provider.util.VerificationPageDTOTransformer;
import com.softserve.edu.dto.CalibrationTestDTO;
import com.softserve.edu.dto.PageDTO;
import com.softserve.edu.dto.application.ClientStageVerificationDTO;
import com.softserve.edu.dto.asm.CalibrationTestDTOAsm;
import com.softserve.edu.dto.provider.VerificationPageDTO;
import com.softserve.edu.dto.provider.VerificationReadStatusUpdateDTO;
import com.softserve.edu.dto.calibrator.VerificationSearchDTO;
import com.softserve.edu.dto.calibrator.VerificationUpdatingDTO;
import com.softserve.edu.entity.*;
import com.softserve.edu.service.calibrator.CalibratorService;
import com.softserve.edu.service.SecurityUserDetailsService;
import com.softserve.edu.service.exceptions.NotAvailableException;
import com.softserve.edu.service.provider.ProviderService;
import com.softserve.edu.service.state.verificator.StateVerificatorService;
import com.softserve.edu.service.verification.VerificationService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/calibrator/verifications/")
public class CalibratorController {

    @Autowired
    VerificationService verificationService;

    @Autowired
    ProviderService providerService;

    @Autowired
    CalibratorService calibratorService;

    @Autowired
    StateVerificatorService verificatorService;

    private final Logger logger = Logger.getLogger(CalibratorController.class);

    @RequestMapping(value = "new/{pageNumber}/{itemsPerPage}", method = RequestMethod.GET)
    public PageDTO<VerificationPageDTO> getPageOfAllSentVerificationsByCalibratorId(
            @PathVariable Integer pageNumber,
            @PathVariable Integer itemsPerPage,
            @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails employeeUser) {

        Page<VerificationPageDTO> page = VerificationPageDTOTransformer
                .toDTO(verificationService
                        .findPageOfSentVerificationsByCalibratorId(
                                employeeUser.getOrganizationId(),
                                pageNumber,
                                itemsPerPage));

        return new PageDTO<>(page.getTotalElements(), page.getContent());
    }
    
    @RequestMapping(value = "new/search", method = RequestMethod.POST)
    public PageDTO<VerificationPageDTO> getPageOfAllSentVerificationsByCalibratorIdAndSearch(
    		@RequestBody VerificationSearchDTO verificationSearchDto,
            @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails employeeUser) {

    		if(verificationSearchDto.getSearchText().length()>0){
    			
    			 Page<VerificationPageDTO> page = VerificationPageDTOTransformer
    		                .toDTO(verificationService
    		                        .findPageOfSentVerificationsByCalibratorIdAndSearch(
    		                                employeeUser.getOrganizationId(),
    		                                verificationSearchDto.getPageNumber(),
    		                                verificationSearchDto.getItemsPerPage(),
    		                                verificationSearchDto.getSearchType(),
    		                                verificationSearchDto.getSearchText()
    		                                ));

    		        return new PageDTO<>(page.getTotalElements(), page.getContent());
    		} else {
    			
    			 Page<VerificationPageDTO> page = VerificationPageDTOTransformer
    		                .toDTO(verificationService
    		                        .findPageOfSentVerificationsByCalibratorId(
    		                                employeeUser.getOrganizationId(),
    		                                verificationSearchDto.getPageNumber(),
    		                                verificationSearchDto.getItemsPerPage()));

    		        return new PageDTO<>(page.getTotalElements(), page.getContent());
    		}
    		
       
    }

    @RequestMapping(value = "new/count/calibrator", method = RequestMethod.GET)
    public Long getCountOfNewVerificationsByCalibratorId( @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {
    	return verificationService.findCountOfNewVerificationsByCalibratorId(user.getOrganizationId());
    }
    
    @RequestMapping(value = "new/verificators", method = RequestMethod.GET)
    public List<StateVerificator> getMatchingVerificators(
            @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {

        return verificatorService.findByDistrict(
                calibratorService
                        .findById(user.getOrganizationId())
                        .getAddress()
                        .getDistrict()
        );
    }

    @RequestMapping(value = "new/update", method = RequestMethod.PUT)
    public void updateVerification(
            @RequestBody VerificationUpdatingDTO verificationUpdatingDTO) {
        for (String verificationId : verificationUpdatingDTO.getIdsOfVerifications()) {
            verificationService
                    .updateVerificationByCalibrator(
                            verificationId,
                            verificationUpdatingDTO.getVerificator()
                    );
        }
    }
    
    @RequestMapping(value = "new/read", method = RequestMethod.PUT)
    public void markVerificationAsRead(@RequestBody VerificationReadStatusUpdateDTO verificationDto) {
     System.out.println("inside controller to update");
     verificationService.updateVerificationReadStatus(verificationDto.getVerificationId(), verificationDto.getReadStatus());
    }
        
    @RequestMapping(value = "new/{verificationId}", method = RequestMethod.GET)
    public ClientStageVerificationDTO getNewVerificationDetailsById(
            @PathVariable String verificationId,
            @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {

        Verification verification = verificationService
                .findByIdAndCalibratorId(
                        verificationId,
                        user.getOrganizationId()
                );

        ClientData clientData = verification.getClientData();
        Address address = clientData.getClientAddress();

        return new ClientStageVerificationDTO(clientData, address,  null);
    }

    @RequestMapping(value = "new/{verificationId}/calibration-test", method = RequestMethod.POST)
    public ResponseEntity<CalibrationTestDTO> createCalibrationTest(
            @PathVariable String verificationId,
            @RequestBody CalibrationTestDTO sentTest) {
        CalibrationTest createdTest;
        try {
            createdTest = verificationService.createCalibrationTest(
                    verificationId, sentTest.toCalibrationTest());
            CalibrationTestDTO createdTestDTO =
                    new CalibrationTestDTOAsm().toResource(createdTest);
            return new ResponseEntity<>(createdTestDTO, HttpStatus.CREATED);
        } catch (NotAvailableException e) {
            throw new com.softserve.edu.exceptions.NotFoundException(e);
        }
    }
}
