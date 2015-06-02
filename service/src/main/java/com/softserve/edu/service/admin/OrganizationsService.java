package com.softserve.edu.service.admin;

import com.softserve.edu.entity.*;
import com.softserve.edu.entity.user.CalibratorEmployee;
import com.softserve.edu.entity.user.Employee;
import com.softserve.edu.entity.user.ProviderEmployee;
import com.softserve.edu.entity.user.StateVerificatorEmployee;
import com.softserve.edu.repository.OrganizationRepository;
import com.softserve.edu.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

import static com.softserve.edu.entity.user.CalibratorEmployee.CalibratorEmployeeRole
        .CALIBRATOR_ADMIN;
import static com.softserve.edu.entity.user.ProviderEmployee.ProviderEmployeeRole.PROVIDER_ADMIN;
import static com.softserve.edu.entity.user.StateVerificatorEmployee.StateVerificatorEmployeeRole
        .STATE_VERIFICATOR_ADMIN;

@Service

public class OrganizationsService {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void addOrganizationWithAdmin(String name, String email, String phone, String type,
                                         String username, String password, Address address) {
        String passwordEncoded = new BCryptPasswordEncoder().encode(password);
        Organization organization;
        Employee employeeAdmin;
        switch (type) {
            case "PROVIDER":
                organization = new Provider(name, email, phone);
                employeeAdmin = new ProviderEmployee(username, passwordEncoded, PROVIDER_ADMIN,
                        organization);
                break;
            case "CALIBRATOR":
                organization = new Calibrator(name, email, phone);
                employeeAdmin = new CalibratorEmployee(username, passwordEncoded,
                        CALIBRATOR_ADMIN, organization);
                break;
            case "STATE_VERIFICATION":
                organization = new StateVerificator(name, email, phone);
                employeeAdmin = new StateVerificatorEmployee(username, passwordEncoded,
                        STATE_VERIFICATOR_ADMIN, organization);
                break;
            default:
                throw new NoSuchElementException();
        }
        organization.setAddress(address);
        organizationRepository.save(organization);
        userRepository.save(employeeAdmin);
    }

    @Transactional
    public Page<Organization> getOrganizationsBySearchAndPagination(int pageNumber, int
            itemsPerPage, String search) {
        /* pagination starts from 1 at client side, but Spring Data JPA from 0 */
        PageRequest pageRequest = new PageRequest(pageNumber - 1, itemsPerPage);
        return search == null ?
                organizationRepository.findAll(pageRequest) :
                organizationRepository.findByNameLikeIgnoreCase("%" + search + "%", pageRequest);
    }

    @Transactional(readOnly = true)
    public Organization findById(Long id) {
        return organizationRepository.findOne(id);
    }

    public String getType(Organization organization) {
        if (organization instanceof Provider) { return "PROVIDER"; }
        if (organization instanceof Calibrator) { return "CALIBRATOR"; }
        if (organization instanceof StateVerificator) { return "STATE_VERIFICATION"; }
        return "NO_TYPE";
    }
}
