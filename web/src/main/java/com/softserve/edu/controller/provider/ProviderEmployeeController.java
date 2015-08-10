package com.softserve.edu.controller.provider;


import com.softserve.edu.dto.PageDTO;
import com.softserve.edu.dto.admin.UsersPageItem;
import com.softserve.edu.entity.Organization;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.service.SecurityUserDetailsService;
import com.softserve.edu.service.admin.OrganizationsService;
import com.softserve.edu.service.provider.ProviderEmployeeService;
import com.softserve.edu.service.provider.buildGraphic.ProviderEmployeeGraphic;
import com.softserve.edu.service.utils.ListToPageTransformer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequestMapping(value = "provider/admin/users/")
public class ProviderEmployeeController {

    Logger logger = Logger.getLogger(ProviderEmployeeController.class);


    @Autowired
    private ProviderEmployeeService providerEmployeeService;

    @Autowired
    private OrganizationsService organizationsService;


    @RequestMapping(value = "graphic", method = RequestMethod.GET)
    public List<ProviderEmployeeGraphic> graphic
            (@RequestParam String fromDate, @RequestParam String toDate,
             @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {
        Long idOrganization = user.getOrganizationId();
        List<ProviderEmployeeGraphic> list = null;
        try {
            Date dateFrom = providerEmployeeService.convertToDate(fromDate);
            Date dateTo = providerEmployeeService.convertToDate(toDate);
            List<User> providerEmployee= providerEmployeeService.getAllProviderEmployee(idOrganization);
            list = providerEmployeeService.buildGraphic(dateFrom, dateTo, idOrganization, providerEmployee);
        } catch (Exception e) {
            logger.error("Failed to get graphic data");
        }
        return list;
    }

    @RequestMapping(value = "graphicmainpanel", method = RequestMethod.GET)
    public List<ProviderEmployeeGraphic> graphicMainPanel
            (@RequestParam String fromDate, @RequestParam String toDate,
             @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {
        Long idOrganization = user.getOrganizationId();
        List<ProviderEmployeeGraphic> list = null;
        try {
            Date dateFrom = providerEmployeeService.convertToDate(fromDate);
            Date dateTo = providerEmployeeService.convertToDate(toDate);

            list = providerEmployeeService.buidGraphicMainPanel(dateFrom, dateTo, idOrganization);

        } catch (Exception e) {
            logger.error("Failed to get graphic data");
        }
        return list;
    }


    @RequestMapping(value = "piemainpanel", method = RequestMethod.GET)
    public Map pieMainPanel
            (@AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {
        Long idOrganization = user.getOrganizationId();
        Organization organization = organizationsService.getOrganizationById(idOrganization);
        Map tmp = new HashMap<>();
        tmp.put("notOnWork", 5);
        tmp.put("onWork",20);
        return tmp;
    }






}
