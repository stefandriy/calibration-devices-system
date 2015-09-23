package com.softserve.edu.service.provider;

import com.softserve.edu.entity.Organization;

import java.util.List;
import java.util.Set;

public interface ProviderService {
     List<Organization> findByDistrictAndType(String district, String type);

     Set<String> getTypesById(Long id);

     Organization findById(Long id);
}
