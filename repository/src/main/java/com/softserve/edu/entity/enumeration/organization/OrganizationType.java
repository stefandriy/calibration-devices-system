package com.softserve.edu.entity.enumeration.organization;

import com.softserve.edu.entity.enumeration.user.UserRole;

public enum OrganizationType {
    PROVIDER,
    CALIBRATOR,
    STATE_VERIFICATOR,
    NO_TYPE;

    public static UserRole getOrganizationAdminRole(OrganizationType type) {
        UserRole userRole;
        switch (type) {
            case PROVIDER:
                userRole = UserRole.PROVIDER_ADMIN;
                break;
            case CALIBRATOR:
            userRole = UserRole.CALIBRATOR_ADMIN;
                break;
            case STATE_VERIFICATOR:
                userRole = UserRole.STATE_VERIFICATOR_ADMIN;
                break;
            default: throw new IllegalArgumentException("No admin for organization with type : " + type);
        }
        return userRole;
    }
}
