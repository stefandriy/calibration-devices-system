package com.softserve.edu.service.state.verificator.impl;


import com.softserve.edu.entity.enumeration.user.UserRole;
import com.softserve.edu.entity.organization.Organization;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.verification.Verification;
import com.softserve.edu.repository.OrganizationRepository;
import com.softserve.edu.repository.UserRepository;
import com.softserve.edu.repository.VerificationRepository;
import com.softserve.edu.service.state.verificator.StateVerificatorService;
import com.softserve.edu.service.utils.EmployeeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
public class StateVerificatorServiceImpl implements StateVerificatorService {

    @Autowired
    private OrganizationRepository stateVerificatorRepository;

    @Autowired
    private VerificationRepository verificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public void saveStateVerificator(Organization stateVerificator) {
        stateVerificatorRepository.save(stateVerificator);
    }

    @Override
    @Transactional(readOnly = true)
    public Organization findById(Long id) {
        return stateVerificatorRepository.findOne(id);
    }


    /**
     * return all Employees from Verificators(User) organization
     * @param userRoles
     * @param employee
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDTO> getAllVerificatorEmployee(List<String> userRoles, User employee) {

        List<EmployeeDTO> verificatorEmployeeList = new ArrayList<>();
        if (userRoles.contains(UserRole.STATE_VERIFICATOR_ADMIN.name())) {
            List<User> allAvailableUsersList = userRepository
                    .findAllAvailableUsersByRoleAndOrganizationId(UserRole.STATE_VERIFICATOR_EMPLOYEE,
                            employee.getOrganization().getId())
                    .stream()
                    .collect(Collectors.toList());

            verificatorEmployeeList = EmployeeDTO.giveListOfEmployeeDTOs(allAvailableUsersList);
        } else {
            EmployeeDTO userPage = new EmployeeDTO(employee.getUsername(), employee.getFirstName(),
                    employee.getLastName(), employee.getMiddleName(), userRoles.get(0));
            verificatorEmployeeList.add(userPage);
        }
        return verificatorEmployeeList;
    }

    /**
     * assigning employee on the verification
     * and than this employee will work with this verification
     * @param verificationId
     * @param verificatorEmployee
     */
    @Override
    @Transactional
    public void assignVerificatorEmployee(String verificationId, User verificatorEmployee) {
        Verification verification = verificationRepository.findOne(verificationId);
        verification.setStateVerificatorEmployee(verificatorEmployee);
        verification.setReadStatus(Verification.ReadStatus.READ);
        verificationRepository.save(verification);
    }
}
