package com.softserve.edu.controller.admin.util;

import com.softserve.edu.dto.admin.OrganizationEditHistoryPageDTO;
import com.softserve.edu.entity.OrganizationChangeHistory;
import com.softserve.edu.entity.OrganizationType;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vova on 23.09.15.
 */
public class OrganizationHistoryPageDTOTransformer {
    public static List<OrganizationEditHistoryPageDTO> toDtoFromList(List<OrganizationChangeHistory> list){

        List<OrganizationEditHistoryPageDTO>  resultList = new ArrayList<OrganizationEditHistoryPageDTO>();

        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

        for (OrganizationChangeHistory organization : list) {

            resultList.add(new OrganizationEditHistoryPageDTO(
                    df.format(organization.getDate()),
                    organization.getName(),
                    organization.getEmail(),
                    organization.getPhone(),
                    organization.getTypes(),
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
                    organization.getAdminName()
            ));
        }

        return resultList;
    }
}
