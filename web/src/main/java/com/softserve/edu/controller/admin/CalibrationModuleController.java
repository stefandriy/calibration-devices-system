package com.softserve.edu.controller.admin;

import com.softserve.edu.dto.PageDTO;
import com.softserve.edu.dto.admin.CalibrationModuleDTO;
import com.softserve.edu.entity.device.CalibrationModule;
import com.softserve.edu.entity.device.Device;
import com.softserve.edu.service.admin.CalibrationModuleService;
import com.softserve.edu.service.admin.OrganizationService;
import com.softserve.edu.service.utils.TypeConverter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Created by roman on 08.11.15.
 *
 */

@RestController
@RequestMapping(value = "/admin/calibration-module/")
public class CalibrationModuleController {

    private static Logger logger = Logger.getLogger(CalibrationModuleController.class);

    @Autowired
    private CalibrationModuleService calibrationModuleService;

    @Autowired
    private OrganizationService organizationService;

    /**
     * Get calibration module by id
     *
     * @param id id of calibration module to find
     * @return calibrationModuleDTO
     */
    @RequestMapping(value = "get/{id}")
    public CalibrationModuleDTO getCalibrationModule(@PathVariable("id") Long id) {
        CalibrationModule calibrationModule = calibrationModuleService.findModuleById(id);
        return new CalibrationModuleDTO(calibrationModule.getModuleId(),
                calibrationModule.getDeviceType(),
                calibrationModule.getOrganizationCode(), calibrationModule.getCondDesignation(),
                calibrationModule.getSerialNumber(), calibrationModule.getEmployeeFullName(),
                calibrationModule.getTelephone(), calibrationModule.getModuleNumber(),
                calibrationModule.getModuleType(),
                calibrationModule.getEmail(), calibrationModule.getCalibrationType(),
                calibrationModule.getWorkDate());
    }

    /**
     * Add new calibration module
     *
     * @param calibrationModuleDTO calibration module to add
     * @return http status
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public ResponseEntity addModule(@RequestBody CalibrationModuleDTO calibrationModuleDTO) {
        HttpStatus httpStatus = HttpStatus.CREATED;
        CalibrationModule calibrationModule = new CalibrationModule(
                Device.DeviceType.valueOf(calibrationModuleDTO.getDeviceType()),
                calibrationModuleDTO.getOrganizationCode(), calibrationModuleDTO.getCondDesignation(),
                calibrationModuleDTO.getSerialNumber(), calibrationModuleDTO.getEmployeeFullName(),
                calibrationModuleDTO.getTelephone(), calibrationModuleDTO.getModuleType(),
                calibrationModuleDTO.getEmail(), calibrationModuleDTO.getCalibrationType(),
                calibrationModuleDTO.getWorkDate());
        try {
            calibrationModuleService.addCalibrationModule(calibrationModule);
        } catch (Exception e) {
            logger.error("Error when adding calibration module", e);
            httpStatus = HttpStatus.CONFLICT;
        }

        return new ResponseEntity(httpStatus);
    }

    /**
     * Edit selected calibration module
     *
     * @param calibrationModuleDTO calibration module to edit
     * @param calibrationModuleId  id of calibration module to edit
     * @return http status
     */
    @RequestMapping(value = "edit/{calibrationModuleId}", method = RequestMethod.POST)
    public ResponseEntity editModule(@RequestBody CalibrationModuleDTO calibrationModuleDTO,
                                        @PathVariable Long calibrationModuleId) {
        HttpStatus httpStatus = HttpStatus.OK;
        CalibrationModule calibrationModule = new CalibrationModule(
                Device.DeviceType.valueOf(calibrationModuleDTO.getDeviceType()),
                calibrationModuleDTO.getOrganizationCode(), calibrationModuleDTO.getCondDesignation(),
                calibrationModuleDTO.getSerialNumber(), calibrationModuleDTO.getEmployeeFullName(),
                calibrationModuleDTO.getTelephone(), calibrationModuleDTO.getModuleType(),
                calibrationModuleDTO.getEmail(), calibrationModuleDTO.getCalibrationType(),
                calibrationModuleDTO.getWorkDate());
        try {
            calibrationModuleService.updateCalibrationModule(calibrationModuleId, calibrationModule);
        } catch (Exception e) {
            logger.error("GOT EXCEPTION WHEN UPDATE CALIBRATION MODULE", e);
            httpStatus = HttpStatus.CONFLICT;
        }
        return new ResponseEntity(httpStatus);
    }

