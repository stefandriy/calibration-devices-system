package com.softserve.edu.service.provider;

import com.softserve.edu.entity.user.ProviderEmployee;
import com.softserve.edu.repository.ProviderEmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.softserve.edu.entity.user.ProviderEmployee.ProviderEmployeeRole.PROVIDER_EMPLOYEE;

@Service
public class ProviderEmployeeService {
    @Autowired
    private ProviderEmployeeRepository providerEmployeeRepository;

    @Transactional
    public void addEmployee(ProviderEmployee providerEmployee) {

        String passwordEncoded = new BCryptPasswordEncoder().encode(providerEmployee.getPassword());
        providerEmployee.setPassword(passwordEncoded);
        providerEmployee.setRole(PROVIDER_EMPLOYEE);
        providerEmployeeRepository.save(providerEmployee);
    }
}
