package com.softserve.edu.service;

import com.softserve.edu.entity.Address;
import com.softserve.edu.entity.Calibrator;
import com.softserve.edu.entity.Provider;
import com.softserve.edu.repository.CalibratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;

@Service

public class CalibratorService {

    @Autowired
    private CalibratorRepository calibratorRepository;

    @Transactional
    public void saveCalibrator(Calibrator calibrator) {
       // Address address = calibrator.getAddress();

        //Assert.isNull(address.getIndex(), "calibrator's index can't be null");
       // Assert.notNull(address.getFlat(), "calibrator can't have flat in address");
        calibratorRepository.save(calibrator);
    }
    @Transactional
    public List<Calibrator> findByDistrict(String district) {
        return calibratorRepository.findByAddressDistrict(district);
    }

    @Transactional(readOnly = true)
    public Calibrator findById(Long id) {
        return calibratorRepository.findOne(id);
    }
}
