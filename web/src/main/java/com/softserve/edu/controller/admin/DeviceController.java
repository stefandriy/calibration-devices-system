package com.softserve.edu.controller.admin;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.softserve.edu.dto.PageDTO;
import com.softserve.edu.dto.admin.DevicePageItem;
import com.softserve.edu.dto.application.ApplicationFieldDTO;
import com.softserve.edu.entity.Device;
import com.softserve.edu.service.DeviceService;

@RestController
@RequestMapping(value = "/admin/devices/")
public class DeviceController {

	@Autowired
	private DeviceService deviceService;

	@RequestMapping(value = "available/{id}", method = RequestMethod.GET)
	public boolean isValidId(@PathVariable Long id) {
		boolean isAvaible = false;
		if (id != null) {
			isAvaible = deviceService.existsWithDeviceid(id);
		}
		return isAvaible;
	}

	@RequestMapping(value = "{pageNumber}/{itemsPerPage}/{search}", method = RequestMethod.GET)
	public PageDTO<DevicePageItem> pageDevicesWithSearch(
			@PathVariable Integer pageNumber,
			@PathVariable Integer itemsPerPage, @PathVariable String search) {

		Page<DevicePageItem> page = deviceService
				.getDevicesBySearchAndPagination(pageNumber, itemsPerPage,
						search).map(
						device -> new DevicePageItem(device.getId(), device
								.getDeviceSign(), device.getNumber(), device
								.getDeviceType().toString()));

		return new PageDTO<>(page.getTotalElements(), page.getContent());

	}

	@RequestMapping(value = "{pageNumber}/{itemsPerPage}", method = RequestMethod.GET)
	public PageDTO<DevicePageItem> getDevicesPage(
			@PathVariable Integer pageNumber, @PathVariable Integer itemsPerPage) {
		return pageDevicesWithSearch(pageNumber, itemsPerPage, null);
	}

	
}
