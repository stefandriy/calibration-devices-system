package com.softserve.edu.controller.admin;


import com.softserve.edu.controller.admin.util.UserDTOTransformer;
import com.softserve.edu.dto.user.UserDTO;
import com.softserve.edu.dto.PageDTO;
import com.softserve.edu.dto.admin.SysAdminDTO;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.service.admin.UsersService;
import com.softserve.edu.service.tool.MailService;
import com.softserve.edu.service.user.SecurityUserDetailsService;
import com.softserve.edu.service.utils.ListToPageTransformer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/admin/sysadmins/")
public class SysAdminController {

    @Autowired
    private UsersService usersService;


    @Autowired
    private MailService mail;

    Logger logger = Logger.getLogger(SysAdminController.class);


    /**
     * Fetch required data about all sys admins
     *
     * @param user
     * @return PageDTO that contains list with required data about all sys admins for
     * sys_admins table
     */
    @RequestMapping(value = "/get_sys_admins", method = RequestMethod.GET)
    public PageDTO<SysAdminDTO> getPaginationUsers(@AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {

        ListToPageTransformer<User> queryResult = usersService.findAllSysAdmins();
        List<SysAdminDTO> resultList = UserDTOTransformer.toDTOFromListSysAdmin(queryResult);
        return new PageDTO<>(queryResult.getTotalItems(), resultList);
    }


    /**
     * Add new sys admin
     *
     * @param sysAdmin - required data for creating current sys admin
     * @return status
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public ResponseEntity<HttpStatus> addSysAdmin(
            @RequestBody UserDTO sysAdmin) {
        HttpStatus httpStatus = HttpStatus.CREATED;

        try {
            usersService.addSysAdmin(sysAdmin.getUsername(), sysAdmin.getFirstName(), sysAdmin.getLastName(), sysAdmin.getMiddleName(), sysAdmin.getPhone(),
                    sysAdmin.getEmail(), sysAdmin.getAddress());
        } catch (Exception e) {
            // TODO
            logger.error("GOT EXCEPTION ", e);
            httpStatus = HttpStatus.CONFLICT;
        }


        return new ResponseEntity<>(httpStatus);
    }

    /**
     * Fetch required fields sys_admin with username @param username
     *
     * @param username
     * @return SysAdminDTO related on sys_admin with current username
     */
    @RequestMapping(value = "get_sys_admin/{username}", method = RequestMethod.GET)
    public SysAdminDTO findSysAdminByUsername(@PathVariable("username") String username) {
        User sysAdmin = usersService.findOne(username);
        SysAdminDTO SysAdminDTO = new SysAdminDTO(
                sysAdmin.getUsername(),
                sysAdmin.getFirstName(),
                sysAdmin.getLastName(),
                sysAdmin.getMiddleName(),
                sysAdmin.getEmail(),
                sysAdmin.getPhone(),
                sysAdmin.getAddress().getRegion(),
                sysAdmin.getAddress().getDistrict(),
                sysAdmin.getAddress().getLocality(),
                sysAdmin.getAddress().getStreet(),
                sysAdmin.getAddress().getBuilding(),
                sysAdmin.getAddress().getFlat()
        );
        return SysAdminDTO;
    }

    /**
     *
     * Delete sys admin with current username
     *
     * @param username
     * @return a response body with http status {@literal CREATED} if sys admin
     * have been successfully edited or else http
     * status {@literal CONFLICT}
     */
    @RequestMapping(value = "delete/{username}", method = RequestMethod.DELETE)
    public ResponseEntity<HttpStatus> deleteSysAdmin(
            @PathVariable String username) {
        HttpStatus httpStatus = HttpStatus.OK;

        try {
            usersService.deleteSysAdmin(username);
        } catch (Exception e) {
            // TODO
            logger.error("GOT EXCEPTION ", e);
            httpStatus = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<>(httpStatus);
    }

    /**
     *
     * Edit sys admin with current username
     *
     * @return a response body with http status {@literal CREATED} if sys admin
     * have been successfully created or else http
     * status {@literal CONFLICT}
     */
    @RequestMapping(value = "edit/{username}", method = RequestMethod.POST)
    public ResponseEntity<HttpStatus> editSysAdmin(
            @RequestBody UserDTO sysAdmin,
            @PathVariable String username) {
        HttpStatus httpStatus = HttpStatus.OK;

        try {
            usersService.editSysAdmin(username, sysAdmin.getPassword(), sysAdmin.getFirstName(), sysAdmin.getLastName(), sysAdmin.getMiddleName(), sysAdmin.getPhone(),
                    sysAdmin.getEmail(), sysAdmin.getAddress());
        } catch (Exception e) {
            // TODO
            logger.error("GOT EXCEPTION ", e);
            httpStatus = HttpStatus.CONFLICT;
        }
        return new ResponseEntity<>(httpStatus);
    }
}
