package com.softserve.edu.controller.admin;

import com.softserve.edu.dto.PageDTO;
import com.softserve.edu.dto.admin.DevicePageItem;
import com.softserve.edu.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/admin/devices/")
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

    /**
     * Responds a page according to input data and search value
     *
     * @param pageNumber   current page number
     * @param itemsPerPage count of elements per one page
     * @param search       keyword for looking entities by Device.number
     * @return a page of Devices with their total amount
     */
    @RequestMapping(value = "{pageNumber}/{itemsPerPage}/{search}", method = RequestMethod.GET)
    public PageDTO<DevicePageItem> pageDevicesWithSearch(@PathVariable Integer pageNumber, @PathVariable Integer itemsPerPage, @PathVariable String search) {
        Page<DevicePageItem> page = deviceService.getDevicesBySearchAndPagination(pageNumber, itemsPerPage, search)
                .map(device -> new DevicePageItem(device.getId(), device.getDeviceSign(), device.getNumber(), device.getDeviceType().toString(),
                        device.getDeviceName().toString()));
        return new PageDTO<>(page.getTotalElements(), page.getContent());

    }

    @RequestMapping(value = "{pageNumber}/{itemsPerPage}", method = RequestMethod.GET)
    public PageDTO<DevicePageItem> getDevicesPage(@PathVariable Integer pageNumber, @PathVariable Integer itemsPerPage) {
        return pageDevicesWithSearch(pageNumber, itemsPerPage, null);
    }


}
