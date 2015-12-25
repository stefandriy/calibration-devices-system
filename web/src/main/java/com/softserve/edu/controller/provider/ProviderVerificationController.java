package com.softserve.edu.controller.provider;

import com.softserve.edu.controller.provider.util.VerificationPageDTOTransformer;
import com.softserve.edu.dto.*;
import com.softserve.edu.dto.admin.OrganizationDTO;
import com.softserve.edu.dto.application.ClientStageVerificationDTO;
import com.softserve.edu.dto.provider.*;
import com.softserve.edu.entity.Address;
import com.softserve.edu.entity.enumeration.organization.OrganizationType;
import com.softserve.edu.entity.enumeration.user.UserRole;
import com.softserve.edu.entity.enumeration.verification.Status;
import com.softserve.edu.entity.organization.Organization;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.verification.ClientData;
import com.softserve.edu.entity.verification.Verification;
import com.softserve.edu.service.admin.OrganizationService;
import com.softserve.edu.service.admin.UsersService;
import com.softserve.edu.service.calibrator.CalibratorService;
import com.softserve.edu.service.provider.ProviderEmployeeService;
import com.softserve.edu.service.provider.ProviderService;
import com.softserve.edu.service.tool.MailService;
import com.softserve.edu.service.user.SecurityUserDetailsService;
import com.softserve.edu.service.utils.EmployeeDTO;
import com.softserve.edu.service.utils.ListToPageTransformer;
import com.softserve.edu.service.verification.VerificationProviderEmployeeService;
import com.softserve.edu.service.verification.VerificationService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/provider/verifications/")
public class ProviderVerificationController {

    @Autowired
    VerificationService verificationService;

    @Autowired
    ProviderService providerService;

    @Autowired
    ProviderEmployeeService providerEmployeeService;

    @Autowired
    OrganizationService organizationService;

    @Autowired
    CalibratorService calibratorService;

    @Autowired
    VerificationProviderEmployeeService verificationProviderEmployeeService;

    @Autowired
    private OrganizationService organizationServiceImpl;

    @Autowired
    private UsersService usersService;

    @Autowired
    private MailService mailService;

    private final Logger logger = Logger.getLogger(ProviderVerificationController.class);

    @RequestMapping(value = "archive/{pageNumber}/{itemsPerPage}/{sortCriteria}/{sortOrder}", method = RequestMethod.GET)
    public PageDTO<VerificationPageDTO> getPageOfArchivalVerificationsByOrganizationId(@PathVariable Integer pageNumber, @PathVariable Integer itemsPerPage, @PathVariable String sortCriteria, @PathVariable String sortOrder,
                                                                                       ArchiveVerificationsFilterAndSort searchData, @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails employeeUser) {


        User providerEmployee = providerEmployeeService.oneProviderEmployee(employeeUser.getUsername());
        ListToPageTransformer<Verification> queryResult = verificationService.findPageOfArchiveVerificationsByProviderId(
                employeeUser.getOrganizationId(),
                pageNumber,
                itemsPerPage,
                searchData.getDate(),
                searchData.getEndDate(),
                searchData.getId(),
                searchData.getClient_full_name(), //TODO: WHY????!!!
                searchData.getStreet(),
                searchData.getRegion(),
                searchData.getDistrict(),
                searchData.getLocality(),
                searchData.getStatus(),
                searchData.getEmployee_last_name(), //TODO: WHY????!!!
                sortCriteria,
                sortOrder,
                providerEmployee
        );
        List<VerificationPageDTO> content = VerificationPageDTOTransformer.toDtoFromList(queryResult.getContent());
        return new PageDTO<>(queryResult.getTotalItems(), content);
    }

