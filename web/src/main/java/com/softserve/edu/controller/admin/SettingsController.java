package com.softserve.edu.controller.admin;

import com.softserve.edu.dto.NewPasswordDTO;
import com.softserve.edu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping(value = "/admin/settings/")
public class SettingsController {

    @Autowired
    private UserService userService;

    /**
     * Changes user's password
     *
     * @param newPasswordDTO container for new and old password
     * @param userDetails user details stored in session
     * @return 200 OK if changed, else 404 NOT FOUND
     */
    @RequestMapping(value = "password", method = RequestMethod.PUT)
    public ResponseEntity changePassword(
            @RequestBody NewPasswordDTO newPasswordDTO,
            @AuthenticationPrincipal UserDetails userDetails) {
        HttpStatus httpStatus = HttpStatus.CONFLICT;
        if (userService.changePassword(
                userDetails.getUsername(),
                newPasswordDTO.getOldPassword(),
                newPasswordDTO.getNewPassword())) {
            httpStatus = HttpStatus.OK;
        }
        return new ResponseEntity(httpStatus);
    }
}
