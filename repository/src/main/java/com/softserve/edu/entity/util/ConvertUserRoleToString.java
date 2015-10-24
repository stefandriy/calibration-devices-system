package com.softserve.edu.entity.util;

import com.softserve.edu.entity.enumeration.user.UserRole;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Convert List<Sting> to Set<UserRole>
 *     and the another side.
 *     Use for conversion data, that we got from database
 *     using hql query
 */
//TODO Lets rename the class and make it generic
public class ConvertUserRoleToString {

    /**
     * Convert to List<String> from Set<UserRole>
     * @param userRoles
     * @return
     */
    public static List<String> convertToListString(Set<UserRole> userRoles) {
        List<String> userRolesString = userRoles
                .stream()
                .map(UserRole::toString)
                .collect(Collectors.toList());
        return userRolesString;
    }

    /**
     * Convert to Set<UserRole> from List<String>
     * @param userRolesStringList
     * @return
     * @throws IllegalArgumentException
     */
    public static Set<UserRole> convertToSetUserRole(List<String> userRolesStringList) throws IllegalArgumentException {
        Set<UserRole> userRoles = userRolesStringList
                .stream()
                .map(UserRole::valueOf)
                .collect(Collectors.toSet());
        return userRoles;
    }
}
