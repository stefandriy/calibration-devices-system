package com.softserve.edu.service.utils;

import com.softserve.edu.entity.Organization;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

/**
 * Created by vova on 02.09.15.
 */
public enum SortCriteriaOrganization {
 /*   private String id;
    private String name_admin;
    private String type_admin;
    private String email;
    private String phone_number;*/
    ID() {
        public Order getSortOrder(Root<Organization> root, CriteriaBuilder cb, String sortOrder) {

            if(sortOrder.equalsIgnoreCase("asc")) {
                return cb.asc(root.get("id"));
            } else {
                return cb.desc(root.get("id"));
            }
        }
    },
    NAME_ADMIN() {
        public Order getSortOrder(Root<Organization> root, CriteriaBuilder cb, String sortOrder) {

            if(sortOrder.equalsIgnoreCase("asc")) {
                return cb.asc(root.get("name"));
            } else {
                return cb.desc(root.get("name"));
            }
        }
    },
    EMAIL() {
        public Order getSortOrder(Root<Organization> root, CriteriaBuilder cb, String sortOrder) {

            if(sortOrder.equalsIgnoreCase("asc")) {
                return cb.asc(root.get("email"));
            } else {
                return cb.desc(root.get("email"));
            }
        }
    },
  /*  TYPE_ADMIN() {
        public Order getSortOrder(Root<Organization> root, CriteriaBuilder cb, String sortOrder) {

            if (sortOrder.equalsIgnoreCase("asc")) {
                return cb.asc(root.join("").get("type"));
            } else {
                return cb.desc(root.join("").get("type"));
            }
        }
    },*/
    PHONE_NUMBER() {
        public Order getSortOrder(Root<Organization> root, CriteriaBuilder cb, String sortOrder) {

            if(sortOrder.equalsIgnoreCase("asc")) {
                return cb.asc(root.get("phone"));
            } else {
                return cb.desc(root.get("phone"));
            }
        }
    };

    public Order getSortOrder(Root<Organization> root, CriteriaBuilder cb, String sortOrder) {
        return null;
    }
}
