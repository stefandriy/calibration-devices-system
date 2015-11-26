package com.softserve.edu.controller.admin;

import com.softserve.edu.dto.PageDTO;
import com.softserve.edu.dto.admin.DevicesDTO;
import com.softserve.edu.dto.admin.UnsuitabilityReasonDTO;
import com.softserve.edu.entity.device.UnsuitabilityReason;
import com.softserve.edu.service.admin.UnsuitabilityReasonService;
import com.softserve.edu.service.tool.DeviceService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Sonka on 23.11.2015.
 */
@RestController
@RequestMapping(value = "/admin/unsuitability-reasons/", produces = "application/json")
public class UnsuitabilityReasonController {
    private final Logger logger = Logger.getLogger(DeviceController.class);
    @Autowired
    private UnsuitabilityReasonService unsuitabilityReasonService;
    @Autowired
    private DeviceService deviceService;

    /**
     * Saves unsuitability reason  in database
     *
     * @param unsuitabilityReasonDTO object with unsuitability reason data
     * @return a response body with http status {@literal CREATED} if everything
     * device category successfully created or else http status {@literal CONFLICT}
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public ResponseEntity addUnsuitabilityReason(@RequestBody UnsuitabilityReasonDTO unsuitabilityReasonDTO) {
        HttpStatus httpStatus = HttpStatus.CREATED;
        try {
            unsuitabilityReasonService.addUnsuitabilityReason(
                    unsuitabilityReasonDTO.getName(),
                    unsuitabilityReasonDTO.getDeviceId()
            );
        } catch (Exception e) {
            logger.error("Got exeption while add unsuitability reason", e);
            httpStatus = HttpStatus.CONFLICT;
        }
        return new ResponseEntity(httpStatus);
    }

    /**
     * Delete counter type
     *
     * @param reasonId Long id of unsuitability reason
     * @return a response body with http status {@literal OK} if unsuitability reason
     * successfully deleted or else http status {@literal CONFLICT}
     */
    @RequestMapping(value = "delete/{reasonId}", method = RequestMethod.DELETE)
    public ResponseEntity removeUnsuitabilityReason(@PathVariable Long reasonId) {
        HttpStatus httpStatus = HttpStatus.OK;
        try {
            unsuitabilityReasonService.removeUnsuitabilityReason(reasonId);
        } catch (Exception e) {
            logger.error("Got exeption while remove unsuitability reason", e);
            httpStatus = HttpStatus.CONFLICT;
        }
        return new ResponseEntity(httpStatus);
    }

    /**
     * Build page
     *
     * @param pageNumber
     * @param itemsPerPage
     * @return
     */
    @RequestMapping(value = "{pageNumber}/{itemsPerPage}", method = RequestMethod.GET)
    public PageDTO<UnsuitabilityReasonDTO> pageUnsuitabilityReasonsWithSearch(@PathVariable Integer pageNumber,
                                                                              @PathVariable Integer itemsPerPage) {

        List<UnsuitabilityReason> reasons = unsuitabilityReasonService.findAllUnsuitabilityReasons();
        Long count = (long) reasons.size();
        List<UnsuitabilityReasonDTO> content = toUnsuitabilityReasonDTOFromList(reasons);
        return new PageDTO<>(count, content);
    }

    /**
     * return all counter types
     *
     * @return list of device into DevicesDTO
     */
    @RequestMapping(value = "devices", method = RequestMethod.GET)
    public List<DevicesDTO> getAllDevices() {
        return deviceService.getAll().stream()
                .map(device -> new DevicesDTO(device.getId(), device.getDeviceName()))
                .collect(Collectors.toList());
    }

    /**
     * return all devices
     *
     * @return ist of devices wrapped into DeviceLightDTO
     */

    public static List<UnsuitabilityReasonDTO> toUnsuitabilityReasonDTOFromList(List<UnsuitabilityReason> list) {
        List<UnsuitabilityReasonDTO> resultList = new ArrayList<>();
        for (UnsuitabilityReason unsuitabilityReason : list) {
            resultList.add(new UnsuitabilityReasonDTO(
                    unsuitabilityReason.getId(),
                    unsuitabilityReason.getName(),
                    unsuitabilityReason.getDevice().getId(),
                    unsuitabilityReason.getDevice().getDeviceName()
            ));
        }
        return resultList;
    }
}
