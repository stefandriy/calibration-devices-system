package com.softserve.edu.controller.client.application.util;

import com.softserve.edu.dto.application.ApplicationFieldDTO;
import com.softserve.edu.entity.catalogue.AbstractCatalogue;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class CatalogueDTOTransformer {
    public static List<ApplicationFieldDTO> toDto(
            Iterable<? extends AbstractCatalogue> catalogues) {
        return StreamSupport
                .stream(catalogues.spliterator(), true)
                .map(catalogue -> new ApplicationFieldDTO(
                                catalogue.getId(),
                                catalogue.getDesignation())
                )
                .collect(Collectors.toList());
    }
}
