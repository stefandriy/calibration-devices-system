package com.softserve.edu.service.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;

import com.softserve.edu.entity.Organization;
import com.softserve.edu.entity.Verification;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.user.UserRole;
import com.softserve.edu.entity.util.Status;

public class ArchivalVerificationsQueryConstructorProvider {
static Logger logger = Logger.getLogger(ArchivalVerificationsQueryConstructorProvider.class);
	
	
	public static CriteriaQuery<Verification> buildSearchQuery (Long employeeId, String dateToSearch,
									String idToSearch, String lastNameToSearch, String streetToSearch, String status, String employeeName, 
									User providerEmployee, EntityManager em) {

			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Verification> criteriaQuery = cb.createQuery(Verification.class);
			Root<Verification> root = criteriaQuery.from(Verification.class);
			Join<Verification, Organization> providerJoin = root.join("provider");
			Predicate predicate = ArchivalVerificationsQueryConstructorProvider.buildPredicate(root, cb, employeeId, dateToSearch, idToSearch,
																		lastNameToSearch, streetToSearch, status,
																		employeeName, providerEmployee, providerJoin);
			criteriaQuery.orderBy(cb.desc(root.get("initialDate")));
			criteriaQuery.select(root);
			criteriaQuery.where(predicate);
			return criteriaQuery;
	}
	
	
	public static CriteriaQuery<Long> buildCountQuery (Long employeeId, String dateToSearch,
							String idToSearch, String lastNameToSearch, String streetToSearch, String status, String employeeName,
							User providerEmployee, EntityManager em) {
		
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
			Root<Verification> root = countQuery.from(Verification.class);
			Join<Verification, Organization> providerJoin = root.join("provider");
			Predicate predicate = ArchivalVerificationsQueryConstructorProvider.buildPredicate(root, cb, employeeId, dateToSearch, idToSearch,
																		lastNameToSearch, streetToSearch, status, employeeName, providerEmployee, providerJoin);
			countQuery.select(cb.count(root));
			countQuery.where(predicate);
			return countQuery;
			}
	
	private static Predicate buildPredicate (Root<Verification> root, CriteriaBuilder cb, Long providerId, 
																	String dateToSearch,String idToSearch, String lastNameToSearch,
																	String streetToSearch, String searchStatus, String employeeName, User employee,
																		Join<Verification, Organization> providerJoin) {

		Predicate queryPredicate = cb.conjunction();		
		queryPredicate = cb.and(cb.equal(providerJoin.get("id"), providerId), queryPredicate);
						
			if(searchStatus != null) {
				for (Status status : Status.values()) {
					if(status.toString().equalsIgnoreCase(searchStatus)){
						queryPredicate =  cb.and(cb.equal(root.get("status"), Status.valueOf(searchStatus.toUpperCase())), queryPredicate);
					}
				}				
			}
				
				 if (dateToSearch != null) {
					 SimpleDateFormat form = new SimpleDateFormat("dd-MM-yyyy");
					 Date date = null;
					 try {
					    date = form.parse(dateToSearch);
					 } catch (ParseException pe) {
					    logger.error("Cannot parse date", pe);
					 }
					 queryPredicate = cb.and(cb.equal(root.get("initialDate"), date), queryPredicate);
				}
	
				 if (idToSearch != null) {
					 queryPredicate = cb.and(cb.like(root.get("id"), "%" + idToSearch + "%"), queryPredicate);
				 }
				 if (lastNameToSearch !=null) {
				   queryPredicate = cb.and(cb.like(root.get("clientData").get("lastName"), "%" + lastNameToSearch + "%"), queryPredicate);
				 }
				 if (streetToSearch != null) {
				   queryPredicate = cb.and(cb.like(root.get("clientData").get("clientAddress").get("street"), "%" + streetToSearch + "%"), queryPredicate);
				 }
				 if(employeeName != null) {
					Join<Verification, User> joinProviderEmployee = root.join("providerEmployee");
						Predicate searchByProviderName =cb.like(joinProviderEmployee.get("firstName"), "%" + employeeName + "%");
						Predicate searchByProviderSurname = cb.like(joinProviderEmployee.get("lastName"), "%" + employeeName + "%");
						Predicate searchByProviderLastName = cb.like(joinProviderEmployee.get("middleName"), "%" + employeeName + "%");
						Predicate searchPredicateByProviderEmployeeName=cb.or(searchByProviderName,searchByProviderSurname, searchByProviderLastName);  
						queryPredicate = cb.and(searchPredicateByProviderEmployeeName, queryPredicate);
				 }
	
			return queryPredicate;
	}
}
