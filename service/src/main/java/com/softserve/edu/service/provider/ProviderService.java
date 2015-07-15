package com.softserve.edu.service.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.softserve.edu.entity.Organization;
import com.softserve.edu.repository.OrganizationRepository;

import java.util.List;

@Service
public class ProviderService {

    @Autowired
    private OrganizationRepository providerRepository;

//    @Transactional(readOnly = true)
//    public List<Organization> findByDistrictDesignation(String designation) {
//        return providerRepository.findByAddressDistrict(designation);
//    }

    @Transactional(readOnly = true)
    public Organization findById(Long id) {
    	System.err.println("inside provider service find by id");
        return providerRepository.findOne(id);
    }

}
