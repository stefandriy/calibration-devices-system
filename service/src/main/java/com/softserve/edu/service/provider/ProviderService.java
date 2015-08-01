package com.softserve.edu.service.provider;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.softserve.edu.entity.Organization;
import com.softserve.edu.repository.OrganizationRepository;

@Service
public class ProviderService {

    @Autowired
    private OrganizationRepository providerRepository;
   
    @Transactional (readOnly = true)
    public List<Organization> findByDistrict(String district, String type) {
    	  return providerRepository.getByTypeAndDistrict(district, type);
    }
   /* @Transactional (readOnly = true)
    public List<Organization> findByDistrictId(Long id, String type) {
    	  return providerRepository.getByTypeAndDistrictId(id, type);
    }*/
    
    @Transactional(readOnly = true)
    public Set<String> getTypesById(Long id) {
    	return providerRepository.getOrganizationTypesById(id);
    }
    
    @Transactional(readOnly = true)
    public Organization findById(Long id) {
    	System.err.println("inside provider service find by id");
        return providerRepository.findOne(id);
    }

}
