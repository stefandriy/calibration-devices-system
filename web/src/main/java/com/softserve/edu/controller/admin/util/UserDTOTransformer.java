package com.softserve.edu.controller.admin.util;

import com.softserve.edu.dto.admin.SysAdminDTO;
import com.softserve.edu.dto.admin.UsersPageItem;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.service.admin.UsersService;
import com.softserve.edu.service.utils.ListToPageTransformer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class UserDTOTransformer {
    /**
     * Current method transform list of sys admins to List DTO
     *
     * @param queryResult
     * @return full information about sys admins
     */
    public static List<SysAdminDTO> toDTOFromListSysAdmin(ListToPageTransformer<User> queryResult) {
        List<SysAdminDTO> resultList = new ArrayList<>();
        for (User sysAdmin : queryResult.getContent()) {

            resultList.add(new SysAdminDTO(
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
                            sysAdmin.getAddress().getFlat())
            );
        }
        return resultList;
    }


    public static List<UsersPageItem> toDTOFromListEmployee(ListToPageTransformer<User> queryResult, UsersService usersService) {
        List<UsersPageItem> resultList = new ArrayList<>();
        for (User employee : queryResult.getContent()) {

            List<String> userRoles = usersService.getRoles(employee.getUsername())
                    .stream()
                    .distinct()
                    .collect(Collectors.toList());

            resultList.add(new UsersPageItem(
                            employee.getUsername(),
                            userRoles,
                            employee.getFirstName(),
                            employee.getLastName(),
                            employee.getMiddleName(),
                            employee.getPhone(),
                            employee.getSecondPhone(),
                            employee.getOrganization().getName(),
                            null, null, null,
                            employee.getIsAvailable())
            );
        }
        return resultList;
    }
}
