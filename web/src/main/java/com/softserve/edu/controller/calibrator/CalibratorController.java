package com.softserve.edu.controller.calibrator;

import com.softserve.edu.controller.provider.util.VerificationPageDTOTransformer;
import com.softserve.edu.dto.PageDTO;
import com.softserve.edu.dto.provider.VerificationPageDTO;
import com.softserve.edu.dto.calibrator.VerificationUpdatingDTO;
import com.softserve.edu.entity.*;
import com.softserve.edu.service.CalibratorService;
import com.softserve.edu.service.SecurityUserDetailsService;
import com.softserve.edu.service.provider.ProviderService;
import com.softserve.edu.service.state.verificator.StateVerificatorService;
import com.softserve.edu.service.verification.VerificationService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
        for (String verificationId : verificationUpdatingDTO.getIdsOfVerifications()){
            verificationService
                    .updateVerificationByCalibrator(
                            verificationId,
                            verificationUpdatingDTO.getVerificator()
                    );
        }
    }
}
