package com.softserve.edu.controller.calibrator;

import com.softserve.edu.controller.provider.util.VerificationPageDTOTransformer;
import com.softserve.edu.dto.*;
import com.softserve.edu.dto.provider.VerificationDTO;
import com.softserve.edu.dto.provider.VerificationPageDTO;
import com.softserve.edu.dto.provider.VerificationReadStatusUpdateDTO;
import com.softserve.edu.entity.Organization;
import com.softserve.edu.entity.Verification;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.util.Status;
import com.softserve.edu.service.CalibrationTestService;
import com.softserve.edu.service.SecurityUserDetailsService;
import com.softserve.edu.service.admin.OrganizationService;
import com.softserve.edu.service.admin.UsersService;
import com.softserve.edu.service.calibrator.CalibratorEmployeeService;
import com.softserve.edu.service.calibrator.CalibratorService;
import com.softserve.edu.service.provider.ProviderService;
import com.softserve.edu.service.state.verificator.StateVerificatorService;
import com.softserve.edu.service.utils.ListToPageTransformer;
import com.softserve.edu.service.verification.VerificationService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

@RestController
@RequestMapping(value = "/calibrator/verifications/")
public class CalibratorController {

    private static final String contentExtPattern = "^.*\\.(bbi|BBI|)$";
    private final Logger logger = Logger.getLogger(CalibratorController.class);
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
    @Autowired
    OrganizationService organizationService;
    @Autowired
    UsersService userService;

    @RequestMapping(value = "new/{pageNumber}/{itemsPerPage}/{sortCriteria}/{sortOrder}", method = RequestMethod.GET)
    public PageDTO<VerificationPageDTO> getPageOfAllSentVerificationsByProviderIdAndSearch(
            @PathVariable Integer pageNumber, @PathVariable Integer itemsPerPage, @PathVariable String sortCriteria, @PathVariable String sortOrder, NewVerificationsFilterSearch searchData,
            @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails employeeUser) {
        User calibratorEmployee = calibratorEmployeeService.oneCalibratorEmployee(employeeUser.getUsername());
        ListToPageTransformer<Verification> queryResult = verificationService.findPageOfVerificationsByCalibratorIdAndCriteriaSearch(employeeUser.getOrganizationId(), pageNumber, itemsPerPage,
                searchData.getDate(),
                searchData.getEndDate(),
                searchData.getId(),
                searchData.getClient_full_name(),
                searchData.getStreet(),
                searchData.getRegion(),
                searchData.getDistrict(),
                searchData.getLocality(),
                searchData.getStatus(),
                searchData.getEmployee_last_name(),
                sortCriteria, sortOrder, calibratorEmployee);
        List<VerificationPageDTO> content = VerificationPageDTOTransformer.toDtoFromList(queryResult.getContent());
        return new PageDTO<VerificationPageDTO>(queryResult.getTotalItems(), content);
    }

    /**
     * Responds a page according to input data and search value
     *
     * @param pageNumber   current page number
     * @param itemsPerPage count of elements per one page
     * @param search       keyword for looking entities by CalibrationTest.name
     * @return a page of CalibrationTests with their total amount
     */
    @RequestMapping(value = "calibration-test/{pageNumber}/{itemsPerPage}/{search}/{verificationId}", method = RequestMethod.GET)
    public PageDTO<CalibrationTestPageItem> pageCalibrationTestWithSearch(@PathVariable Integer pageNumber, @PathVariable Integer itemsPerPage, @PathVariable String search, @PathVariable String verificationId) {
        Verification verification = verificationService.findById(verificationId);
        Page<CalibrationTestPageItem> page = testService.getCalibrationTestsBySearchAndPagination(pageNumber, itemsPerPage, search)
                                                        .map(calibrationTest -> new CalibrationTestPageItem(calibrationTest.getId(), calibrationTest.getName(), calibrationTest.getDateTest(),
                                                                calibrationTest.getTemperature(), calibrationTest.getSettingNumber(), calibrationTest.getLatitude(),
                                                                calibrationTest.getLongitude(), calibrationTest.getConsumptionStatus(), calibrationTest.getTestResult()));
        return new PageDTO<>(page.getTotalElements(), page.getContent());
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

        return verificatorService.findByDistrictAndType(
                calibratorService.findById(user.getOrganizationId()).getAddress().getDistrict(), "STATE_VERIFICATOR");
    }

