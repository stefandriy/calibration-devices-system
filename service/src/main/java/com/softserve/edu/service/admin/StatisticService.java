package com.softserve.edu.service.admin;

import com.softserve.edu.entity.user.User;

public interface StatisticService {

    Long countOrganizations();

    Long countUsers();

    Long countDevices();

    Long countCounterTypes();

    Long countVerifications();

    Long countSysAdmins();

    User employeeExist(String username);

}
