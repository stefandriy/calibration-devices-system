package com.softserve.edu.controller.admin;

import com.softserve.edu.controller.admin.util.OrganizationPageDTOTransformer;
import com.softserve.edu.dto.NewOrganizationFilterSearch;
import com.softserve.edu.dto.PageDTO;
import com.softserve.edu.dto.admin.OrganizationDTO;
import com.softserve.edu.dto.admin.OrganizationEditDTO;
import com.softserve.edu.dto.admin.OrganizationPageDTO;
import com.softserve.edu.dto.admin.OrganizationPageItem;
import com.softserve.edu.entity.Address;
import com.softserve.edu.entity.Organization;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.user.UserRole;
import com.softserve.edu.service.user.SecurityUserDetailsService;
import com.softserve.edu.service.user.UserService;
import com.softserve.edu.service.admin.OrganizationService;
import com.softserve.edu.service.utils.ListToPageTransformer;
import com.softserve.edu.service.utils.OrganizationAdminDTO;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/admin/organization/")
public class OrganizationsController {

    private final Logger logger = Logger
            .getLogger(OrganizationsController.class);
    @Autowired
    private OrganizationService organizationsService;

    @Autowired
    private UserService userService;

    /**
     * Saves organization and its administrator employee in database
     *
     * @param organizationDTO object with organization and employee admin data
     * @return a response body with http status {@literal CREATED} if everything
     * organization and employee admin successfully created or else http
     * status {@literal CONFLICT}
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public ResponseEntity addOrganization(
            @RequestBody OrganizationDTO organizationDTO) {
        HttpStatus httpStatus = HttpStatus.CREATED;
        Address address = new Address(
                organizationDTO.getRegion(),
                organizationDTO.getDistrict(),
                organizationDTO.getLocality(),
                organizationDTO.getStreet(),
                organizationDTO.getBuilding(),
                organizationDTO.getFlat());
        try {
            organizationsService.addOrganizationWithAdmin(
                    organizationDTO.getName(),
                    organizationDTO.getEmail(),
                    organizationDTO.getPhone(),
                    organizationDTO.getTypes(),
                    organizationDTO.getEmployeesCapacity(),
                    organizationDTO.getMaxProcessTime(),
                    organizationDTO.getFirstName(),
                    organizationDTO.getLastName(),
                    organizationDTO.getMiddleName(),
                    organizationDTO.getUsername(),
                    organizationDTO.getPassword(),
                    address);
        } catch (Exception e) {
            // TODO
            logger.error("GOT EXCEPTION " + e.getMessage());
            httpStatus = HttpStatus.CONFLICT;
        }
        return new ResponseEntity(httpStatus);
    }

    /**
     * @param pageNumber
     * @param itemsPerPage
     * @param sortCriteria
     * @param sortOrder
     * @param searchData
     * @return
     */
    @RequestMapping(value = "{pageNumber}/{itemsPerPage}/{sortCriteria}/{sortOrder}", method = RequestMethod.GET)
    public PageDTO<OrganizationPageItem> pageOrganizationsWithSearch(
            @PathVariable Integer pageNumber, @PathVariable Integer itemsPerPage, @PathVariable String sortCriteria, @PathVariable String sortOrder,
            NewOrganizationFilterSearch searchData, @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user
    ) {


	/*	Page<OrganizationPageItem> page = organizationsService
                .getOrganizationsBySearchAndPagination(pageNumber,
						itemsPerPage, search/*, adress, type).map(
						organization -> new OrganizationPageItem(organization
								.getId(), organization.getName(), organization
								.getEmail(), organization.getPhone(),
								organizationsService
										.getOrganizationTypes(organization)));

		return new PageDTO<>(page.getTotalElements(), page.getContent());*/

        ListToPageTransformer<Organization> queryResult = organizationsService.getOrganizationsBySearchAndPagination(
                pageNumber,
                itemsPerPage,
                searchData.getName_admin(),
                searchData.getEmail(),
                searchData.getType_admin(),
                searchData.getPhone_number(),
                searchData.getRegion(),
                searchData.getDistrict(),
                searchData.getLocality(),
                searchData.getStreet(),
                sortCriteria,
                sortOrder
        );
        List<OrganizationPageDTO> content = OrganizationPageDTOTransformer.toDtoFromList(queryResult.getContent());
        return new PageDTO(queryResult.getTotalItems(), content);
    }

