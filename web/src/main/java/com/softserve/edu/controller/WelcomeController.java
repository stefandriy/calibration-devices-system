package com.softserve.edu.controller;

import com.softserve.edu.dto.admin.UsersPageItem;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.service.user.SecurityUserDetailsService;
import com.softserve.edu.service.admin.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class WelcomeController {

    @Autowired
    SecurityUserDetailsService securityUserDetailsService;

    @Autowired
    private StatisticService statisticService;

    /**
     * Responds details about current principal.
     *
     * @param principal - a user who made a request
     * @return "guest" if not authenticated or UserDetails for authenticated user.
     */
    @RequestMapping(value = "/getuser", method = RequestMethod.GET)
    public Object getUser(Principal principal) {
        return principal == null ? null : securityUserDetailsService.loadUserByUsername(principal.getName());
    }

    @RequestMapping(value = "/loginuser", method = RequestMethod.GET)
    public UsersPageItem getEmployee(@AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails userDetails) {
        UsersPageItem usersPageItem = new UsersPageItem();
        User user =  statisticService.employeeExist(userDetails.getUsername());
        usersPageItem.setFirstName(user.getFirstName());
        usersPageItem.setLastName(user.getLastName());
        usersPageItem.setUsername(user.getUsername());
        usersPageItem.setMiddleName(user.getMiddleName());
        usersPageItem.setEmail(user.getEmail());
        usersPageItem.setPhone(user.getPhone());
        usersPageItem.setSecondPhone(user.getSecondPhone());
        return  usersPageItem;
    }
}

