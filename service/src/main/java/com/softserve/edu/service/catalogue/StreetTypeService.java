package com.softserve.edu.service.catalogue;

import com.softserve.edu.entity.catalogue.StreetType;
import com.softserve.edu.repository.catalogue.StreetTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class StreetTypeService {

    @Autowired
    private StreetTypeRepository streetTypeRepository;

    public List<StreetType> getStreetsTypes() {
        return streetTypeRepository.findAll();
    }
}