    @RequestMapping(value = "new/update", method = RequestMethod.PUT)
    public void updateVerification(@RequestBody VerificationUpdateDTO verificationUpdateDTO) {
        for (String verificationId : verificationUpdateDTO.getIdsOfVerifications()) {
            Long idCalibrator = verificationUpdateDTO.getOrganizationId();
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
                    verification.getStateVerificatorEmployee());
        } else {
            return null;
        }
    }

    /**
     * Current method received bbi file and save in system
     *
     * @param file
     * @param idVerification
     * @return status witch depends on loading file
     */
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
        }
        catch (Exception e) {
            logger.error("Failed to load file " + e.getMessage());
            httpStatus = new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return httpStatus;
    }

    @RequestMapping(value = "archive/{pageNumber}/{itemsPerPage}/{sortCriteria}/{sortOrder}", method = RequestMethod.GET)
    public PageDTO<VerificationPageDTO> getPageOfArchivalVerificationsByOrganizationId(@PathVariable Integer pageNumber,
                                                                                       @PathVariable Integer itemsPerPage, @PathVariable String sortCriteria, @PathVariable String sortOrder, ArchiveVerificationsFilterAndSort searchData,
                                                                                       @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails employeeUser) {
        User calibratorEmployee = calibratorEmployeeService.oneCalibratorEmployee(employeeUser.getUsername());
//        System.out.println("CalibratorController searchData.getMeasurement_device_type() " + searchData.getMeasurement_device_type());
        System.out.println("prot from CalibratorController  protocol status= "+searchData.getProtocol_status());
        ListToPageTransformer<Verification> queryResult = verificationService
                .findPageOfArchiveVerificationsByCalibratorId(
                        employeeUser.getOrganizationId(),
                        pageNumber,
                        itemsPerPage,
                        searchData.getDate(),
                        searchData.getEndDate(),
                        searchData.getId(),
                        searchData.getClient_full_name(),
                        searchData.getStreet(),
                        searchData.getStatus(),
                        searchData.getEmployee_last_name(),
                        searchData.getProtocol_id(),
                        searchData.getProtocol_status(),
                        searchData.getMeasurement_device_id(),
                        searchData.getMeasurement_device_type(),
                        sortCriteria,
                        sortOrder, calibratorEmployee);
        List<VerificationPageDTO> content = VerificationPageDTOTransformer.toDtoFromList(queryResult.getContent());
        return new PageDTO<VerificationPageDTO>(queryResult.getTotalItems(), content);
    }

    @RequestMapping(value = "archive/{verificationId}", method = RequestMethod.GET)
    public VerificationDTO getArchivalVerificationDetailsById(@PathVariable String verificationId,
                                                              @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {

        Verification verification = verificationService.findByIdAndCalibratorId(verificationId,
                user.getOrganizationId());

        return new VerificationDTO(
                verification.getClientData(),
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
                verification.getStateVerificatorEmployee());
    }

    @RequestMapping(value = "new/earliest_date/calibrator", method = RequestMethod.GET)
    public String getNewVerificationEarliestDateByProviderId(@AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {
        if (user != null) {
            Organization organization = organizationService.getOrganizationById(user.getOrganizationId());
            java.util.Date gottenDate = verificationService.getNewVerificationEarliestDateByCalibrator(organization);
            java.util.Date date = null;
            if (gottenDate != null) {
                date = new Date(gottenDate.getTime());
            }
            else{
                return null;
            }
            DateTimeFormatter dbDateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
            LocalDateTime localDate = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
            String isoLocalDateString = localDate.format(dbDateTimeFormatter);
            return isoLocalDateString;
        } else {
            return null;
        }
    }

    /**
     * Find date of earliest new verification
     *
     * @param user
     * @return String date
     */
    @RequestMapping(value = "archive/earliest_date/calibrator", method = RequestMethod.GET)
    public String getArchivalVerificationEarliestDateByProviderId(@AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {
        if (user != null) {
            Organization organization = organizationService.getOrganizationById(user.getOrganizationId());
            java.util.Date gottenDate = verificationService.getArchivalVerificationEarliestDateByCalibrator(organization);
            java.util.Date date = null;
            if (gottenDate != null) {
                date = new Date(gottenDate.getTime());
            }
            else{
                return null;
            }
            DateTimeFormatter dbDateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
            LocalDateTime localDate = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
            String isoLocalDateString = localDate.format(dbDateTimeFormatter);
            return isoLocalDateString;
        } else {
            return null;
        }
    }
    /**
     * Current method search for file name witch user decided to delete
     *
     * @param idVerification
     * @return name of file and corresponding verification ID
     */
    @RequestMapping(value = "find/uploadFile", method = RequestMethod.GET)
    public List<String> getBbiFile(@RequestParam String idVerification) {
        List<String> data = new ArrayList();
        String fileName = calibratorService.findBbiFileByOrganizationId(idVerification);
        data = Arrays.asList(idVerification, fileName);
        return data;
    }

    /**
     * Current method delete file
     *
     * @param idVerification
     * @return status of deletion
     */
    @RequestMapping(value = "deleteBbiprotocol", method = RequestMethod.PUT)
    public ResponseEntity deleteBbiprotocol(@RequestParam String idVerification) {
        HttpStatus httpStatus = HttpStatus.OK;
        try {
            calibratorService.deleteBbiFile(idVerification);
        }
        catch (Exception e) {
            logger.error("GOT EXCEPTION " + e.getMessage());
            httpStatus = HttpStatus.CONFLICT;
        }
        return new ResponseEntity<>(httpStatus);
    }


}
