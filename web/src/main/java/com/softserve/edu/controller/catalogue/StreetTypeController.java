package com.softserve.edu.controller.catalogue;

import com.softserve.edu.dto.application.ApplicationFieldDTO;
import com.softserve.edu.controller.client.application.util.CatalogueDTOTransformer;
import com.softserve.edu.service.catalogue.StreetTypeService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StreetTypeController {

    @Autowired
    private StreetTypeService streetTypeService;

    @RequestMapping(value = "application/streetsTypes", method = RequestMethod.GET)
    public List<ApplicationFieldDTO> getStreetsTypes() {
        return CatalogueDTOTransformer.toDto(streetTypeService.getStreetsTypes());
    }
}
