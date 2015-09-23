package com.softserve.edu.controller.admin;

import com.softserve.edu.controller.admin.util.OrganizationHistoryPageDTOTransformer;
import com.softserve.edu.controller.admin.util.OrganizationPageDTOTransformer;
import com.softserve.edu.dto.NewOrganizationFilterSearch;
import com.softserve.edu.dto.PageDTO;
import com.softserve.edu.dto.admin.*;
import com.softserve.edu.entity.*;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.user.UserRole;
import com.softserve.edu.entity.util.Roles;
import com.softserve.edu.repository.OrganizationRepository;
import com.softserve.edu.repository.UserRepository;
import com.softserve.edu.service.SecurityUserDetailsService;
import com.softserve.edu.service.UserService;
import com.softserve.edu.service.admin.OrganizationsService;
import com.softserve.edu.service.utils.ListToPageTransformer;
import com.softserve.edu.service.utils.OrganizationAdminDTO;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Role;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/admin/organization/")
public class OrganizationsController {

    private final Logger logger = Logger
            .getLogger(OrganizationsController.class);
    @Autowired
    private OrganizationsService organizationsService;


    @Autowired
    private OrganizationRepository organizationRepository;


    @Autowired
    private UserRepository userRepository;

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
            @RequestBody OrganizationDTO organizationDTO,
            @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {
        HttpStatus httpStatus = HttpStatus.CREATED;
        Address address = new Address(
                organizationDTO.getRegion(),
                organizationDTO.getDistrict(),
                organizationDTO.getLocality(),
                organizationDTO.getStreet(),
                organizationDTO.getBuilding(),
                organizationDTO.getFlat());
        try {
            String adminName = user.getUsername();
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
                    address,
                    adminName
                    );
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
    public OrganizationDTO getOrganization(@PathVariable("id") Long id) {
        Organization organization = organizationsService.getOrganizationById(id);

        OrganizationDTO	organizationDTO=new OrganizationDTO(organization.getId() ,organization.getName(), organization.getEmail(), organization.getPhone(),
        organization.getEmployeesCapacity(), organization.getMaxProcessTime(), organization.getAddress().getRegion(), organization.getAddress().getDistrict(), organization.getAddress().getLocality(),
            organization.getAddress().getStreet(), organization.getAddress().getBuilding(), organization.getAddress().getFlat());
        return organizationDTO;
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
            for (String strType : organization.getTypes()) {System.out.println(strType);
            }
           
           String adminName = user.getUsername();
           
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
                    organization.getMiddleName(),
                    adminName);
        } catch (Exception e) {
            logger.error("GOT EXCEPTION " + e.getMessage());
            httpStatus = HttpStatus.CONFLICT;
        }

        Organization org = organizationRepository
                .findOne(organizationId);

        User admin = userRepository
                .findByUsername(organization.getUsername());

        organizationsService.sendOrganizationChanges(org, admin);

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
                                    .map(UserRole::getRole)
                                    .filter(userRole -> userRole.equals(Roles.PROVIDER_ADMIN.name()) ||
                                                    userRole.equals(Roles.CALIBRATOR_ADMIN.name()) || userRole.equals(Roles.STATE_VERIFICATOR_ADMIN.name())
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
        return organizationAdminDTO;
    }

    @RequestMapping(value = "edit/history/{id}")
    public PageDTO<OrganizationChangeHistory> getEditHistory(@PathVariable("id") Long id) {
        List<OrganizationChangeHistory> organizationChangeHistoryList = organizationsService.getOrganizationEditHistoryById(id);

        List<OrganizationEditHistoryPageDTO> content = OrganizationHistoryPageDTOTransformer.toDtoFromList(organizationChangeHistoryList);
        return new PageDTO(content);
    }
}
