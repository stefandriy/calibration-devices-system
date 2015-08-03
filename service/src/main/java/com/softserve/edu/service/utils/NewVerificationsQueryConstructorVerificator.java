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


public class NewVerificationsQueryConstructorVerificator {

    static Logger logger = Logger.getLogger(NewVerificationsQueryConstructorVerificator.class);

    /**
     * Method dynamically builds query to database depending on input parameters specified.
     *
     * @param verificatorID
     * 		search by organization ID
     * @param dateToSearch
     * 		search by initial date of verification (optional)
     * @param idToSearch
     * 		search by verification ID
     * @param lastNameToSearch
     * 		search by client's last name
     * @param streetToSearch
     * 		search by client's street
     * @param verificatorEmployee
     * 		used to additional query restriction if logged user is simple employee (not admin)
     * @param em
     * 		EntityManager needed to have a possibility to create query
     * @return CriteriaQuery<Verification>
     */
    public static CriteriaQuery<Verification> buildSearchQuery (Long verificatorID, String dateToSearch, String idToSearch, String lastNameToSearch, String streetToSearch, String status,
                                                                							User verificatorEmployee, String employeeSearchName, EntityManager em) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Verification> criteriaQuery = cb.createQuery(Verification.class);
        Root<Verification> root = criteriaQuery.from(Verification.class);
        Join<Verification, Organization> verificatorJoin = root.join("stateVerificator");

        Predicate predicate = NewVerificationsQueryConstructorVerificator.buildPredicate(root, cb, verificatorJoin, verificatorID, dateToSearch, idToSearch,
                														lastNameToSearch, streetToSearch, status, verificatorEmployee, employeeSearchName);
        criteriaQuery.orderBy(cb.desc(root.get("initialDate")));
        criteriaQuery.select(root);
        criteriaQuery.where(predicate);
        return criteriaQuery;
    }

    /**
     * Method dynamically builds query to database depending on input parameters specified.
     * Needed to get max count of rows with current predicates for pagination
     *
     * @param verificatorID
     * 		search by organization ID
     * @param dateToSearch
     * 		search by initial date of verification (optional)
     * @param idToSearch
     * 		search by verification ID
     * @param lastNameToSearch
     * 		search by client's last name
     * @param streetToSearch
     * 		search by client's street
     * @param verificatorEmployee
     * 		used to additional query restriction if logged user is simple employee (not admin)
     * @param em
     * 		EntityManager needed to have a possibility to create query
     * @return CriteriaQuery<Long>
     */
    public static CriteriaQuery<Long> buildCountQuery (Long verificatorID, String dateToSearch, String idToSearch, String lastNameToSearch, String streetToSearch, String status,
                                                       User verificatorEmployee, String employeeSearchName, EntityManager em) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Verification> root = countQuery.from(Verification.class);
        Join<Verification, Organization> verificatorJoin = root.join("stateVerificator");
        Predicate predicate = NewVerificationsQueryConstructorVerificator.buildPredicate(root, cb, verificatorJoin, verificatorID, dateToSearch, idToSearch,
                lastNameToSearch, streetToSearch, status, verificatorEmployee, employeeSearchName);
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
     * @param verificatorID
     * @param dateToSearch
     * @param idToSearch
     * @param lastNameToSearch
     * @param streetToSearch
     * @param verificatorEmployee
     * @return Predicate
     */
    private static Predicate buildPredicate (Root<Verification> root, CriteriaBuilder cb, Join<Verification, Organization> joinSearch, Long verificatorId,
                                             String dateToSearch,String idToSearch, String lastNameToSearch, String streetToSearch, String status, User verificatorEmployee, String employeeSearchName) {

        String userName = verificatorEmployee.getUsername();
        Predicate queryPredicate = cb.conjunction();
        Set<UserRole> roles= verificatorEmployee.getUserRoles();
        for (UserRole userRole : roles) {
            String role = userRole.getRole();
            if(role.equalsIgnoreCase("STATE_VERIFICATOR_EMPLOYEE")) {
                Join<Verification, User> joinVerificatorEmployee = root.join("stateVerificatorEmployee", JoinType.LEFT);
                Predicate searchPredicateByUsername =cb.equal(joinVerificatorEmployee.get("username"), userName);
                Predicate searchPredicateByEmptyField = cb.isNull(joinVerificatorEmployee.get("username"));
                Predicate searchByVerificatorEmployee=cb.or(searchPredicateByUsername,searchPredicateByEmptyField);
                queryPredicate=cb.and(searchByVerificatorEmployee);
            }
        }

        if ((status != null)&&(status.equalsIgnoreCase("SENT_TO_VERIFICATOR"))) {
            queryPredicate = cb.and(cb.equal(root.get("status"), Status.SENT_TO_VERIFICATOR), queryPredicate);
        } else {
            Predicate sentToVerificator = cb.equal(root.get("status"), Status.SENT_TO_VERIFICATOR);
            queryPredicate = cb.and(cb.or(sentToVerificator), queryPredicate);
        }

        queryPredicate = cb.and(cb.equal(joinSearch.get("id"), verificatorId), queryPredicate);

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

        if (employeeSearchName != null) {
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


