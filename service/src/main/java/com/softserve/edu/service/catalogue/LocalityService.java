package com.softserve.edu.service.catalogue;

import com.softserve.edu.entity.catalogue.Locality;
import com.softserve.edu.repository.catalogue.LocalityRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class LocalityService {
    Logger logger = Logger.getLogger(LocalityService.class);

    @Autowired
    private LocalityRepository localityRepository;

    public List<Locality> getLocalitiesCorrespondingDistrict(Long districtId) {
        return localityRepository.findDistinctByDistrictId(districtId);
    }

    public List<String> getMailIndexForLocality(String designation, Long districtId) {
        return localityRepository.findMailIndexByDesignationAndDistrictId(designation, districtId);
    }
}
