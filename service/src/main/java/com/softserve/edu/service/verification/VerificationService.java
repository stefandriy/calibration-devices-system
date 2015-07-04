package com.softserve.edu.service.verification;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import com.softserve.edu.entity.*;

import com.softserve.edu.entity.user.ProviderEmployee;
import com.softserve.edu.entity.util.ReadStatus;

import com.softserve.edu.entity.util.Status;
import com.softserve.edu.repository.CalibrationTestRepository;
import com.softserve.edu.repository.VerificationRepository;
import com.softserve.edu.service.exceptions.NotAvailableException;

import com.softserve.edu.service.utils.ListToPageTransformer;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VerificationService {
	Logger logger = Logger.getLogger(VerificationService.class);

	@Autowired
	private VerificationRepository verificationRepository;

	@Autowired
	private CalibrationTestRepository calibrationTestRepository;

	@PersistenceContext
	private EntityManager em;

	@Transactional
	public void saveVerification(Verification verification) {
		verificationRepository.save(verification);
	}

	@Transactional(readOnly = true)
	public Verification findById(String code) {
		return verificationRepository.findOne(code);
	}

	/**
	 * Returns requested number(page) of Verification entities(itemsPerPage
	 * parameter) that belongs to specific organization. Note: pagination starts
	 * from 1 at client side, but Spring Data JPA from 0.
	 *
	 * @param pageNumber
	 *            Number of partial data that will be returned.
	 * @param itemsPerPage
	 *            Number of Verification-s that will be present in one page(unit
	 *            of partial data).
	 * @return Requested page of Verification-s that belong to specific
	 *         organization.
	 */
	@Transactional(readOnly = true)
	public Page<Verification> findPageOfAllVerificationsByProviderId(Long providerId, int pageNumber,
			int itemsPerPage) {
		Pageable pageRequest = new PageRequest(pageNumber - 1, itemsPerPage);
		return verificationRepository.findByProviderId(providerId, pageRequest);
	}

	@Transactional(readOnly = true)
	public Page<Verification> findPageOfAllVerificationsByCalibratorId(Long calibratorId, int pageNumber,
			int itemsPerPage) {
		Pageable pageRequest = new PageRequest(pageNumber - 1, itemsPerPage);
		return verificationRepository.findByCalibratorId(calibratorId, pageRequest);
	}

	@Transactional(readOnly = true)
	public Page<Verification> findPageOfAllVerificationsByStateVerificatorId(Long stateVerificatorId, int pageNumber,
			int itemsPerPage) {
		Pageable pageRequest = new PageRequest(pageNumber - 1, itemsPerPage);
		return verificationRepository.findByStateVerificatorId(stateVerificatorId, pageRequest);
	}

	@Transactional(readOnly = true)
	public long findCountOfNewVerificationsByCalibratorId(Long providerId) {
		return verificationRepository.countByCalibratorIdAndStatusAndReadStatus(providerId, Status.RECEIVED,
				ReadStatus.UNREAD);
	}

	@Transactional(readOnly = true)
	public long findCountOfNewVerificationsByProviderId(Long providerId) {
		return verificationRepository.countByProviderIdAndStatusAndReadStatus(providerId, Status.SENT,
				ReadStatus.UNREAD);
	}

	@Transactional(readOnly = true)
	public Page<Verification> findPageOfSentVerificationsByProviderId(Long providerId, int pageNumber,
			int itemsPerPage) {
		Pageable pageRequest = new PageRequest(pageNumber - 1, itemsPerPage);
		return verificationRepository.findByProviderIdAndStatusOrderByInitialDateDesc(providerId, Status.SENT,
				pageRequest);
	}

	/**
	 * Returns requested number(page) of Verification entities(itemsPerPage
	 * parameter) that belongs to specific calibrator and have status received.
	 * Note: pagination starts from 1 at client side, but Spring Data JPA from
	 * 0.
	 *
	 * @param calibratorId
	 *            id of calibrator.
	 * @param pageNumber
	 *            Number of partial data that will be returned.
	 * @param itemsPerPage
	 *            Number of Verification-s that will be present in one page(unit
	 *            of partial data).
	 * @return Requested page of Verification-s that belong to specific
	 *         organization.
	 */
	@Transactional(readOnly = true)
	public Page<Verification> findPageOfSentVerificationsByCalibratorId(Long calibratorId, int pageNumber,
			int itemsPerPage) {
		Pageable pageRequest = new PageRequest(pageNumber - 1, itemsPerPage);
		return verificationRepository.findByCalibratorIdAndStatusOrderByInitialDateDesc(calibratorId, Status.RECEIVED,
				pageRequest);
	}

	@Transactional(readOnly = true)
	public Page<Verification> findPageOfSentVerificationsByCalibratorIdAndSearch(Long calibratorId, int pageNumber,
			int itemsPerPage, String searchType, String searchText) {
		Pageable pageRequest = new PageRequest(pageNumber - 1, itemsPerPage);
		switch (searchType) {
		case "id":
			return verificationRepository.findByCalibratorIdAndStatusAndIdLikeIgnoreCase(calibratorId, Status.RECEIVED,
					"%" + searchText + "%", pageRequest);

		case "lastName":
			return verificationRepository.findByCalibratorIdAndStatusAndClientData_lastNameLikeIgnoreCase(calibratorId,
					Status.RECEIVED, "%" + searchText + "%", pageRequest);

		case "street":
			return verificationRepository.findByCalibratorIdAndStatusAndClientDataClientAddressStreetLikeIgnoreCase(
					calibratorId, Status.RECEIVED, "%" + searchText + "%", pageRequest);

		case "date":

			SimpleDateFormat form = new SimpleDateFormat("dd-MM-yyyy");
			Date date = null;
			try {
				date = form.parse(searchText);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return verificationRepository.findByCalibratorIdAndStatusAndInitialDateLike(calibratorId, Status.RECEIVED,
					date, pageRequest);

		default:
			return verificationRepository.findByCalibratorIdAndStatusOrderByInitialDateDesc(calibratorId,
					Status.RECEIVED, pageRequest);
		}

	}

	@Transactional(readOnly = true)
	public Page<Verification> findPageOfSentVerificationsByStateVerificatorId(Long stateVerificatorId, int pageNumber,
			int itemsPerPage) {
		Pageable pageRequest = new PageRequest(pageNumber - 1, itemsPerPage);
		return verificationRepository.findByStateVerificatorIdAndStatus(stateVerificatorId, Status.IN_PROGRESS,
				pageRequest);
	}

	// @Transactional(readOnly = true)
	// public Page<Verification>
	// findPageOfSentVerificationsByProviderIdAndSearch(
	// Long providerId, int pageNumber, int itemsPerPage, String searchType,
	// String searchText) {
	// Pageable pageRequest = new PageRequest(pageNumber - 1, itemsPerPage);
	// switch (searchType) {
	// case "id":
	// return
	// verificationRepository.findByProviderIdAndStatusAndIdLikeIgnoreCase(providerId,
	// Status.SENT, "%"+ searchText +"%", pageRequest);
	//
	// case "lastName":
	// return
	// verificationRepository.findByProviderIdAndStatusAndClientData_lastNameLikeIgnoreCase(providerId,
	// Status.SENT, "%"+ searchText +"%", pageRequest);
	//
	// case "street":
	// return
	// verificationRepository.findByProviderIdAndStatusAndClientDataClientAddressStreetLikeIgnoreCase(providerId,
	// Status.SENT, "%"+ searchText +"%", pageRequest);
	//
	// case "date":
	//
	// SimpleDateFormat form = new SimpleDateFormat("dd-MM-yyyy");
	// Date date = null;
	// try {
	// date = form.parse(searchText);
	// } catch (ParseException e) {
	// e.printStackTrace();
	// }
	// return
	// verificationRepository.findByProviderIdAndStatusAndInitialDate(providerId,
	// Status.SENT, date, pageRequest);
	//
	// default:
	// return
	// verificationRepository.findByProviderIdAndStatusOrderByInitialDateDesc(providerId,
	// Status.SENT, pageRequest);
	// }
	//
	// }

	@Transactional(readOnly = true)
	public ListToPageTransformer<Verification> findPageOfSentVerificationsByProviderIdAndCriteriaSearch(Long providerId,
			int pageNumber, int itemsPerPage, String dateToSearch, String idToSearch, String lastNameToSearch,
			String streetToSearch) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Verification> criteriaQuery = cb.createQuery(Verification.class);
		Root<Verification> root = criteriaQuery.from(Verification.class);
		Path<Long> provider = root.join("provider").get("id");

		List<Predicate> predicates = new ArrayList<Predicate>();

		Predicate statusPredicate = cb.equal(root.get("status"), Status.SENT);
		predicates.add(statusPredicate);

		Predicate providerPredicate = cb.equal(provider, providerId);
		predicates.add(providerPredicate);

		System.err.println("date to search " + dateToSearch);
		if (dateToSearch.length() > 5) {

			SimpleDateFormat form = new SimpleDateFormat("dd-MM-yyyy");
			Date date = null;
			try {
				date = form.parse(dateToSearch);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			Predicate datePredicate = cb.equal(root.get("initialDate"), date);
			predicates.add(datePredicate);
		}

		if (idToSearch.length() > 0) {
			Predicate verifIdPredicate = cb.like(root.get("id"), "%" + idToSearch + "%");
			predicates.add(verifIdPredicate);
		}

		if (lastNameToSearch.length() > 0) {
			Predicate lastNamePredicate = cb.like(root.get("clientData").get("lastName"), "%" + lastNameToSearch + "%");
			predicates.add(lastNamePredicate);
		}

		if (streetToSearch.length() > 0) {
			Predicate streetPredicate = cb.like(root.get("clientData").get("clientAddress").get("street"),
					"%" + streetToSearch + "%");
			predicates.add(streetPredicate);
		}

		criteriaQuery.select(root).where(predicates.toArray(new Predicate[predicates.size()]));

		CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
		countQuery.select(cb.count(countQuery.from(Verification.class)));// .where(predicates.toArray(new
																			// Predicate[]{}));
		Long count = em.createQuery(countQuery).getSingleResult();

		TypedQuery<Verification> typedQuery = em.createQuery(criteriaQuery);

		typedQuery.setFirstResult((pageNumber - 1) * itemsPerPage);
		typedQuery.setMaxResults(itemsPerPage);
		List<Verification> verificationList = typedQuery.getResultList();
		ListToPageTransformer<Verification> result = new ListToPageTransformer<Verification>();

		result.setContent(verificationList);
		result.setTotalItems(count);

		return result;
	}

	@Transactional(readOnly = true)
	public Verification findByIdAndProviderId(String id, Long providerId) {
		Verification verification = verificationRepository.findByIdAndProviderId(id, providerId);
		if (verification == null) {
			throw new AccessDeniedException("You have not permission to get this data.");
		}
		return verification;
	}

	/**
	 * Returns requested number(page) of Verification entities(itemsPerPage
	 * parameter) that belongs to specific calibrator and have status received.
	 * Note: pagination starts from 1 at client side, but Spring Data JPA from
	 * 0.
	 *
	 * @param id
	 *            Id of the verification
	 * @param calibratorId
	 *            Number id of provider assigned to this verification
	 * @return Verification that belong to specific calibrator
	 */
	@Transactional(readOnly = true)
	public Verification findByIdAndCalibratorId(String id, Long calibratorId) {
		Verification verification = verificationRepository.findByIdAndCalibratorId(id, calibratorId);
		if (verification == null) {
			throw new AccessDeniedException("You have not permission to get this data.");
		}
		return verification;
	}

	@Transactional(readOnly = true)
	public Verification findByIdAndStateVerificatorId(String id, Long stateVerificatorId) {
		Verification verification = verificationRepository.findByIdAndStateVerificatorId(id, stateVerificatorId);
		if (verification == null) {
			throw new AccessDeniedException("You have not permission to get this data.");
		}
		return verification;
	}

	/**
	 * Find verification, add receive status to calibrator, add calibrator to
	 * verification save verification
	 */
	@Transactional
	public void updateVerification(String verificationId, Calibrator calibrator, ProviderEmployee providerEmployee) {
		Verification verification = verificationRepository.findOne(verificationId);

		if (verification == null) {
			logger.error("verification haven't found");
			return;
		}
		verification.setStatus(Status.RECEIVED);
		verification.setCalibrator(calibrator);
		verification.setProviderEmployee(providerEmployee);
		verification.setReadStatus(ReadStatus.UNREAD);
		verificationRepository.save(verification);
	}

	@Transactional
	public void updateVerificationReadStatus(String verificationId, String readStatus) {
		Verification verification = verificationRepository.findOne(verificationId);
		if (verification == null) {
			logger.error("verification haven't found");
			return;
		}
		System.out.println("inside service updating verif addressed from calibrator...");
		verification.setReadStatus(ReadStatus.READ);
		verificationRepository.save(verification);
	}

	/**
	 * Find verification, add IN_PROGRESS status to state verificator, add state
	 * verificator to verification. /** Find verification, add IN_PROGRESS
	 * status to state verificator, add stat verificator to verification. save
	 * verification
	 */
	@Transactional
	public void updateVerificationByCalibrator(String verificationId, StateVerificator stateVerificator) {
		Verification verification = verificationRepository.findOne(verificationId);
		verification.setStatus(Status.IN_PROGRESS);
		verification.setStateVerificator(stateVerificator);
		verification.setReadStatus(ReadStatus.UNREAD);
		verificationRepository.save(verification);
	}

	/**
	 * Find verification, add complete status to stateVerificator, add
	 * stateVerificator to verification save verification
	 */
	@Transactional
	public void updateVerification(String verificationId, StateVerificator stateVerificator) {
		Verification verification = verificationRepository.findOne(verificationId);
		if (verification == null) {
			logger.error("verification haven't found");
			return;
		}
		verification.setStatus(Status.COMPLETED);
		verification.setStateVerificator(stateVerificator);
		verificationRepository.save(verification);
	}

	/**
	 * Find verification, add receive status to Provider, add Provider to
	 * verification save verification
	 */
	// @Transactional
	// public void updateVerification(String verificationId, Provider provider){
	// Verification verification =
	// verificationRepository.findOne(verificationId);
	// if (verification == null) {
	// logger.error("verification haven't found");
	// return;
	// }
	// verification.setStatus(Status.COMPLETED);
	// verification.setProvider(provider);
	// verificationRepository.save(verification);
	// }
	//
	// /**
	// * Find verification, add RECEIVED status to provider, add provider to
	// verification.
	// * save verification
	// * SOME SH*T!!!
	// */
	// @Transactional
	// public void updateVerificationByStateVerificator(String verificationId,
	// Provider provider) {
	// Verification verification =
	// verificationRepository.findOne(verificationId);
	// verification.setStatus(Status.RECEIVED);
	// verification.setProvider(provider);
	// verificationRepository.save(verification);
	// }

	/**
	 * Returns calibration test assigned to verification
	 *
	 * @param verificationId
	 *            Id of the verification
	 * @param data
	 *            all data filled by calibrator in test
	 * @return test data with assigned verification that belong to specific
	 *         calibrator
	 * @throws NotAvailableException
	 *             if there is no verification with such id
	 */
	@Transactional
	public CalibrationTest createCalibrationTest(String verificationId, CalibrationTest data) {
		Verification updatedVerification = verificationRepository.findOne(verificationId);
		if (updatedVerification == null) {
			throw new NotAvailableException("Повірки з таким ID не існує");
		}
		CalibrationTest testData = calibrationTestRepository.save(data);
		testData.setVerification(updatedVerification);
		return testData;
	}

	@Transactional
	public Long countByProviderEmployeeTasks(String username) {
		return verificationRepository.countByProviderEmployee_usernameAndStatus(username, Status.RECEIVED);
	}

}
