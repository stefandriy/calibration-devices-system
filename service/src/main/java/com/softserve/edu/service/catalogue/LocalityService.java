package com.softserve.edu.service.catalogue;

import com.softserve.edu.entity.catalogue.Locality;

import java.util.List;

public interface LocalityService {

    List<Locality> getLocalitiesCorrespondingDistrict(Long districtId);

    List<String> getMailIndexForLocality(String designation, Long districtId);

}