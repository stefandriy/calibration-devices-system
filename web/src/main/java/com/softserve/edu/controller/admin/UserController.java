package com.softserve.edu.controller.admin;

import com.softserve.edu.dto.PageDTO;
import com.softserve.edu.dto.admin.UsersPageItem;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.service.admin.UsersService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/admin/users/")
public class UserController {

    @Autowired
    private UsersService userService;

    Logger logger = Logger.getLogger(UserController.class);

    /**
     * Check whereas {@code username} is available,
     * i.e. it is possible to create new user with this {@code username}
     *
     * @param username username
     * @return {@literal true} if {@code username} available or else {@literal false}
     */
    @RequestMapping(value = "available/{username}", method = RequestMethod.GET)
    public Boolean isValidUsername(@PathVariable String username) {
        boolean isAvailable = false;
        if (username != null) {
            isAvailable = userService.existsWithUsername(username);
        }
        return isAvailable;
    }

    /**
     * Take data from pageSearchUsers in first request and then
     * if somebody doing search this method will invoke instead
     * pageSearchUsers
     *
     * @param pageNumber
     * @param itemsPerPage
     * @param search
     * @return PageDTO with employees
     */
    @RequestMapping(value = "{pageNumber}/{itemsPerPage}/{search}", method = RequestMethod.GET)
    public PageDTO<UsersPageItem> pageSearchUsers(
            @PathVariable Integer pageNumber,
            @PathVariable Integer itemsPerPage,
            @PathVariable String search) {

        Page<UsersPageItem> page = userService
                .getUsersBySearchAndPagination(pageNumber, itemsPerPage, search)
                .map(
                        new Converter<User, UsersPageItem>() {
                            @Override
                            public UsersPageItem convert(User user) {
                                UsersPageItem usPage = null;

                                if (user instanceof User) {
                                    usPage = new UsersPageItem();
                                    usPage.setUsername(user.getUsername());
                                 //   usPage.setRole(userService.getUsersRole(user));

                                    usPage.setRole(userService.getRoleByUserName(user.getUsername()));
                                    usPage.setFirstName(((User) user).getFirstName());
                                    usPage.setLastName(((User) user).getLastName());
                                    if (usPage.getRole().equalsIgnoreCase("SYS_ADMIN")) {
                                    } else {
                                        usPage.setOrganization(user.getOrganization().getName());
                                    }
                                    usPage.setPhone(((User) user).getPhone());
                                } /*else if (user instanceof SystemAdmin) {
                                     usPage = new UsersPageItem();
                                    usPage.setUsername(user.getUsername());
                                    usPage.setRole(user.getRole());
                                }*/
                                return usPage;
                            }
                        }
                );

        return new PageDTO<>(page.getTotalElements(), page.getContent());
    }

    /**
     * This method take data from web and invoke pageSearchUsers
     *
     * @param pageNumber
     * @param itemsPerPage
     * @return pageSearchUsers
     */
    @RequestMapping(value = "{pageNumber}/{itemsPerPage}", method = RequestMethod.GET)
    public PageDTO<UsersPageItem> getUsersPage(@PathVariable Integer pageNumber,
                                               @PathVariable Integer itemsPerPage) {
        return pageSearchUsers(pageNumber, itemsPerPage, null);
    }

}
