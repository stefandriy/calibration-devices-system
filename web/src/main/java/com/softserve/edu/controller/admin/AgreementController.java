package com.softserve.edu.controller.admin;

import com.softserve.edu.dto.PageDTO;
import com.softserve.edu.dto.admin.AgreementDTO;
import com.softserve.edu.entity.device.Device;
import com.softserve.edu.entity.organization.Agreement;
import com.softserve.edu.service.admin.AgreementService;
import com.softserve.edu.service.utils.ListToPageTransformer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/admin/agreement/")
public class AgreementController {

    private static Logger logger = Logger.getLogger(AgreementController.class);

    @Autowired
    private AgreementService agreementService;

    /**
     * Get agreement by id
     * @param id id of agreement to find
     * @return agreementDTO
     */
    @RequestMapping(value = "get/{id}")
    public AgreementDTO getAgreement(@PathVariable("id") Long id) {
        Agreement agreement = agreementService.findAgreementById(id);
        return new AgreementDTO(agreement.getId(), agreement.getCustomer().getId(),
                agreement.getExecutor().getId(), agreement.getCustomer().getName(), agreement.getExecutor().getName(),
                agreement.getNumber(), agreement.getDeviceCount(), agreement.getDeviceType().name(),
                agreement.getCustomer().getOrganizationTypes().iterator().next().name());
    }

    /**
     * Add new agreement
     * @param agreementDTO agreement to add
     * @return http status
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public ResponseEntity addAgreement(@RequestBody AgreementDTO agreementDTO) {
        HttpStatus httpStatus = HttpStatus.CREATED;
        try {
            agreementService.add(agreementDTO.getCustomerId(), agreementDTO.getExecutorId(), agreementDTO.getNumber(),
                    agreementDTO.getDeviceCount(), new Date(), Device.DeviceType.valueOf(agreementDTO.getDeviceType()));
        } catch (Exception e) {
            logger.error("Error when adding agreement", e);
            httpStatus = HttpStatus.CONFLICT;
        }

        return new ResponseEntity(httpStatus);
    }

    /**
     * Edit selected agreement
     * @param agreementDTO agreement to edit
     * @param agreementId id of agreement to edit
     * @return http status
     */
    @RequestMapping(value = "edit/{agreementId}", method = RequestMethod.POST)
    public ResponseEntity editAgreement(@RequestBody AgreementDTO agreementDTO,
                                             @PathVariable Long agreementId) {
        HttpStatus httpStatus = HttpStatus.OK;
        try {
            agreementService.update(agreementId, agreementDTO.getCustomerId(), agreementDTO.getExecutorId(),
                    agreementDTO.getNumber(), agreementDTO.getDeviceCount(), new Date(), Device.DeviceType.valueOf(agreementDTO.getDeviceType()));
        } catch (Exception e) {
            logger.error("GOT EXCEPTION WHEN UPDATE AGREEMENT", e);
            httpStatus = HttpStatus.CONFLICT;
        }
        return new ResponseEntity(httpStatus);
    }

    /**
     * Disable agreement
     * @param agreementId id of agreement to disable
     * @return http status
     */
    @RequestMapping(value = "disable/{agreementId}", method = RequestMethod.GET)
    public ResponseEntity disableAgreement(@PathVariable Long agreementId) {
        HttpStatus httpStatus = HttpStatus.OK;
        try {
            agreementService.disableAgreement(agreementId);
        } catch (Exception e) {
            logger.error("GOT EXCEPTION ",e);
            httpStatus = HttpStatus.CONFLICT;
        }
        return new ResponseEntity(httpStatus);
    }

    /**
     * Return page of agreements according to filters and sort order
     * @param pageNumber number of page to return
     * @param itemsPerPage count of items on page
     * @param sortCriteria sorting criteria
     * @param sortOrder order of sorting
     * @param searchData filtering data
     * @return sorted and filtered page of agreement
     */
    @RequestMapping(value = "{pageNumber}/{itemsPerPage}/{sortCriteria}/{sortOrder}", method = RequestMethod.GET)
    public PageDTO<AgreementDTO> pageDeviceCategoryWithSearch(@PathVariable Integer pageNumber, @PathVariable Integer itemsPerPage,
                                                              @PathVariable String sortCriteria, @PathVariable String sortOrder,
                                                              AgreementDTO searchData) {
        ListToPageTransformer<Agreement> queryResult = agreementService.getCategoryDevicesBySearchAndPagination(
                pageNumber,
                itemsPerPage,
                searchData.getCustomerName(),
                searchData.getExecutorName(),
                searchData.getNumber(),
                searchData.getDeviceCount() == null ? null : searchData.getDeviceCount().toString(),
                searchData.getCustomerName(),
                searchData.getDeviceType(),
                "true",
                sortCriteria,
                sortOrder
        );
        List<AgreementDTO> content = queryResult.getContent().stream()
                .map(Agreement -> new AgreementDTO(Agreement.getId(), Agreement.getCustomer().getId(),
                        Agreement.getExecutor().getId(), Agreement.getCustomer().getName(),
                        Agreement.getExecutor().getName(), Agreement.getNumber(), Agreement.getDeviceCount(), Agreement.getDeviceType().name()))
                .collect(Collectors.toList());
        return new PageDTO<>(queryResult.getTotalItems(), content);
    }


    @RequestMapping(value = "{pageNumber}/{itemsPerPage}", method = RequestMethod.GET)
    public PageDTO<AgreementDTO> getDeviceCategoryPage(@PathVariable Integer pageNumber, @PathVariable Integer itemsPerPage) {
        return pageDeviceCategoryWithSearch(pageNumber, itemsPerPage, null, null, null);
    }
}
