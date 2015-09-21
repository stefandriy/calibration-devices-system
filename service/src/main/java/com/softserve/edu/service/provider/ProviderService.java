package com.softserve.edu.service.provider;

import com.softserve.edu.entity.Organization;
import com.softserve.edu.entity.OrganizationType;
import com.softserve.edu.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProviderService {

    @Autowired
    private OrganizationRepository providerRepository;

    @Transactional(readOnly = true)
    public List<Organization> findByTypeAndDistrict(String district, String type) {
        return providerRepository.findByTypeAndDistrict(district, type);
    }

    @Transactional(readOnly = true)
    public Set<String> getTypesById(Long id) {

        return providerRepository.findOne(id).getOrganizationTypes()
                .stream()
                .map(OrganizationType::name)
                .collect(Collectors.toSet());
    }

    @Transactional(readOnly = true)
    public Organization findById(Long id) {
        return providerRepository.findOne(id);
    }
}
