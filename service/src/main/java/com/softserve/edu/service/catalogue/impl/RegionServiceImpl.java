package com.softserve.edu.service.catalogue.impl;

import com.softserve.edu.service.catalogue.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.softserve.edu.entity.catalogue.Region;
import com.softserve.edu.repository.catalogue.RegionRepository;

@Service
@Transactional(readOnly = true)
public class RegionServiceImpl implements RegionService{

    @Autowired
    private RegionRepository regionRepository;

    public Iterable<Region> getAll() {
        return regionRepository.findAll();
    }

    public Region getRegionByDesignation(String designation) {
        return regionRepository.findByDesignation(designation);
    }
}
