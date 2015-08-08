package com.softserve.edu.service.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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


public class NewVerificationsQueryConstructorProvider {

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
	public static CriteriaQuery<Verification> buildSearchQuery (Long providerId, String dateToSearch, String idToSearch, String lastNameToSearch,
			String streetToSearch, String status, User providerEmployee, String sortCriteria, String sortOrder, String employeeSearchName, EntityManager em) {

			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Verification> criteriaQuery = cb.createQuery(Verification.class);
			Root<Verification> root = criteriaQuery.from(Verification.class);
			Join<Verification, Organization> joinSearch = root.join("provider");

			Predicate predicate = NewVerificationsQueryConstructorProvider.buildPredicate(root, cb, joinSearch, providerId, dateToSearch, idToSearch,
																		lastNameToSearch, streetToSearch, status, providerEmployee, employeeSearchName);

			criteriaQuery.orderBy(SortCriteria.valueOf(sortCriteria.toUpperCase()).getSortOrder(root, cb, sortOrder));
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
	public static CriteriaQuery<Long> buildCountQuery (Long providerId, String dateToSearch, String idToSearch, String lastNameToSearch, String streetToSearch, String status,
															User providerEmployee, String employeeSearchName, EntityManager em) {
		
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
			Root<Verification> root = countQuery.from(Verification.class);
			Join<Verification, Organization> joinSearch = root.join("provider");
			Predicate predicate = NewVerificationsQueryConstructorProvider.buildPredicate(root, cb, joinSearch, providerId, dateToSearch, idToSearch,
																		lastNameToSearch, streetToSearch, status, providerEmployee, employeeSearchName);
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
	private static Predicate buildPredicate (Root<Verification> root, CriteriaBuilder cb, Join<Verification, Organization> joinSearch, Long providerId, 
												String dateToSearch,String idToSearch, String lastNameToSearch, String streetToSearch, String status, User providerEmployee, String employeeSearchName) {
	
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

	
		if ((status != null) && (!status.startsWith("?"))) {
			queryPredicate = cb.and(cb.equal(root.get("status"), Status.valueOf(status.trim())), queryPredicate);
		} else {
			queryPredicate = cb.and(cb.or(Status.SENT.getQueryPredicate(root, cb), Status.ACCEPTED.getQueryPredicate(root, cb)), queryPredicate);
		}

		queryPredicate = cb.and(cb.equal(joinSearch.get("id"), providerId), queryPredicate);

		if (dateToSearch != null) {		
			SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");	
			Date date = null;
			try {
				date = form.parse(dateToSearch.substring(0, 10));
				System.err.println("date before" + date);
				Calendar c = Calendar.getInstance();
				c.setTime(date);
				c.add(Calendar.DATE, 1);
				date = c.getTime();
				System.err.println("date after" + date);

			} catch (ParseException pe) {
				logger.error("Cannot parse date", pe);
			}
			queryPredicate = cb.and(cb.equal(root.get("initialDate"), date), queryPredicate);
		}

		if ((idToSearch != null)&&(idToSearch.length()>0)) {
			queryPredicate = cb.and(cb.like(root.get("id"), "%" + idToSearch + "%"), queryPredicate);
		}
		if ((lastNameToSearch != null)&&(lastNameToSearch.length()>0)) {
			queryPredicate = cb.and(cb.like(root.get("clientData").get("lastName"), "%" + lastNameToSearch + "%"),
					queryPredicate);
		}
		if ((streetToSearch != null)&&(streetToSearch.length()>0)) {
			queryPredicate = cb.and(
					cb.like(root.get("clientData").get("clientAddress").get("street"), "%" + streetToSearch + "%"),
					queryPredicate);
		}
		if ((employeeSearchName != null)&&(employeeSearchName.length()>0)) {
			Join<Verification, User> joinProviderEmployee = root.join("providerEmployee");
			Predicate searchByProviderName = cb.like(joinProviderEmployee.get("firstName"),"%" + employeeSearchName + "%");
			Predicate searchByProviderSurname = cb.like(joinProviderEmployee.get("lastName"),"%" + employeeSearchName + "%");
			Predicate searchByProviderLastName = cb.like(joinProviderEmployee.get("middleName"),"%" + employeeSearchName + "%");
			Predicate searchPredicateByProviderEmployeeName = cb.or(searchByProviderName, searchByProviderSurname, searchByProviderLastName);
			queryPredicate = cb.and(searchPredicateByProviderEmployeeName, queryPredicate);
		}
	
			return queryPredicate;
	}
	
}
