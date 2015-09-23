package com.softserve.edu.service.catalogue.impl;

import com.softserve.edu.entity.catalogue.StreetType;
import com.softserve.edu.repository.catalogue.StreetTypeRepository;
import com.softserve.edu.service.catalogue.StreetTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class StreetTypeServiceImpl implements StreetTypeService {

    @Autowired
    private StreetTypeRepository streetTypeRepository;

    @Override
    public List<StreetType> getStreetsTypes() {
        return streetTypeRepository.findAll();
    }
}
