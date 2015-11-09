package com.softserve.edu.service.calibrator.impl;

import com.softserve.edu.entity.enumeration.user.UserRole;
import com.softserve.edu.entity.enumeration.verification.Status;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.verification.Verification;
import com.softserve.edu.repository.UserRepository;
import com.softserve.edu.repository.VerificationRepository;
import com.softserve.edu.service.calibrator.CalibratorDigitalProtocolsService;
import com.softserve.edu.service.utils.ListToPageTransformer;
import com.softserve.edu.service.utils.NewVerificationsQueryConstructorVerificator;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.Query;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Set;

/**
 * Created by Veronichka on 04.11.2015.
 */
@Service
public class CalibratorDigitalProtocolsServiceImpl implements CalibratorDigitalProtocolsService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationRepository verificationRepository;

    @PersistenceContext
    private EntityManager em;

    private Logger logger = Logger.getLogger(CalibratorPlaningTaskServiceImpl.class);

    @Override
    public int findVerificationsByCalibratorEmployeeAndTaskStatusCount(String userName) {
        User user  = userRepository.findOne(userName);
        if (user == null){
            logger.error("Cannot found user!");
        }
        Set<UserRole> roles = user.getUserRoles();
        for (UserRole role : roles) {
            if (role.equals(UserRole.CALIBRATOR_ADMIN)) {
                return verificationRepository.findByTaskStatusAndCalibratorId(Status.TEST_COMPLETED, user.getOrganization().getId()).size();
            }
        }
        return verificationRepository.findByCalibratorEmployeeUsernameAndTaskStatus(user.getUsername(), Status.TEST_COMPLETED).size();

    }

    @Override
    public Page<Verification> findByTaskStatusAndCalibratorId(Long id, int pageNumber, int itemsPerPage, Status status) {
        Pageable pageRequest = new PageRequest(pageNumber - 1, itemsPerPage, new Sort(Sort.Direction.ASC,
                "clientData.clientAddress.district", "clientData.clientAddress.street", "clientData.clientAddress.building", "clientData.clientAddress.flat"));
        return verificationRepository.findByTaskStatusAndCalibratorId(status, id, pageRequest);
    }

    @Override
    public Page<Verification> findVerificationsByCalibratorEmployeeAndTaskStatus(String userName, int pageNumber, int itemsPerPage, Status status) {
        User user  = userRepository.findOne(userName);
        if (user == null){
            logger.error("Cannot found user!");
            throw new NullPointerException();
        }
        Set<UserRole> roles = user.getUserRoles();
        for (UserRole role : roles) {
            if (role.equals(UserRole.CALIBRATOR_ADMIN)) {
                return findByTaskStatusAndCalibratorId(user.getOrganization().getId(), pageNumber, itemsPerPage, status);
            }
        }
        Pageable pageRequest = new PageRequest(pageNumber - 1, itemsPerPage, new Sort(Sort.Direction.ASC,
                "clientData.clientAddress.district", "clientData.clientAddress.street", "clientData.clientAddress.building", "clientData.clientAddress.flat"));
        return verificationRepository.findByCalibratorEmployeeUsernameAndTaskStatus(user.getUsername(), status, pageRequest);
    }

    public Long countByCalibratorEmployee_usernameAndStatus (User calibratorEmployee, Status status) {
        return verificationRepository.countByCalibratorEmployee_usernameAndStatus(calibratorEmployee.getUsername(), status);
    }
//    @Transactional(readOnly = true)
//    public ListToPageTransformer<Verification> findPageOfVerificationsByCalibratorIdAndCriteriaSearch(
//            Long calibratorId, int pageNumber, int itemsPerPage, String dateToSearch, String idToSearch,
//            String fullNameToSearch, String streetToSearch, String status, String employeeName, String sortCriteria,
//            String sortOrder, User calibratorEmployee) {
//
//        CriteriaQuery<Verification> criteriaQuery = NewVerificationsQueryConstructorVerificator.buildSearchQuery(
//                calibratorId, dateToSearch, idToSearch, fullNameToSearch, streetToSearch, status, calibratorEmployee,
//                sortCriteria, sortOrder, employeeName, em);
//
//        Long count = em.createQuery(NewVerificationsQueryConstructorVerificator.buildCountQuery(
//                calibratorId, dateToSearch, idToSearch, fullNameToSearch, streetToSearch, status, calibratorEmployee,
//                employeeName, em)).getSingleResult();
//
//        TypedQuery<Verification> typedQuery = em.createQuery(criteriaQuery);
//        typedQuery.setFirstResult((pageNumber - 1) * itemsPerPage);
//        typedQuery.setMaxResults(itemsPerPage);
//        List<Verification> verificationList = typedQuery.getResultList();
//
//        ListToPageTransformer<Verification> result = new ListToPageTransformer<Verification>();
//        result.setContent(verificationList);
//        result.setTotalItems(count);
//        return result;
//    }

    @Transactional(readOnly = true)
    public List<Verification> findPageOfVerificationsByCalibratorIdAndStatus(
            User calibratorEmployee, int pageNumber, int itemsPerPage, Status status) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Verification> cq = cb.createQuery(Verification.class);
        Root<Verification> verifications = cq.from(Verification.class);
//        cq.select(cb.construct(Verification.class,
//                verifications.get("sentToCalibratorDate"),
//                verifications.get("clientData").get("firstName"),
//                verifications.get("clientData").get("lastName"),
//                verifications.get("clientData").get("clientAddress").get("district"),
//                verifications.get("providerEmployee"),
//                verifications.get("status")));
        Join<Verification, User> joinCalibratorEmployee = verifications.join("calibratorEmployee");

        cq.where(cb.and(cb.equal(verifications.get("stateVerificatorEmployee"), joinCalibratorEmployee.get("username")),
                cb.equal(verifications.get("status"), status)));
        TypedQuery<Verification> typedQuery = em.createQuery(cq);
//        typedQuery.setFirstResult((pageNumber - 1) * itemsPerPage);
//        typedQuery.setMaxResults(itemsPerPage);
//        List<Verification> ver = typedQuery.getResultList();

        List<Verification> result = typedQuery.getResultList();
        return result;
    }
}
