package com.softserve.edu.controller.provider.util;

import com.softserve.edu.dto.admin.UsersPageItem;
import com.softserve.edu.entity.user.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MAX on 12.07.2015.
 */
public class ProviderEmployeePageDTOTransformer {


    public static List<UsersPageItem> toDtoFromList(List<User> list,String role) {

        List<UsersPageItem> resultList = new ArrayList<UsersPageItem>();
        for (User providerEmployee : list) {
            resultList.add(new UsersPageItem(
                            providerEmployee.getUsername(),
                            role,
                            providerEmployee.getFirstName(),
                            providerEmployee.getLastName(),
                            providerEmployee.getMiddleName(),
                            providerEmployee.getPhone(),
                            providerEmployee.getOrganization().getName(),
                            providerEmployee.getCountOfWork()

                    )
            );
        }
        return resultList;
    }
}