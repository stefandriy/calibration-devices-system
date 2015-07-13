package com.softserve.edu.service.state.verificator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.softserve.edu.entity.user.Employee;
import com.softserve.edu.repository.UserRepository;

@Service
public class StateVerificatorEmployeeService {

	@Autowired
	private UserRepository stateVerificatorEmployeeRepository;
	
	 /**
     * Adds Employee for state_verificator. Saves encoded password and
     * gives role STATE_VERIFICATOR_EMPLOYEE for user
     *
     * @param state_verificator_Employee data for creation employee
     *      */
	@Transactional
	public void addEmployee(Employee stateVerificatorEmployee){
		
		String passwordEncoded = new BCryptPasswordEncoder().encode(stateVerificatorEmployee.getPassword());
		stateVerificatorEmployee.setPassword(passwordEncoded);
//		stateVerificatorEmployee.setRole(STATE_VERIFICATOR_EMPLOYEE);
		stateVerificatorEmployeeRepository.save(stateVerificatorEmployee);
	}
	
//    @Transactional()
//    public Employee findByUserame(String userName){
//    	return stateVerificatorEmployeeRepository.findByUsername(userName);
//    }
}