    /**
     * Find page of verifications by specific criterias
     *
     * @param pageNumber
     * @param itemsPerPage
     * @param employeeUser
     * @return PageDTO<VerificationPageDTO>
     */
    @RequestMapping(value = "new/{pageNumber}/{itemsPerPage}/{sortCriteria}/{sortOrder}", method = RequestMethod.GET)
    public PageDTO<VerificationPageDTO> getPageOfAllSentVerificationsByProviderIdAndSearch(@PathVariable Integer pageNumber, @PathVariable Integer itemsPerPage, @PathVariable String sortCriteria, @PathVariable String sortOrder,
                                                                                           NewVerificationsFilterSearch searchData, @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails employeeUser) {

        User providerEmployee = providerEmployeeService.oneProviderEmployee(employeeUser.getUsername());
        ListToPageTransformer<Verification> queryResult = verificationService.findPageOfSentVerificationsByProviderIdAndCriteriaSearch(
                employeeUser.getOrganizationId(),
                pageNumber,
                itemsPerPage,
                searchData.getDate(),
                searchData.getEndDate(),
                searchData.getId(),
                searchData.getClient_full_name(), //TODO: WHY????!!!
                searchData.getStreet(),
                searchData.getRegion(),
                searchData.getDistrict(),
                searchData.getLocality(),
                searchData.getStatus(),
                searchData.getEmployee_last_name(), //TODO: WHY????!!!
                sortCriteria,
                sortOrder,
                providerEmployee
        );
        List<Verification> verifications = queryResult.getContent();
        List<VerificationPageDTO> content = VerificationPageDTOTransformer.toDtoFromList(verifications);
        return new PageDTO<>(queryResult.getTotalItems(), content);
    }


    /**
     * Find page of verifications by specific criterias on main panel
     *
     * @param pageNumber
     * @param itemsPerPage
     * @param employeeUser
     * @return PageDTO<VerificationPageDTO>
     */
    @RequestMapping(value = "new/mainpanel/{pageNumber}/{itemsPerPage}", method = RequestMethod.GET)
    public PageDTO<VerificationPageDTO> getPageOfAllSentVerificationsByProviderIdAndSearchOnMainPanel(@PathVariable Integer pageNumber, @PathVariable Integer itemsPerPage,
                                                                                                      NewVerificationsSearch searchData, @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails employeeUser) {

        User providerEmployee = providerEmployeeService.oneProviderEmployee(employeeUser.getUsername());
        ListToPageTransformer<Verification> queryResult = verificationService.findPageOfArchiveVerificationsByProviderIdOnMainPanel(
                employeeUser.getOrganizationId(),
                pageNumber,
                itemsPerPage,
                searchData.getFormattedDate(),
                searchData.getIdText(),
                searchData.getClient_full_name(),
                searchData.getStreetText(),
                searchData.getRegion(),
                searchData.getDistrict(),
                searchData.getLocality(),
                searchData.getStatus(),
                searchData.getEmployee(),
                providerEmployee);
        List<VerificationPageDTO> content = VerificationPageDTOTransformer.toDtoFromList(queryResult.getContent());
        return new PageDTO<>(queryResult.getTotalItems(), content);
    }

