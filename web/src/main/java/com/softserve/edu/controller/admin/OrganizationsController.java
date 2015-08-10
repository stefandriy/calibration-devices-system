package com.softserve.edu.controller.admin;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.softserve.edu.dto.PageDTO;
import com.softserve.edu.dto.admin.OrganizationDTO;
import com.softserve.edu.dto.admin.OrganizationEditDTO;
import com.softserve.edu.dto.admin.OrganizationPageItem;
import com.softserve.edu.entity.Address;
import com.softserve.edu.entity.Organization;
import com.softserve.edu.service.admin.OrganizationsService;

@RestController
@RequestMapping(value = "/admin/organization/")
public class OrganizationsController {

	@Autowired
	private OrganizationsService organizationsService;

	private final Logger logger = Logger
			.getLogger(OrganizationsController.class);

	/**
	 * Saves organization and its administrator employee in database
	 *
	 * @param organizationDTO
	 *            object with organization and employee admin data
	 * @return a response body with http status {@literal CREATED} if everything
	 *         organization and employee admin successfully created or else http
	 *         status {@literal CONFLICT}
	 */
	@RequestMapping(value = "add", method = RequestMethod.POST)
	public ResponseEntity addOrganization(
			@RequestBody OrganizationDTO organizationDTO) {
		HttpStatus httpStatus = HttpStatus.CREATED;
		Address address = new Address(organizationDTO.getRegion(),
				organizationDTO.getDistrict(), organizationDTO.getLocality(),
				organizationDTO.getStreet(), organizationDTO.getBuilding(),
				organizationDTO.getFlat());
		try {
			organizationsService.addOrganizationWithAdmin(
					organizationDTO.getName(), organizationDTO.getEmail(),
					organizationDTO.getPhone(), organizationDTO.getTypes(), organizationDTO.getEmployeesCapacity(), organizationDTO.getMaxProcessTime(),
					organizationDTO.getUsername(),
					organizationDTO.getPassword(), address);
		} catch (Exception e) {
			// TODO
			logger.error("GOT EXCEPTION " + e.getMessage());
			httpStatus = HttpStatus.CONFLICT;
		}
		return new ResponseEntity(httpStatus);
	}

	/**
	 * Responds a page according to input data and search value
	 *
	 * @param pageNumber
	 *            current page number
	 * @param itemsPerPage
	 *            count of elements per one page
	 * @param search
	 *            keyword for looking entities by Organization.name
	 * @return a page of organizations with their total amount
	 */
	@RequestMapping(value = "{pageNumber}/{itemsPerPage}/{search}", method = RequestMethod.GET)
	public PageDTO<OrganizationPageItem> pageOrganizationsWithSearch(
			@PathVariable Integer pageNumber,
			@PathVariable Integer itemsPerPage, @PathVariable String search) {

		Page<OrganizationPageItem> page = organizationsService
				.getOrganizationsBySearchAndPagination(pageNumber,
						itemsPerPage, search).map(
						organization -> new OrganizationPageItem(organization
								.getId(), organization.getName(), organization
								.getEmail(), organization.getPhone(),
								organizationsService
										.getOrganizationTypes(organization)));

		return new PageDTO<>(page.getTotalElements(), page.getContent());
	}

	/**
	 * Responds a page according to input data.
	 *
	 * <p>
	 * Note that this uses method {@code pageOrganizationsWithSearch}, whereas
	 * search values is {@literal null}
	 *
	 * @param pageNumber
	 *            current page number
	 * @param itemsPerPage
	 *            count of elements per one page
	 * @return a page of organizations with their total amount
	 */
	@RequestMapping(value = "{pageNumber}/{itemsPerPage}", method = RequestMethod.GET)
	public PageDTO<OrganizationPageItem> getOrganizationsPage(
			@PathVariable Integer pageNumber, @PathVariable Integer itemsPerPage) {
		return pageOrganizationsWithSearch(pageNumber, itemsPerPage, null);
	}
	
	@RequestMapping(value = "getOrganization/{id}")
	public Organization getOrganization(@PathVariable("id") Long id) {
		Organization organization = organizationsService
				.getOrganizationById(id);
		return organization;
	}

	/**
	 * Edit organization in database
	 *
	 * @param organization
	 *            object with organization data
	 * @return a response body with http status {@literal OK} if organization
	 *         successfully edited or else http status {@literal CONFLICT}
	 */
	@RequestMapping(value = "edit/{organizationId}", method = RequestMethod.POST)
	public ResponseEntity editOrganization(
			@RequestBody OrganizationEditDTO organization,
			@PathVariable Long organizationId) {
		HttpStatus httpStatus = HttpStatus.OK;
		Address address = new Address(organization.getRegion(),
				organization.getDistrict(), organization.getLocality(),
				organization.getStreet(), organization.getBuilding(),
				organization.getFlat());
		try {
			organizationsService.editOrganization(organizationId,
					organization.getName(), organization.getPhone(),
					organization.getEmail(), organization.getEmployeesCapacity(), organization.getMaxProcessTime(), address);
		} catch (Exception e) {
			logger.error("GOT EXCEPTION " + e.getMessage());
			httpStatus = HttpStatus.CONFLICT;
		}
		return new ResponseEntity(httpStatus);
	}
}
