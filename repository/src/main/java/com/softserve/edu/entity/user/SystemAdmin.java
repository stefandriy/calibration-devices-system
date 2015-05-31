package com.softserve.edu.entity.user;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Root administrator.
 */
@Entity
@DiscriminatorValue(value= "sys_admin")
public class SystemAdmin extends User {

    public enum AdminRole implements Role
    {
        SYS_ADMIN;

        @Override
        public String roleName() {
            return name();
        }
    }

    public SystemAdmin(String username, String password)
    {
        super(username, password, AdminRole.SYS_ADMIN);
    }

    protected SystemAdmin() {}
}
