package com.softserve.edu.service.state.verificator;


import com.softserve.edu.entity.StateVerificator;
import com.softserve.edu.repository.StateVerificatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Service
@Transactional
public class StateVerificatorService {

        @Autowired
        private StateVerificatorRepository stateVerificatorRepository;

        @Transactional
        public void saveStateVerificator(StateVerificator cstateVerificatorlibrator) {
            stateVerificatorRepository.save(cstateVerificatorlibrator);
        }
        @Transactional(readOnly = true)
        public List<StateVerificator> findByDistrict(String district) {
            return stateVerificatorRepository.findByAddressDistrict(district);
        }
        @Transactional(readOnly = true)
        public StateVerificator findById(Long id){
        	return stateVerificatorRepository.findOne(id);
        }

}
