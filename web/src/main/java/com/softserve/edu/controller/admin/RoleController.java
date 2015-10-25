package com.softserve.edu.controller.admin;

import com.softserve.edu.entity.enumeration.user.UserRole;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.service.admin.UserService;
import com.softserve.edu.service.user.SecurityUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/admin/")
public class RoleController {

    @Autowired
    private UserService userService;


    @RequestMapping(value = "is_super_admin/", method = RequestMethod.GET)
    public Boolean isSuperAdmin(
            @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {

        User authorizedAdmin = userService.findOne(user.getUsername());

        return userService.getRoles(authorizedAdmin.getUsername()).contains(UserRole.SUPER_ADMIN.name());
    }
}
