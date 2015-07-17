package com.softserve.edu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Routing between pages
 */
@Controller
public class Router {

    /**
     * Displays welcome page
     *
     * @return welcome page
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String welcome() {
        return "/resources/app/welcome/index.html";
    }

    /**
     * Displays admin page
     *
     * @return admin page
     */
    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String admin() {
        return "/resources/app/admin/index.html";
    }

    /**
     * Displays provider page
     *
     * @return provider page
     */
    @RequestMapping(value = "/employee", method = RequestMethod.GET)
    public String provider() {
        return "/resources/app/index.jsp";
    }

}
