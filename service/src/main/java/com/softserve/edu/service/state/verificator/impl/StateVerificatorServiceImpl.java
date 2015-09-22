package com.softserve.edu.service.state.verificator.impl;


import com.softserve.edu.entity.Organization;
import com.softserve.edu.repository.OrganizationRepository;
import com.softserve.edu.service.state.verificator.StateVerificatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class StateVerificatorServiceImpl implements StateVerificatorService {

    @Autowired
    private OrganizationRepository stateVerificatorRepository;

    @Transactional
    public void saveStateVerificator(Organization stateVerificator) {
        stateVerificatorRepository.save(stateVerificator);
    }

    @Transactional(readOnly = true)
    public List<Organization> findByDistrictAndType(String district, String type) {
        return stateVerificatorRepository.findByDistrictAndType(district, type);
    }

    @Transactional(readOnly = true)
    public Organization findById(Long id) {
        return stateVerificatorRepository.findOne(id);
    }
}
