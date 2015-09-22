package com.softserve.edu.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.softserve.edu.dto.NewPasswordDTO;
import com.softserve.edu.service.UserServiceImpl;
@RestController
@RequestMapping(value = "/admin/settings/")
public class SettingsAdminController {

    @Autowired
    private UserServiceImpl userServiceImpl;

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
        if (userServiceImpl.changePassword(
                userDetails.getUsername(),
                newPasswordDTO.getOldPassword(),
                newPasswordDTO.getNewPassword())) {
            httpStatus = HttpStatus.OK;
        }
        return new ResponseEntity(httpStatus);
    }
}
