package com.softserve.edu.service.admin.impl;

import com.softserve.edu.entity.user.User;
import com.softserve.edu.repository.DeviceRepository;
import com.softserve.edu.repository.OrganizationRepository;
import com.softserve.edu.repository.UserRepository;
import com.softserve.edu.repository.VerificationRepository;
import com.softserve.edu.service.admin.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StatisticServiceImpl implements StatisticService {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private VerificationRepository verificationRepository;

    @Override
    public Long countOrganizations() {
        return organizationRepository.count();
    }

    @Override
    public Long countUsers() {
        return userRepository.count();
    }

    @Override
    public Long countDevices() {
        return deviceRepository.count();
    }

    @Override
    public Long countVerifications() {
        return verificationRepository.count();
    }

    @Override
    @Transactional
    public User employeeExist(String username) {
        return userRepository.findOne(username);
    }
}
