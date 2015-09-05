package com.softserve.edu.controller.admin.util;

import com.softserve.edu.dto.admin.OrganizationPageDTO;
import com.softserve.edu.entity.Organization;
import com.softserve.edu.entity.OrganizationType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by vova on 03.09.15.
 */
public class OrganizationPageDTOTransformer {
    public static List<OrganizationPageDTO> toDtoFromList(List<Organization> list){
        List<OrganizationPageDTO> resultList = new ArrayList<OrganizationPageDTO>();
        for (Organization organization : list) {
            Set<OrganizationType> organizationTypes = organization.getOrganizationTypes();
            List<String> listOrganizationTypes = new ArrayList<String>();
            organizationTypes.forEach(organizationType -> listOrganizationTypes.add(organizationType.getType()));
            String[] arrayOrganizationTypes = new String[listOrganizationTypes.size()];
            arrayOrganizationTypes = listOrganizationTypes.toArray(arrayOrganizationTypes);

            resultList.add(new OrganizationPageDTO(
                            organization.getId(),
                            organization.getName(),
                    organization.getEmail(),
                    arrayOrganizationTypes,
                    organization.getPhone(),
                    organization.getAddress().getRegion(),
                    organization.getAddress().getLocality(),
                    organization.getAddress().getDistrict(),
                    organization.getAddress().getStreet()
            ));
        }
        return resultList;
    }

}
