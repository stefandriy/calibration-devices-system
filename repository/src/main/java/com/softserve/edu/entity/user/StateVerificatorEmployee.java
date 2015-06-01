package com.softserve.edu.entity.user;

import com.softserve.edu.entity.Organization;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "STATE_VERIFICATOR_EMPLOYEE")
public class StateVerificatorEmployee extends Employee {

    public enum StateVerificatorEmployeeRole implements Role {
        STATE_VERIFICATOR_EMPLOYEE, STATE_VERIFICATOR_ADMIN;

        @Override
        public String roleName() {
            return name();
        }
    }

    protected StateVerificatorEmployee() {}

    public StateVerificatorEmployee(
            String username, String password, StateVerificatorEmployeeRole role,
            Organization organization) {
        super(username, password, role, organization);
    }

    public StateVerificatorEmployee(
            String username, String password, StateVerificatorEmployeeRole role,
            Organization organization, String firstName, String lastName,
            String email, String phone) {

        super(username, password, role, organization, firstName, lastName, email, phone);
    }

    @Override
    public void setRole(Role role) {
        if (!(role instanceof StateVerificatorEmployeeRole)) {
            throw new IllegalArgumentException("Role " + role.roleName() +
                    " not supported for StateVerificatorEmployee");
        }
        super.setRole(role);
    }
}
