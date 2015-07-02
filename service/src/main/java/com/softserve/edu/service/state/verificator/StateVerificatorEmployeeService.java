package com.softserve.edu.service.state.verificator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.softserve.edu.entity.user.StateVerificatorEmployee;
import com.softserve.edu.repository.StateVerificatorEmployeeRepository;

import static com.softserve.edu.entity.user.StateVerificatorEmployee.StateVerificatorEmployeeRole.STATE_VERIFICATOR_EMPLOYEE; 

@Service
public class StateVerificatorEmployeeService {

	@Autowired
	private StateVerificatorEmployeeRepository stateVerificatorEmployeeRepository;
	
	 /**
     * Adds Employee for state_verificator. Saves encoded password and
     * gives role STATE_VERIFICATOR_EMPLOYEE for user
     *
     * @param state_verificator_Employee data for creation employee
     *      */
	@Transactional
	public void addEmployee(StateVerificatorEmployee stateVerificatorEmployee){
		
		String passwordEncoded = new BCryptPasswordEncoder().encode(stateVerificatorEmployee.getPassword());
		stateVerificatorEmployee.setPassword(passwordEncoded);
		stateVerificatorEmployee.setRole(STATE_VERIFICATOR_EMPLOYEE);
		stateVerificatorEmployeeRepository.save(stateVerificatorEmployee);
	}
}
