package com.softserve.edu.controller.admin.util;

import com.softserve.edu.dto.admin.OrganizationPageDTO;
import com.softserve.edu.entity.Organization;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vova on 03.09.15.
 */
public class OrganizationPageDTOTransformer {
    public static List<OrganizationPageDTO> toDtoFromList(List<Organization> list){
        List<OrganizationPageDTO> resultList = new ArrayList<OrganizationPageDTO>();
        for (Organization organization : list) {
            resultList.add(new OrganizationPageDTO(
                            organization.getId(),
                            organization.getName(),
                            organization.getEmail(),
                            organization.getPhone()
            ));
        }
        return resultList;
    }

}
