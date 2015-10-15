package com.softserve.edu.controller.admin;

import com.softserve.edu.dto.PageDTO;
import com.softserve.edu.dto.admin.DeviceDTO;
import com.softserve.edu.entity.device.Device;
import com.softserve.edu.service.tool.DeviceService;
import com.softserve.edu.service.user.SecurityUserDetailsService;
import com.softserve.edu.service.utils.ListToPageTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/admin/device-category/")
public class DeviceController {

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
