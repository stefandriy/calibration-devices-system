package com.softserve.edu.controller.provider;


import com.softserve.edu.entity.user.User;
import com.softserve.edu.service.SecurityUserDetailsService;
import com.softserve.edu.service.provider.ProviderEmployeeService;
import com.softserve.edu.service.provider.buildGraphic.ProviderEmployeeGraphic;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;


@RestController
@RequestMapping(value = "provider/admin/users/")
public class ProviderEmployeeController {

    Logger logger = Logger.getLogger(ProviderEmployeeController.class);


    @Autowired
    private ProviderEmployeeService providerEmployeeService;


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


}
