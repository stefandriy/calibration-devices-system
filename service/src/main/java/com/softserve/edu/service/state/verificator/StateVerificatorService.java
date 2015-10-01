package com.softserve.edu.service.state.verificator;

import com.softserve.edu.entity.organization.Organization;

import java.util.List;

public interface StateVerificatorService {
    void saveStateVerificator(Organization stateVerificator);

    List<Organization> findByDistrictAndType(String district, String type);

    Organization findById(Long id);
}

