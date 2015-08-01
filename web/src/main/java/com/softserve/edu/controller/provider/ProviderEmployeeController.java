package com.softserve.edu.controller.provider;


import com.softserve.edu.service.SecurityUserDetailsService;
import com.softserve.edu.service.provider.ProviderEmployeeService;
import com.softserve.edu.service.provider.buildGraphic.ProviderEmployeeGrafic;

import java.text.ParseException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.Date;
import java.util.List;


import com.softserve.edu.service.SecurityUserDetailsService;
import com.softserve.edu.service.provider.ProviderEmployeeService;
import com.softserve.edu.service.utils.ProviderEmployeeGraphic;
 
 
@RestController
@RequestMapping(value = "provider/admin/users/")
public class ProviderEmployeeController {

    Logger logger = Logger.getLogger(ProviderEmployeeController.class);


    @Autowired
    private ProviderEmployeeService providerEmployeeService;


    @RequestMapping(value = "graphic", method = RequestMethod.GET)
    public List<ProviderEmployeeGrafic> graphic
            (@RequestParam String fromDate, @RequestParam String toDate,
             @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {
        Long idOrganization = user.getOrganizationId();
        List<ProviderEmployeeGrafic> list = null;
        try {
            Date dateFrom = providerEmployeeService.convertToDate(fromDate);
            Date dateTo = providerEmployeeService.convertToDate(toDate);
            list = providerEmployeeService.buidGraphic(dateFrom, dateTo, idOrganization);
        } catch (Exception e) {
            logger.error("Failed to get graphic data");
        }
        return list;
    }


}
