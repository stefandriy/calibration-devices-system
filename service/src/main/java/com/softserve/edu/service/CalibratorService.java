package com.softserve.edu.service;


import com.softserve.edu.entity.Calibrator;
import com.softserve.edu.repository.CalibratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
public class CalibratorService {

    @Autowired
    private CalibratorRepository calibratorRepository;

    @Transactional (readOnly = true)
    public List<Calibrator> findByDistrict(String district) {
        return calibratorRepository.findByAddressDistrict(district);
    }

    @Transactional(readOnly = true)
    public Calibrator findById(Long id) {
        return calibratorRepository.findOne(id);
    }
}
