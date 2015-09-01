package com.softserve.edu.service.utils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

import com.softserve.edu.entity.Verification;

public enum SortCriteria {
	ID() {
	    	public Order getSortOrder(Root<Verification> root, CriteriaBuilder cb, String sortOrder) {
	    	
	    		if(sortOrder.equalsIgnoreCase("asc")) {
	    			return cb.asc(root.get("id"));
	    		} else {
	    			return cb.desc(root.get("id"));
	    		}
	        }
	    },
	DATE() {
	    	public Order getSortOrder(Root<Verification> root, CriteriaBuilder cb, String sortOrder) {
	    
	    		if(sortOrder.equalsIgnoreCase("asc")) {
	    			return cb.asc(root.get("initialDate"));
	    		} else {
	    			return cb.desc(root.get("initialDate"));
	    		}
	        }
	    },
	CLIENT_LAST_NAME() {
	    	public Order getSortOrder(Root<Verification> root, CriteriaBuilder cb, String sortOrder) {
	    	
	    		if(sortOrder.equalsIgnoreCase("asc")) {
	    			return cb.asc(root.get("clientData").get("lastName"));
	    		} else {
	    			return cb.desc(root.get("clientData").get("lastName"));
	    		}
	        }
	    },
	STREET() {
	    	public Order getSortOrder(Root<Verification> root, CriteriaBuilder cb, String sortOrder) {
	    
	    		if(sortOrder.equalsIgnoreCase("asc")) {
	    			return cb.asc(root.get("clientData").get("clientAddress").get("street"));
	    		} else {
	    			return cb.desc(root.get("clientData").get("clientAddress").get("street"));
	    		}
	        }
	    },
	REGION() {
		public Order getSortOrder(Root<Verification> root, CriteriaBuilder cb, String sortOrder) {

			if(sortOrder.equalsIgnoreCase("asc")) {
				return cb.asc(root.get("clientData").get("clientAddress").get("region"));
			} else {
				return cb.desc(root.get("clientData").get("clientAddress").get("region"));
			}
		}
	},
	STATUS() {
	    	public Order getSortOrder(Root<Verification> root, CriteriaBuilder cb, String sortOrder) {
	 
	    		if(sortOrder.equalsIgnoreCase("asc")) {
	    			return cb.asc(root.get("status"));
	    		} else {
	    			return cb.desc(root.get("status"));
	    		}
	        }
	    },
	EMPLOYEE_LAST_NAME() {
	    	public Order getSortOrder(Root<Verification> root, CriteriaBuilder cb, String sortOrder) {
	    	
	    		if(sortOrder.equalsIgnoreCase("asc")) {
	    			return (cb.asc(root.join("providerEmployee").get("lastName")));
	    		} else {
	    			return (cb.desc(root.join("providerEmployee").get("lastName")));
	    		}
	        }
	    };
	
	 public Order getSortOrder(Root<Verification> root, CriteriaBuilder cb, String sortOrder) {
	    	return null;
	    }
}





