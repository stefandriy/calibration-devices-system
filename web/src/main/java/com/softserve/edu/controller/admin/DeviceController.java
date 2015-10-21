package com.softserve.edu.controller.admin;

import com.softserve.edu.dto.PageDTO;
import com.softserve.edu.dto.admin.DeviceDTO;
import com.softserve.edu.entity.device.Device;
import com.softserve.edu.entity.enumeration.device.DeviceType;
import com.softserve.edu.service.tool.DeviceService;
import com.softserve.edu.service.user.SecurityUserDetailsService;
import com.softserve.edu.service.utils.ListToPageTransformer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/admin/device-category/")
public class DeviceController {

    private final Logger logger = Logger.getLogger(DeviceController.class);

    @Autowired
    private DeviceService deviceService;

    /**
     * Looks for available Device with id
     * @param id
     * @return boolean
     */
    @RequestMapping(value = "available/{id}", method = RequestMethod.GET)
    public boolean isValidId(@PathVariable Long id) {
        boolean isAvaible = false;
        if (id != null) {
            isAvaible = deviceService.existsWithDeviceId(id);
        }
        return isAvaible;
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public ResponseEntity addDeviceCategory(@RequestBody DeviceDTO deviceDTO) {
        HttpStatus httpStatus = HttpStatus.CREATED;
        try {
            deviceService.addDeviceCategory(
                    deviceDTO.getNumber(),
                    deviceDTO.getDeviceType(),
                    deviceDTO.getDeviceName()
            );
        } catch (Exception e) {
            logger.error("GOT EXCEPTION ", e);
            httpStatus = HttpStatus.CONFLICT;
        }
        return new ResponseEntity(httpStatus);
    }

    /**
     * Edit organization in database
     *
     * @param deviceCategoryDTO object with device category data
     * @return a response body with http status {@literal OK} if organization
     * successfully edited or else http status {@literal CONFLICT}
     */
    @RequestMapping(value = "edit/{deviceCategoryId}", method = RequestMethod.POST)
     public ResponseEntity editDeviceCategory(@RequestBody DeviceDTO deviceCategoryDTO,
                                            @PathVariable Long deviceCategoryId) {
        HttpStatus httpStatus = HttpStatus.OK;
        try {
            deviceService.editDeviceCategory(
                    deviceCategoryId,
                    deviceCategoryDTO.getNumber(),
                    deviceCategoryDTO.getDeviceType(),
                    deviceCategoryDTO.getDeviceName()
            );
        } catch (Exception e) {
            logger.error("GOT EXCEPTION ",e);
            httpStatus = HttpStatus.CONFLICT;
        }
        return new ResponseEntity(httpStatus);
    }

    @RequestMapping(value = "delete/{deviceCategoryId}", method = RequestMethod.DELETE)
    public ResponseEntity removeDeviceCategory(@PathVariable Long deviceCategoryId) {
        HttpStatus httpStatus = HttpStatus.OK;
        try {
            deviceService.removeDeviceCategory(deviceCategoryId);
        } catch (Exception e) {
            logger.error("GOT EXCEPTION ",e);
            httpStatus = HttpStatus.CONFLICT;
        }
        return new ResponseEntity(httpStatus);
    }

    @RequestMapping(value = "get/{id}")
    public DeviceDTO getDeviceCategory(@PathVariable("id") Long id) {
        Device deviceCategory = deviceService.getById(id);
        DeviceDTO deviceDTO = new DeviceDTO(deviceCategory.getId(), deviceCategory.getDeviceType().name(),
                deviceCategory.getDeviceSign(), deviceCategory.getNumber(), deviceCategory.getDeviceName());
        return deviceDTO;
    }

    @RequestMapping(value = "{pageNumber}/{itemsPerPage}/{sortCriteria}/{sortOrder}", method = RequestMethod.GET)
    public PageDTO<DeviceDTO> pageDeviceCategoryWithSearch(@PathVariable Integer pageNumber, @PathVariable Integer itemsPerPage,
                                                           @PathVariable String sortCriteria, @PathVariable String sortOrder,
                                                           DeviceDTO searchData) {
        ListToPageTransformer<Device> queryResult = deviceService.getCategoryDevicesBySearchAndPagination(
                pageNumber,
                itemsPerPage,
                searchData.getNumber(),
                searchData.getDeviceType(),
                searchData.getDeviceName(),
                sortCriteria,
                sortOrder
        );
        List<DeviceDTO> content = toDeviceDtoFromList(queryResult.getContent());
        return new PageDTO(queryResult.getTotalItems(), content);
    }


    @RequestMapping(value = "{pageNumber}/{itemsPerPage}", method = RequestMethod.GET)
    public PageDTO<DeviceDTO> getDeviceCategoryPage(@PathVariable Integer pageNumber, @PathVariable Integer itemsPerPage) {
        return pageDeviceCategoryWithSearch(pageNumber, itemsPerPage, null, null, null);
    }

    public static List<DeviceDTO> toDeviceDtoFromList(List<Device> list){
        List<DeviceDTO> resultList = new ArrayList<>();
        for (Device deviceCategory : list) {
            resultList.add(new DeviceDTO(
                    deviceCategory.getId(),
                    deviceCategory.getDeviceType().toString(),
                    deviceCategory.getDeviceSign(),
                    deviceCategory.getNumber(),
                    deviceCategory.getDeviceName()
            ));
        }
        return resultList;
    }
}