    /**
     * Responds a page according to input data.
     * <p>
     * <p>
     * Note that this uses method {@code pageOrganizationsWithSearch}, whereas
     * search values is {@literal null}
     *
     * @param pageNumber   current page number
     * @param itemsPerPage count of elements per one page
     * @return a page of organizations with their total amount
     */
    @RequestMapping(value = "{pageNumber}/{itemsPerPage}", method = RequestMethod.GET)
    public PageDTO<OrganizationPageItem> getOrganizationsPage(
            @PathVariable Integer pageNumber,
            @PathVariable Integer itemsPerPage) {
        return pageOrganizationsWithSearch(pageNumber, itemsPerPage, null, null, null, null);
    }

    @RequestMapping(value = "getOrganization/{id}")
    public Organization getOrganization(@PathVariable("id") Long id) {
        //OrganizationDTO	organizationDTO=new OrganizationDTO();
        Organization organization = organizationsService.getOrganizationById(id);
        return organization;
    }

    /**
     * Edit organization in database
     *
     * @param organization object with organization data
     * @return a response body with http status {@literal OK} if organization
     * successfully edited or else http status {@literal CONFLICT}
     */
    @RequestMapping(value = "edit/{organizationId}", method = RequestMethod.POST)
    public ResponseEntity editOrganization(
            @RequestBody OrganizationEditDTO organization,
            @PathVariable Long organizationId,
            @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {
        HttpStatus httpStatus = HttpStatus.OK;

        Address address = new Address(
                organization.getRegion(),
                organization.getDistrict(),
                organization.getLocality(),
                organization.getStreet(),
                organization.getBuilding(),
                organization.getFlat());
       try {
            if (organization.getTypes().equals(null)) {
                System.out.println("Nothing here");
            }
            for (String strType : organization.getTypes()) {
                System.out.println(strType);
            }

           logger.info(organization.getName());
           logger.info(organization.getPassword());
           logger.info(organization.getEmail());
           logger.info(organization.getRegion());
        logger.info(organization.getDistrict());
        logger.info(organization.getPhone());
            organizationsService.editOrganization(
                    organizationId,
                    organization.getName(),
                    organization.getPhone(),
                    organization.getEmail(),
                    organization.getTypes(),
                    organization.getEmployeesCapacity(),
                    organization.getMaxProcessTime(),
                    address,
                    organization.getPassword(),
                    organization.getUsername(),
                    organization.getFirstName(),
                    organization.getLastName(),
                    organization.getMiddleName());
        } catch (Exception e) {
            logger.error("GOT EXCEPTION " + e.getMessage());
            httpStatus = HttpStatus.CONFLICT;
        }
        organizationsService.sendOrganizationChanges(organizationId, organization.getUsername());

        return new ResponseEntity(httpStatus);
    }

    @RequestMapping(value = "getOrganizationAdmin/{id}")
    public OrganizationAdminDTO getAdmin(@PathVariable("id") Long id) {
        Organization organization = organizationsService.getOrganizationById(id);
        OrganizationAdminDTO organizationAdminDTO = new OrganizationAdminDTO();
        try {

            User user = organization
                    .getUsers()
                    .stream()
                    .filter(userChecked -> userChecked.getUserRoles()
                                    .stream()
                                    .map(UserRole::name)
                                    .filter(userRole -> userRole.equals(UserRole.PROVIDER_ADMIN.name()) ||
                                                    userRole.equals(UserRole.CALIBRATOR_ADMIN.name()) || userRole.equals(UserRole.STATE_VERIFICATOR_ADMIN.name())
                                    )
                                    .collect(Collectors.toList()).size() > 0
                    )
                    .findFirst().get();
            logger.info(user);
            organizationAdminDTO = new OrganizationAdminDTO(user.getFirstName(), user.getMiddleName(), user.getLastName(), user.getUsername());
        } catch (Exception e){
            logger.info("========================");
            logger.info("no one admin in organization");
            logger.info("========================");
        }



        logger.info("========================");
        logger.info(organization.getUsers());
        logger.info("========================");

        //--------------------
       /* //OrganizationDTO	organizationDTO=new OrganizationDTO();
        Set<String> set = organizationsService.getOrganizationTypesById(id);
        System.out.println(set);
        String[] roles = organizationsService.getOrganizationTypesById(id).toArray(new String[set.size()]);
        String role = roles[0] + "_ADMIN";
        System.out.println(roles[0]);
        System.out.println(role);

        User admin = userService.findByRoleAndOrganizationId(role, id);
        System.out.println(admin);*/
        return organizationAdminDTO;
    }
}
