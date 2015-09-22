package com.softserve.edu.service.provider;

import com.softserve.edu.entity.Organization;
import com.softserve.edu.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class ProviderService {

    @Autowired
    private OrganizationRepository providerRepository;

    @Transactional(readOnly = true)
    public List<Organization> findByDistrictAndType(String district, String type) {
        return providerRepository.findByDistrictAndType(district, type);
    }

    @Transactional(readOnly = true)
    public Set<String> getTypesById(Long id) {
        return providerRepository.findOrganizationTypesById(id);
    }

    @Transactional(readOnly = true)
    public Organization findById(Long id) {
        return providerRepository.findOne(id);
    }
}
