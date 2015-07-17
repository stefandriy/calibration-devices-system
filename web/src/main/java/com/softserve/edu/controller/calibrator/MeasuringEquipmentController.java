//package com.softserve.edu.controller.calibrator;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.softserve.edu.dto.PageDTO;
//import com.softserve.edu.dto.calibrator.MeasuringEquipmentPageItem;
//import com.softserve.edu.service.MeasuringEquipmentService;
//
//
//
//
//@RestController
//@RequestMapping(value = "/calibrator/mEquipment/")
//public class MeasuringEquipmentController {
//
//	@Autowired
//	private MeasuringEquipmentService measuringEquipmentService;
//	
//	@RequestMapping(value = "{pageNumber}/{itemsPerPage}/{search}", method = RequestMethod.GET)
//	public PageDTO<MeasuringEquipmentPageItem> pageDevicesWithSearch(
//			@PathVariable Integer pageNumber,
//			@PathVariable Integer itemsPerPage, @PathVariable String search) {
//
//		Page<MeasuringEquipmentPageItem> page = measuringEquipmentService
//				.getDevicesBySearchAndPagination(pageNumber, itemsPerPage, search)
//				.map(
//						measuringEquipment -> new MeasuringEquipmentPageItem( 
//								name, equipmentType, manufacturer, verificationInterval);
//
//		return new PageDTO<>(page.getTotalElements(), page.getContent());
//
//	}
//
//	@RequestMapping(value = "{pageNumber}/{itemsPerPage}", method = RequestMethod.GET)
//	public PageDTO<MeasuringEquipmentPageItem> getDevicesPage(
//			@PathVariable Integer pageNumber, @PathVariable Integer itemsPerPage) {
//		return pageDevicesWithSearch(pageNumber, itemsPerPage, null);
//	}
//}
