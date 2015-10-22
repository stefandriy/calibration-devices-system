package com.softserve.edu.controller.admin;

import com.softserve.edu.dto.PageDTO;
import com.softserve.edu.dto.admin.AgreementDTO;
import com.softserve.edu.entity.enumeration.device.DeviceType;
import com.softserve.edu.entity.organization.Agreement;
import com.softserve.edu.service.admin.AgreementService;
import com.softserve.edu.service.admin.OrganizationService;
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
    private OrganizationService organizationService;

    @Autowired
    private AgreementService agreementService;

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public ResponseEntity addAgreement(@RequestBody AgreementDTO agreementDTO) {
        HttpStatus httpStatus = HttpStatus.CREATED;
        try {
            agreementService.add(agreementDTO.getCustomerId(), agreementDTO.getExecutorId(), agreementDTO.getNumber(),
                    agreementDTO.getDeviceCount(), new Date(), DeviceType.valueOf(agreementDTO.getDeviceType()));
        } catch (Exception e) {
            logger.error("Error when adding agreement", e);
            httpStatus = HttpStatus.CONFLICT;
        }

        return new ResponseEntity(httpStatus);
    }

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
                sortCriteria,
                sortOrder
        );
        List<AgreementDTO> content = queryResult.getContent().stream()
                .map(Agreement -> new AgreementDTO(Agreement.getId(), Agreement.getCustomer().getId(),
                        Agreement.getExecutor().getId(), Agreement.getCustomer().getName(),
                        Agreement.getExecutor().getName(), Agreement.getNumber(), Agreement.getDeviceCount(), Agreement.getDeviceType().name()))
                .collect(Collectors.toList());
        return new PageDTO(queryResult.getTotalItems(), content);
    }


    @RequestMapping(value = "{pageNumber}/{itemsPerPage}", method = RequestMethod.GET)
    public PageDTO<AgreementDTO> getDeviceCategoryPage(@PathVariable Integer pageNumber, @PathVariable Integer itemsPerPage) {
        return pageDeviceCategoryWithSearch(pageNumber, itemsPerPage, null, null, null);
    }
}
