package com.softserve.edu.controller.provider;

import com.softserve.edu.entity.user.ProviderEmployee;
import com.softserve.edu.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "provider/admin/users/")
public class ProviderUserController {

    Logger logger = Logger.getLogger(ProviderUserController.class);

    @Autowired
    private UserService userService;

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

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public ResponseEntity<HttpStatus> addEmployee(@RequestBody ProviderEmployee providerEmployee) {
        logger.info(providerEmployee);
        return null;
    }
}
