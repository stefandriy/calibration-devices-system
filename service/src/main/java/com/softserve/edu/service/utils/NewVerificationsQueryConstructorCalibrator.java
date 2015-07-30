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


	public class NewVerificationsQueryConstructorCalibrator {

		static Logger logger = Logger.getLogger(NewVerificationsQueryConstructorProvider.class);
		
		/**
		 * Method dynamically builds query to database depending on input parameters specified. 
		 * 
		 * @param providerId
		 * 		search by organization ID
		 * @param dateToSearch
		 * 		search by initial date of verification (optional)
		 * @param idToSearch
		 * 		search by verification ID
		 * @param lastNameToSearch
		 * 		search by client's last name 
		 * @param streetToSearch
		 * 		search by client's street
		 * @param providerEmployee
		 * 		used to additional query restriction if logged user is simple employee (not admin)
		 * @param em
		 * 		EntityManager needed to have a possibility to create query
	 	 * @return CriteriaQuery<Verification>
		 */
		public static CriteriaQuery<Verification> buildSearchQuery (Long providerId, String dateToSearch,
										String idToSearch, String lastNameToSearch, String streetToSearch, String status, 
										User calibratorEmployee, EntityManager em) {

				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<Verification> criteriaQuery = cb.createQuery(Verification.class);
				Root<Verification> root = criteriaQuery.from(Verification.class);
				Join<Verification, Organization> calibratorJoin = root.join("calibrator");

				Predicate predicate = NewVerificationsQueryConstructorCalibrator.buildPredicate(root, cb, calibratorJoin, providerId, dateToSearch, idToSearch,
																			lastNameToSearch, streetToSearch, status, calibratorEmployee);
				criteriaQuery.orderBy(cb.desc(root.get("initialDate")));
				criteriaQuery.select(root);
				criteriaQuery.where(predicate);
				return criteriaQuery;
		}
		
		/**
		 * Method dynamically builds query to database depending on input parameters specified. 
		 * Needed to get max count of rows with current predicates for pagination 
		 * 
		 * @param providerId
		 * 		search by organization ID
		 * @param dateToSearch
		 * 		search by initial date of verification (optional)
		 * @param idToSearch
		 * 		search by verification ID
		 * @param lastNameToSearch
		 * 		search by client's last name 
		 * @param streetToSearch
		 * 		search by client's street
		 * @param providerEmployee
		 * 		used to additional query restriction if logged user is simple employee (not admin)
		 * @param em
		 * 		EntityManager needed to have a possibility to create query
	 	 * @return CriteriaQuery<Long>
		 */
		public static CriteriaQuery<Long> buildCountQuery (Long calibratorId, String dateToSearch,
								String idToSearch, String lastNameToSearch, String streetToSearch, String status,
								User calibratorEmployee, EntityManager em) {
			
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
				Root<Verification> root = countQuery.from(Verification.class);
				Join<Verification, Organization> calibratorJoin = root.join("calibrator");
				Predicate predicate = NewVerificationsQueryConstructorCalibrator.buildPredicate(root, cb, calibratorJoin, calibratorId, dateToSearch, idToSearch,
																			lastNameToSearch, streetToSearch, status, calibratorEmployee);
				countQuery.select(cb.count(root));
				countQuery.where(predicate);
				return countQuery;
				}
		/**
		 * Method builds list of predicates depending on parameters passed
		 * Rule for predicates compounding - conjunction (AND)
		 * 
		 * @param root
		 * @param cb
		 * @param joinSearch
		 * @param providerId
		 * @param dateToSearch
		 * @param idToSearch
		 * @param lastNameToSearch
		 * @param streetToSearch
		 * @param providerEmployee
		 * @return Predicate 
		 */
		private static Predicate buildPredicate (Root<Verification> root, CriteriaBuilder cb, Join<Verification, Organization> joinSearch, Long calibratorId, 
													String dateToSearch,String idToSearch, String lastNameToSearch, String streetToSearch, String status, User calibratorEmployee) {
		
			String userName = calibratorEmployee.getUsername();
			Predicate queryPredicate = cb.conjunction();
			Set<UserRole> roles= calibratorEmployee.getUserRoles();
				for (UserRole userRole : roles) {
					String role = userRole.getRole();
					if(role.equalsIgnoreCase("CALIBRATOR_EMPLOYEE")) {
						Join<Verification, User> joinCalibratorEmployee = root.join("calibratorEmployee", JoinType.LEFT);
						Predicate searchPredicateByUsername =cb.equal(joinCalibratorEmployee.get("username"), userName);
						Predicate searchPredicateByEmptyField = cb.isNull(joinCalibratorEmployee.get("username"));
						Predicate searchByCalibratorEmployee=cb.or(searchPredicateByUsername,searchPredicateByEmptyField);
						queryPredicate=cb.and(searchByCalibratorEmployee);
					}
				}

//				if ((status != null)&&(status.equalsIgnoreCase("IN_PROGRESS"))) {
//					queryPredicate = cb.and(cb.equal(root.get("status"), Status.IN_PROGRESS), queryPredicate);
//				} else if ((status != null)&&(status.equalsIgnoreCase("SENT_TO_TEST_DEVICE"))) {
//					queryPredicate = cb.and(cb.equal(root.get("status"), Status.SENT_TO_TEST_DEVICE), queryPredicate);
//				} else if ((status != null)&&(status.equalsIgnoreCase("TEST_PLACE_DETERMINED"))) {
//					queryPredicate = cb.and(cb.equal(root.get("status"), Status.TEST_PLACE_DETERMINED), queryPredicate);
//				} else if ((status != null)&&(status.equalsIgnoreCase("TEST_COMPLETED"))) {
//					queryPredicate = cb.and(cb.equal(root.get("status"), Status.TEST_COMPLETED), queryPredicate);	
//				} else {	
//					Predicate inProgress = cb.equal(root.get("status"), Status.IN_PROGRESS);
//					Predicate sentToTest = cb.equal(root.get("status"), Status.SENT_TO_TEST_DEVICE);
//					Predicate testPlaceDetermined = cb.equal(root.get("status"), Status.TEST_PLACE_DETERMINED);
//					Predicate testCompleted = cb.equal(root.get("status"), Status.TEST_COMPLETED);
//					queryPredicate = cb.and(cb.or(inProgress, sentToTest, testPlaceDetermined, testCompleted), queryPredicate);
//				}
				
				
				if ((status != null)&&(!status.startsWith("?"))) {
					queryPredicate = cb.and(cb.equal(root.get("status"), Status.valueOf(status.trim())), queryPredicate);
				} else {
					queryPredicate = cb.and(cb.or(Status.IN_PROGRESS.getQueryPredicate(root, cb), Status.SENT_TO_TEST_DEVICE.getQueryPredicate(root, cb), 
							Status.TEST_PLACE_DETERMINED.getQueryPredicate(root, cb), Status.TEST_COMPLETED.getQueryPredicate(root, cb)), queryPredicate);
				}
			
				queryPredicate = cb.and(cb.equal(joinSearch.get("id"), calibratorId), queryPredicate);
					
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


