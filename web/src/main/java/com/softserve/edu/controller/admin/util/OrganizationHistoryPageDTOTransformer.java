package com.softserve.edu.controller.admin.util;

import com.softserve.edu.dto.admin.OrganizationEditHistoryPageDTO;
import com.softserve.edu.entity.OrganizationChangeHistory;
import com.softserve.edu.entity.OrganizationType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vova on 23.09.15.
 */
public class OrganizationHistoryPageDTOTransformer {
    public static List<OrganizationEditHistoryPageDTO> toDtoFromList(List<OrganizationChangeHistory> list){
        List<OrganizationEditHistoryPageDTO>  resultList = new ArrayList<OrganizationEditHistoryPageDTO>();
        for (OrganizationChangeHistory organization : list) {

            List<String> types = new ArrayList<>();
            organization.getOrganization().getOrganizationTypes().
                    stream()
                    .map(OrganizationType::name)
                    .forEach(types::add);

            String stringOrganizationTypes = String.join(",", types);

            resultList.add(new OrganizationEditHistoryPageDTO(
                    organization.getOrganizationChangeHistoryPK().getDate(),
                    organization.getName(),
                    organization.getEmail(),
                    organization.getPhone(),
                    stringOrganizationTypes ,
                    organization.getEmployeesCapacity(),
                    organization.getMaxProcessTime(),
                    organization.getAddress().getRegion(),
                    organization.getAddress().getDistrict(),
                    organization.getAddress().getLocality(),
                    organization.getAddress().getStreet(),
                    organization.getAddress().getBuilding(),
                    organization.getAddress().getFlat(),
                    organization.getUsername(),
                    organization.getFirstName(),
                    organization.getLastName(),
                    organization.getMiddleName(),
                    organization.getadminName()
            ));
        }
        return resultList;
    }
}
