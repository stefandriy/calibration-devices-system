package com.softserve.edu.controller.admin.util;

import com.softserve.edu.dto.admin.OrganizationPageDTO;
import com.softserve.edu.entity.Organization;
import com.softserve.edu.entity.OrganizationType;
import com.softserve.edu.service.admin.UsersService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class OrganizationPageDTOTransformer {

    @Autowired
    private static UsersService userService;

    public static List<OrganizationPageDTO> toDtoFromList(List<Organization> list) {
        List<OrganizationPageDTO> resultList = new ArrayList<>();

        for (Organization organization : list) {

            Set<OrganizationType> organizationTypes = organization.getOrganizationTypes();
            List<String> listOrganizationTypes = new ArrayList<>();
            String[] arrayTypes = listOrganizationTypes.toArray(new String[listOrganizationTypes.size()]);
            organizationTypes.forEach(organizationType -> listOrganizationTypes.add(organizationType.name()));
            String stringOrganizationTypes = String.join(",", listOrganizationTypes);

            resultList.add(new OrganizationPageDTO(
                    organization.getId(),
                    organization.getName(),
                    organization.getEmail(),
                    stringOrganizationTypes,

                    organization.getPhone(),
                    organization.getAddress().getRegion(),
                    organization.getAddress().getLocality(),
                    organization.getAddress().getDistrict(),
                    organization.getAddress().getStreet(),
                    arrayTypes
            ));
        }
        return resultList;
    }

}
