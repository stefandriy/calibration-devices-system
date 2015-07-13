package com.softserve.edu.service.calibrator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.softserve.edu.entity.Organization;
import com.softserve.edu.repository.OrganizationRepository;

import java.util.List;

@Service
public class CalibratorService {

    @Autowired
    private OrganizationRepository calibratorRepository;

    @Transactional (readOnly = true)
    public List<Organization> findByDistrict(String district) {
        return calibratorRepository.findByAddressDistrict(district);
    }

    @Transactional(readOnly = true)
    public Organization findById(Long id) {
        return calibratorRepository.findOne(id);
    }
}
