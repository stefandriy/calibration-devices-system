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

public class ArchivalVerificationsQueryConstructor {
static Logger logger = Logger.getLogger(NewVerificationsQueryConstructorProvider.class);
	
	
	public static CriteriaQuery<Verification> buildSearchQuery (Long providerId, String dateToSearch,
									String idToSearch, String lastNameToSearch, String streetToSearch, String status, 
									User providerEmployee, EntityManager em) {

			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Verification> criteriaQuery = cb.createQuery(Verification.class);
			Root<Verification> root = criteriaQuery.from(Verification.class);
			Join<Verification, Organization> joinSearch = root.join("provider");

			Predicate predicate = ArchivalVerificationsQueryConstructor.buildPredicate(root, cb, joinSearch, providerId, dateToSearch, idToSearch,
																		lastNameToSearch, streetToSearch, status, providerEmployee);
			criteriaQuery.orderBy(cb.desc(root.get("initialDate")));
			criteriaQuery.select(root);
			criteriaQuery.where(predicate);
			return criteriaQuery;
	}
	
	
	public static CriteriaQuery<Long> buildCountQuery (Long providerId, String dateToSearch,
							String idToSearch, String lastNameToSearch, String streetToSearch, String status,
							User providerEmployee, EntityManager em) {
		
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
			Root<Verification> root = countQuery.from(Verification.class);
			Join<Verification, Organization> joinSearch = root.join("provider");
			Predicate predicate = ArchivalVerificationsQueryConstructor.buildPredicate(root, cb, joinSearch, providerId, dateToSearch, idToSearch,
																		lastNameToSearch, streetToSearch, status, providerEmployee);
			countQuery.select(cb.count(root));
			countQuery.where(predicate);
			return countQuery;
			}
	
	private static Predicate buildPredicate (Root<Verification> root, CriteriaBuilder cb, Join<Verification, Organization> joinSearch, Long providerId, 
												String dateToSearch,String idToSearch, String lastNameToSearch, String streetToSearch, String status, User providerEmployee) {
	
		String userName = providerEmployee.getUsername();
		Predicate queryPredicate = cb.conjunction();
		Set<UserRole> roles= providerEmployee.getUserRoles();
			for (UserRole userRole : roles) {
				String role = userRole.getRole();
				if(role.equalsIgnoreCase("PROVIDER_EMPLOYEE")) {
					Join<Verification, User> joinSearchProviderEmployee = root.join("providerEmployee", JoinType.LEFT);
					Predicate searchPredicateByUsername =cb.equal(joinSearchProviderEmployee.get("username"), userName);
					Predicate searchPredicateByEmptyField = cb.isNull(joinSearchProviderEmployee.get("username"));
					Predicate searchPredicateByProviderEmployee=cb.or(searchPredicateByUsername,searchPredicateByEmptyField);
					queryPredicate=cb.and(searchPredicateByProviderEmployee);
				}
			}

			queryPredicate = cb.and(cb.equal(joinSearch.get("id"), providerId), queryPredicate);

			if(status != null) {
				switch (status.toUpperCase()) {
				case "SENT":
					queryPredicate = cb.and(cb.equal(root.get("status"), Status.SENT), queryPredicate);
					break;
				case "ACCEPTED":
					queryPredicate = cb.and(cb.equal(root.get("status"), Status.ACCEPTED), queryPredicate);
					break;
				case "REJECTED":
					queryPredicate = cb.and(cb.equal(root.get("status"), Status.REJECTED), queryPredicate);
					break;
				case "IN_PROGRESS":
					queryPredicate = cb.and(cb.equal(root.get("status"), Status.IN_PROGRESS), queryPredicate);
					break;
				case "TEST_PLACE_DETERMINED":
					queryPredicate = cb.and(cb.equal(root.get("status"), Status.TEST_PLACE_DETERMINED), queryPredicate);
					break;
				case "SENT_TO_TEST_DEVICE":
					queryPredicate = cb.and(cb.equal(root.get("status"), Status.SENT_TO_TEST_DEVICE), queryPredicate);
					break;
				case "TEST_COMPLETED":
					queryPredicate = cb.and(cb.equal(root.get("status"), Status.TEST_COMPLETED), queryPredicate);
					break;
				case "SENT_TO_VERIFICATOR":
					queryPredicate = cb.and(cb.equal(root.get("status"), Status.SENT_TO_VERIFICATOR), queryPredicate);
					break;
				case "TEST_OK":
					queryPredicate = cb.and(cb.equal(root.get("status"), Status.TEST_OK), queryPredicate);
					break;
				case "TEST_NOK":
					queryPredicate = cb.and(cb.equal(root.get("status"), Status.TEST_NOK), queryPredicate);
					break;

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
	
			return queryPredicate;
	}
}
