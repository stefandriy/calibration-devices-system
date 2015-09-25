package com.softserve.edu.controller.admin.util;

import com.softserve.edu.dto.admin.OrganizationPageDTO;
import com.softserve.edu.entity.Organization;
import com.softserve.edu.entity.OrganizationType;
import com.softserve.edu.service.admin.UserService;
import com.softserve.edu.service.admin.impl.UsersServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class OrganizationPageDTOTransformer {

    public static List<OrganizationPageDTO> toDtoFromList(List<Organization> list){
        List<OrganizationPageDTO> resultList = new ArrayList<>();

        for (Organization organization : list) {

            List<String> types = new ArrayList<>();
            organization.getOrganizationTypes().
                    stream()
                    .map(OrganizationType::name)
                    .forEach(types::add);
            
            String[] arrayTypes = types.toArray(new String[types.size()]);
            String stringOrganizationTypes = String.join(",", types);

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
