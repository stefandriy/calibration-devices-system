package com.softserve.edu.controller.calibrator;

import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.softserve.edu.controller.provider.util.VerificationPageDTOTransformer;
import com.softserve.edu.dto.ArchiveVerificationsSearch;
import com.softserve.edu.dto.CalibrationTestDTO;
import com.softserve.edu.dto.NewVerificationsSearch;
import com.softserve.edu.dto.PageDTO;
import com.softserve.edu.dto.calibrator.VerificationUpdatingDTO;
import com.softserve.edu.dto.provider.VerificationDTO;
import com.softserve.edu.dto.provider.VerificationPageDTO;
import com.softserve.edu.dto.provider.VerificationReadStatusUpdateDTO;
import com.softserve.edu.entity.CalibrationTest;
import com.softserve.edu.entity.Organization;
import com.softserve.edu.entity.Verification;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.util.Status;
import com.softserve.edu.service.CalibrationTestService;
import com.softserve.edu.service.SecurityUserDetailsService;
import com.softserve.edu.service.calibrator.CalibratorEmployeeService;
import com.softserve.edu.service.calibrator.CalibratorService;
import com.softserve.edu.service.provider.ProviderService;
import com.softserve.edu.service.state.verificator.StateVerificatorService;
import com.softserve.edu.service.utils.ListToPageTransformer;
import com.softserve.edu.service.verification.VerificationService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.ws.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

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
    CalibratorEmployeeService calibratorEmployeeService;

    @Autowired
    CalibrationTestService testService;

    @Autowired
    StateVerificatorService verificatorService;

    private static final String contentExtPattern = "^.*\\.(bbi|BBI|)$";

    private final Logger logger = Logger.getLogger(CalibratorController.class);

    @RequestMapping(value = "new/{pageNumber}/{itemsPerPage}", method = RequestMethod.GET)
    public PageDTO<VerificationPageDTO> getPageOfAllSentVerificationsByProviderIdAndSearch(@PathVariable Integer pageNumber, @PathVariable Integer itemsPerPage,
                                                                                           NewVerificationsSearch searchData, @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails employeeUser) {

    	 User calibratorEmployee = calibratorEmployeeService.oneCalibratorEmployee(employeeUser.getUsername());
      ListToPageTransformer<Verification> queryResult = verificationService.findPageOfVerificationsByCalibratorIdAndCriteriaSearch(
              employeeUser.getOrganizationId(),
              pageNumber,
              itemsPerPage,
              searchData.getFormattedDate(),
              searchData.getIdText(),
              searchData.getLastNameText(),
              searchData.getStreetText(),
              searchData.getStatus(),
              calibratorEmployee
      );
      List<VerificationPageDTO> content = VerificationPageDTOTransformer.toDtoFromList(queryResult.getContent());
      return new PageDTO<VerificationPageDTO>(queryResult.getTotalItems(), content);
  }


    /**
     * Finds count of verifications which have read status 'UNREAD' and are
     * assigned to this organization
     *
     * @param user
     * @return Long
     */
    @RequestMapping(value = "new/count/calibrator", method = RequestMethod.GET)
    public Long getCountOfNewVerificationsByCalibratorId(
            @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {

        if (user != null) {
            return verificationService.findCountOfNewVerificationsByCalibratorId(user.getOrganizationId());
        } else {
            return null;
        }
    }

    @RequestMapping(value = "new/verificators", method = RequestMethod.GET)
    public List<Organization> getMatchingVerificators(
            @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {
        List<Organization> list = verificatorService.findByDistrict(
                calibratorService.findById(user.getOrganizationId()).getAddress().getDistrict(), "STATE_VERIFICATOR");

        return list;
    }

    @RequestMapping(value = "new/update", method = RequestMethod.PUT)
    public void updateVerification(@RequestBody VerificationUpdatingDTO verificationUpdatingDTO) {
        for (String verificationId : verificationUpdatingDTO.getIdsOfVerifications()) {
            Long idCalibrator = verificationUpdatingDTO.getVerificatorId();
            Organization calibrator = calibratorService.findById(idCalibrator);
            verificationService.sendVerificationTo(verificationId, calibrator, Status.SENT_TO_VERIFICATOR);
        }
    }

    /**
     * Update verification when user reads it
     *
     * @param verificationDto
     */
    @RequestMapping(value = "new/read", method = RequestMethod.PUT)
    public void markVerificationAsRead(@RequestBody VerificationReadStatusUpdateDTO verificationDto) {
        verificationService.updateVerificationReadStatus(verificationDto.getVerificationId(),
                verificationDto.getReadStatus());
    }


    @RequestMapping(value = "new/{verificationId}", method = RequestMethod.GET)
    public VerificationDTO getNewVerificationDetailsById(@PathVariable String verificationId) {
        Verification verification = verificationService.findById(verificationId);
        if (verification != null) {
            return new VerificationDTO(verification.getClientData(), verification.getId(),
                    verification.getInitialDate(), verification.getExpirationDate(), verification.getStatus(),
                    verification.getCalibrator(), verification.getCalibratorEmployee(), verification.getDevice(),
                    verification.getProvider(), verification.getProviderEmployee(), verification.getStateVerificator(),
                    verification.getStateVerificatorEmployee(),false);
        } else {
            return null;
        }
    }

    @RequestMapping(value = "new/calibration-test", method = RequestMethod.POST)
    public ResponseEntity createCalibrationTest(@RequestBody CalibrationTestDTO testDTO) {
        HttpStatus httpStatus = HttpStatus.CREATED;
        try {
            CalibrationTest createdTest = testDTO.saveCalibrationTest();
            testService.createTest(createdTest);
        } catch (Exception e) {
            logger.error("GOT EXCEPTION " + e.getMessage());
            httpStatus = HttpStatus.CONFLICT;
        }
        return new ResponseEntity<>(httpStatus);
    }

    @RequestMapping(value = "new/upload", method = RequestMethod.POST)
    public ResponseEntity<String> uploadFileBbi(@RequestBody MultipartFile file, @RequestParam String idVerification) {
        ResponseEntity<String> httpStatus = new ResponseEntity(HttpStatus.OK);
        try {
            String originalFileFullName = file.getOriginalFilename();
            String fileType = originalFileFullName.substring(originalFileFullName.lastIndexOf('.'));
            if (Pattern.compile(contentExtPattern, Pattern.CASE_INSENSITIVE).matcher(fileType).matches()) {
                calibratorService.uploadBbi(file.getInputStream(), idVerification, originalFileFullName);
            } else {
                logger.error("Failed to load file ");
                httpStatus = new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            logger.error("Failed to load file " + e.getMessage());
            httpStatus = new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return httpStatus;
    }

    @RequestMapping(value = "archive/{pageNumber}/{itemsPerPage}", method = RequestMethod.GET)
    public PageDTO<VerificationPageDTO> getPageOfArchivalVerificationsByOrganizationId(@PathVariable Integer pageNumber, @PathVariable Integer itemsPerPage,
                                                                                       ArchiveVerificationsSearch searchData, @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails employeeUser) {
      User calibratorEmployee = calibratorEmployeeService.oneCalibratorEmployee(employeeUser.getUsername());
      ListToPageTransformer<Verification> queryResult = verificationService.findPageOfArchiveVerificationsByCalibratorId(
              employeeUser.getOrganizationId(),
              pageNumber,
              itemsPerPage,
              searchData.getFormattedDate(),
              searchData.getIdText(),
              searchData.getLastNameText(),
              searchData.getStreetText(),
              searchData.getStatus(),
              searchData.getEmployee(),
              calibratorEmployee
      );
      List<VerificationPageDTO> content = VerificationPageDTOTransformer.toDtoFromList(queryResult.getContent());
      return new PageDTO<VerificationPageDTO>(queryResult.getTotalItems(), content);
  }
    
    @RequestMapping(value = "archive/{verificationId}", method = RequestMethod.GET)
    public VerificationDTO getArchivalVerificationDetailsById( @PathVariable String verificationId, @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {

        Verification verification = verificationService.findByIdAndCalibratorId(verificationId, user.getOrganizationId());

        return new VerificationDTO( verification.getClientData(), verification.getId(),
                verification.getInitialDate(), verification.getExpirationDate(),
                verification.getStatus(), verification.getCalibrator(),
                verification.getCalibratorEmployee(), verification.getDevice(),
                verification.getProvider(), verification.getProviderEmployee(),
                verification.getStateVerificator(), verification.getStateVerificatorEmployee(),false
        );
    }


    @RequestMapping(value = "cancel/uploadFile", method = RequestMethod.GET)
    public List<String> getBbiFile(@RequestParam String idVerification) {
        List<String> data = new ArrayList();
        String fileName = calibratorService.findBbiFileByOrganizationId(idVerification);
        data.add(idVerification);
        data.add(fileName);
        return data;
    }


    @RequestMapping(value = "deleteBbiprotocol", method = RequestMethod.PUT)
    public ResponseEntity deleteBbiprotocol(@RequestParam String idVerification) {
        HttpStatus httpStatus = HttpStatus.OK;
        try {
            calibratorService.deleteBbiFile(idVerification);
        } catch (Exception e) {
            logger.error("GOT EXCEPTION " + e.getMessage());
            httpStatus = HttpStatus.CONFLICT;
        }
        return new ResponseEntity<>(httpStatus);
    }



}