    /**
     * Find date of earliest new verification
     *
     * @param user
     * @return String date
     */
    @RequestMapping(value = "new/earliest_date/provider", method = RequestMethod.GET)
    public String getNewVerificationEarliestDateByProviderId(@AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {
        if (user != null) {
            Organization organization = organizationServiceImpl.getOrganizationById(user.getOrganizationId());
            java.util.Date gottenDate = verificationService.getNewVerificationEarliestDateByProvider(organization);
            java.util.Date date = null;
            if (gottenDate != null) {
                date = new Date(gottenDate.getTime());
            } else {
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
    @RequestMapping(value = "archive/earliest_date/provider", method = RequestMethod.GET)
    public String getArchivalVerificationEarliestDateByProviderId(@AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {
        if (user != null) {
            Organization organization = organizationServiceImpl.getOrganizationById(user.getOrganizationId());
            java.util.Date gottenDate = verificationService.getArchivalVerificationEarliestDateByProvider(organization);
            java.util.Date date = null;
            if (gottenDate != null) {
                date = new Date(gottenDate.getTime());
            } else {
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
     * Find count of new verifications that have Read Status "UNREAD"
     *
     * @return Long count
     */
    @RequestMapping(value = "new/count/provider", method = RequestMethod.GET)
    public Long getCountOfNewVerificationsByProviderId(@AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {
        if (user != null) {
            return verificationService.findCountOfNewVerificationsByProviderId(user.getOrganizationId());
        } else {
            return null;
        }
    }


    /**
     * Find calibrators which correspond provider agreements
     *
     * @return calibrator
     */
    @RequestMapping(value = "new/calibrators", method = RequestMethod.GET)
    public Set<OrganizationDTO> updateVerification(@AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {
        //todo agreement
        Organization userOrganization = organizationService.getOrganizationById(user.getOrganizationId());
        return organizationService.findByIdAndTypeAndActiveAgreementDeviceType(user.getOrganizationId(),
                OrganizationType.CALIBRATOR, userOrganization.getDeviceTypes().iterator().next()).stream()
                .map(organization -> new OrganizationDTO(organization.getId(), organization.getName()))
                .collect(Collectors.toSet());
    }


    @RequestMapping(value = "new/providerEmployees", method = RequestMethod.GET)
    public List<EmployeeDTO> employeeVerification(
            @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {
        User employee = providerEmployeeService.oneProviderEmployee(user.getUsername());
        List<String> role = usersService.getRoles(user.getUsername());

        return providerEmployeeService
                .getAllProviders(role, employee);
    }

    /**
     * Update verificationsproviderListEmployee
     */
    @RequestMapping(value = "new/update", method = RequestMethod.PUT)
    public void updateVerification(
            @RequestBody VerificationUpdateDTO verificationUpdateDTO) {

        for (String verificationId : verificationUpdateDTO.getIdsOfVerifications()) {
            Long idCalibrator = verificationUpdateDTO.getOrganizationId();
            Organization calibrator = calibratorService.findById(idCalibrator);
            verificationService.sendVerificationTo(verificationId, calibrator, Status.IN_PROGRESS);
        }
    }


    @RequestMapping(value = "new/reject", method = RequestMethod.PUT)
    public void rejectVerification(@RequestBody VerificationStatusUpdateDTO verificationReadStatusUpdateDTO) {
        verificationService.updateVerificationStatus(verificationReadStatusUpdateDTO.getVerificationId(),
                Status.valueOf(verificationReadStatusUpdateDTO.getStatus().toUpperCase()));
    }

    @RequestMapping(value = "new/accept", method = RequestMethod.PUT)
    public void acceptVerification(@RequestBody VerificationStatusUpdateDTO verificationReadStatusUpdateDTO) {
        verificationService.updateVerificationStatus(verificationReadStatusUpdateDTO.getVerificationId(),
                Status.valueOf(verificationReadStatusUpdateDTO.getStatus().toUpperCase()));

    }

    @RequestMapping(value = "new/read", method = RequestMethod.PUT)
    public void markVerificationAsRead(@RequestBody VerificationReadStatusUpdateDTO verificationDto) {
        verificationService.updateVerificationReadStatus(verificationDto.getVerificationId(), verificationDto.getReadStatus());
    }

    @RequestMapping(value = "assign/providerEmployee", method = RequestMethod.PUT)
    public void assignProviderEmployee(@RequestBody VerificationProviderEmployeeDTO verificationProviderEmployeeDTO) {

        String userNameProvider = verificationProviderEmployeeDTO.getEmployeeProvider().getUsername();

        String idVerification = verificationProviderEmployeeDTO.getIdVerification();

        User employeeProvider = verificationProviderEmployeeService.oneProviderEmployee(userNameProvider);

        verificationProviderEmployeeService.assignProviderEmployee(idVerification, employeeProvider);
    }

    @RequestMapping(value = "remove/providerEmployee", method = RequestMethod.PUT)
    public void removeProviderEmployee(@RequestBody VerificationProviderEmployeeDTO verificationUpdatingDTO) {
        String idVerification = verificationUpdatingDTO.getIdVerification();
        verificationProviderEmployeeService.assignProviderEmployee(idVerification, null);
    }


    @RequestMapping(value = "new/{verificationId}", method = RequestMethod.GET)
    public VerificationDTO getNewVerificationDetailsById(@PathVariable String verificationId) {
        Verification verification = verificationService.findById(verificationId);
        if (verification != null) {
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
                    verification.getStateVerificatorEmployee());
        } else {
            return null;
        }
    }


    @RequestMapping(value = "archive/{verificationId}", method = RequestMethod.GET)
    public VerificationDTO getArchivalVerificationDetailsById(@PathVariable String verificationId, @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user, VerificationDTO verificationDTO) {

        Verification verification = verificationService.findByIdAndProviderId(verificationId, user.getOrganizationId());

        return new VerificationDTO(verification.getClientData(), verification.getId(), verification.getInitialDate(),
                verification.getExpirationDate(), verification.getStatus(), verification.getCalibrator(),
                verification.getCalibratorEmployee(), verification.getDevice(), verification.getProvider(),
                verification.getProviderEmployee(), verification.getStateVerificator(),
                verification.getStateVerificatorEmployee(), verification.getRejectedMessage());//add rejectMessage
    }

    /**
     * Check if current user is Employee
     *
     * @param user
     * @return true if user has role PROVIDER_EMPLOYEE
     * false if user has role PROVIDER_ADMIN
     */
    @RequestMapping(value = "provider/role", method = RequestMethod.GET)
    public Boolean isEmployeeProvider(
            @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {
        User checkedUser = usersService.findOne(user.getUsername());
        return checkedUser.getUserRoles().contains(UserRole.PROVIDER_EMPLOYEE);
    }

    @RequestMapping(value = "editCounterInfo", method = RequestMethod.PUT)
    public ResponseEntity editCounterInfo(@RequestBody CounterInfoDTO counterInfo) {
        HttpStatus httpStatus = HttpStatus.OK;

        try {
            verificationService.editCounter(counterInfo.getVerificationId(), counterInfo.getDeviceName(), counterInfo.getDismantled(),
                    counterInfo.getSealPresence(), counterInfo.getDateOfDismantled(), counterInfo.getDateOfMounted(),
                    counterInfo.getNumberCounter(), counterInfo.getReleaseYear(), counterInfo.getSymbol(),
                    counterInfo.getStandardSize(), counterInfo.getComment(), counterInfo.getDeviceId());
        } catch (Exception e) {
            logger.error("GOT EXCEPTION " + e);
            httpStatus = HttpStatus.CONFLICT;
        }
        return new ResponseEntity<>(httpStatus);
    }

    @RequestMapping(value = "saveInfo", method = RequestMethod.PUT)
    public ResponseEntity saveAddInfo(@RequestBody AdditionalInfoDTO infoDTO) {
        HttpStatus httpStatus = HttpStatus.OK;

        try {
            // pass infoDTO
            verificationService.editAddInfo(infoDTO.getEntrance(), infoDTO.getDoorCode(), infoDTO.getFloor(),
                    infoDTO.getDateOfVerif(), infoDTO.getTimeFrom(), infoDTO.getTimeTo(), infoDTO.isServiceability(), infoDTO.getNoWaterToDate(),
                    infoDTO.getNotes(), infoDTO.getVerificationId());
        } catch (Exception e) {
            logger.error("GOT EXCEPTION " + e);
            httpStatus = HttpStatus.CONFLICT;
        }
        return new ResponseEntity<>(httpStatus);
    }

    @RequestMapping(value = "editClientInfo", method = RequestMethod.PUT)
    public ResponseEntity editClientInfo(@RequestBody ClientStageVerificationDTO clientDTO) {
        HttpStatus httpStatus = HttpStatus.OK;

        Address address = new Address(clientDTO.getRegion(),
                clientDTO.getDistrict(),
                clientDTO.getLocality(),
                clientDTO.getStreet(),
                clientDTO.getBuilding(),
                clientDTO.getFlat()
        );

        ClientData clientData = new ClientData(
                clientDTO.getFirstName(),
                clientDTO.getLastName(),
                clientDTO.getMiddleName(),
                clientDTO.getEmail(),
                clientDTO.getPhone(),
                clientDTO.getSecondPhone(),
                address
        );

        try {
            verificationService.editClientInfo(clientDTO.getVerificationId(), clientData);
        } catch (Exception e) {
            logger.error("GOT EXCEPTION " + e);
            httpStatus = HttpStatus.CONFLICT;
        }
        return new ResponseEntity<>(httpStatus);
    }

}