    /**
     * Disable calibration module
     *
     * @param calibrationModuleId id of calibration module to disable
     * @return http status
     */
    @RequestMapping(value = "disable/{calibrationModuleId}", method = RequestMethod.GET)
    public ResponseEntity disableModule(@PathVariable Long calibrationModuleId) {
        HttpStatus httpStatus = HttpStatus.OK;
        try {
            calibrationModuleService.disableCalibrationModule(calibrationModuleId);
        } catch (Exception e) {
            logger.error("GOT EXCEPTION ", e);
            httpStatus = HttpStatus.CONFLICT;
        }
        return new ResponseEntity(httpStatus);
    }

    /**
     * Return page of calibration module according to filters and sort order
     *
     * @param pageNumber   number of page to return
     * @param itemsPerPage count of items on page
     * @param sortCriteria sorting criteria
     * @param sortOrder    order of sorting
     * @param searchData   filtering data
     * @return sorted and filtered page of calibration modules
     */
    @RequestMapping(value = "{pageNumber}/{itemsPerPage}/{sortCriteria}/{sortOrder}", method = RequestMethod.GET)
    public PageDTO<CalibrationModuleDTO> getSortedAndFilteredPageOfCalibrationModules(@PathVariable Integer pageNumber,
                 @PathVariable Integer itemsPerPage, @PathVariable String sortCriteria,
                 @PathVariable String sortOrder, CalibrationModuleDTO searchData) {
        // converting object to map and filtering the map to have only not-null fields
        Map<String, String> searchDataMap = new HashMap<String, String>();
        if (searchData != null) {
            searchDataMap = TypeConverter.ObjectToMap(searchData);
        }
        searchDataMap.put("isActive", "true");
        // creating Sort object for using as a parameter for Pageable creation
        Sort sort;
        if ((sortCriteria.equals("undefined") && sortOrder.equals("undefined")) ||
                sortCriteria == null && sortOrder == null) {
            sort = new Sort(Sort.Direction.DESC, "moduleId");
        } else {
            sort = new Sort(Sort.Direction.valueOf(sortOrder.toUpperCase()), sortCriteria);
        }
        Pageable pageable = new PageRequest(pageNumber - 1, itemsPerPage, sort);
        // fetching data from database, receiving a sorted and filtered page of calibration modules
        Page<CalibrationModule> queryResult = calibrationModuleService
                .getFilteredPageOfCalibrationModule(searchDataMap, pageable);
        List<CalibrationModuleDTO> content = new ArrayList<CalibrationModuleDTO>();
        // converting Page of CalibrationModules to List of CalibrationModuleDTOs
        for (CalibrationModule calibrationModule: queryResult) {
            content.add(new CalibrationModuleDTO(calibrationModule.getModuleId(),
                    calibrationModule.getDeviceType(), calibrationModule.getOrganizationCode(),
                    calibrationModule.getCondDesignation(), calibrationModule.getSerialNumber(),
                    calibrationModule.getEmployeeFullName(), calibrationModule.getTelephone(),
                    calibrationModule.getModuleNumber(), calibrationModule.getModuleType(),
                    calibrationModule.getEmail(), calibrationModule.getCalibrationType(),
                    calibrationModule.getWorkDate()));
        }
        return new PageDTO<>(queryResult.getTotalElements(), content);
    }

    /**
     * Return page of calibration modules
     * @param pageNumber   number of page to return
     * @param itemsPerPage count of items on page
     * @return page of calibration module
     */
    @RequestMapping(value = "{pageNumber}/{itemsPerPage}", method = RequestMethod.GET)
    public PageDTO<CalibrationModuleDTO> getPageOfCalibrationModules(@PathVariable Integer pageNumber,
                                                               @PathVariable Integer itemsPerPage) {
        return getSortedAndFilteredPageOfCalibrationModules(pageNumber, itemsPerPage, null, null, null);
    }

}
