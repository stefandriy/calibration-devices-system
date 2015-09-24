package com.softserve.edu.service.state.verificator;

import com.softserve.edu.entity.user.User;

public interface StateVerificatorEmployeeService {

    void addEmployee(User stateVerificatorEmployee);

    User oneProviderEmployee(String username);
}
