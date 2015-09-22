package com.softserve.edu.service.admin;

import com.softserve.edu.entity.user.User;

public interface StatisticService {

    Long countOrganizations();

    Long countUsers();

    Long countDevices();

    Long countVerifications();

    User employeeExist(String username);

}
