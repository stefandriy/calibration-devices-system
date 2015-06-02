package com.softserve.edu.service.state.verificator;


import com.softserve.edu.entity.Calibrator;
import com.softserve.edu.entity.StateVerificator;
import com.softserve.edu.repository.StateVerificatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;


@Service
@Transactional
public class StateVerificatorService {

        @Autowired
        private StateVerificatorRepository stateVerificatorRepository;

        public void saveStateVerificator(StateVerificator cstateVerificatorlibrator) {
            stateVerificatorRepository.save(cstateVerificatorlibrator);
        }

        public List<StateVerificator> findByDistrict(String district) {
            return stateVerificatorRepository.findByAddressDistrict(district);
        }


}
