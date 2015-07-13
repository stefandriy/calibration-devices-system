package com.softserve.edu.service.verification;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;

import com.softserve.edu.entity.*;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.util.ReadStatus;
import com.softserve.edu.entity.util.Status;
import com.softserve.edu.repository.CalibrationTestRepository;
import com.softserve.edu.repository.VerificationRepository;
import com.softserve.edu.service.exceptions.NotAvailableException;
import com.softserve.edu.service.utils.ListToPageTransformer;
import com.softserve.edu.service.utils.QueryConstructor;

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

	/**
	 * Finds count of rows in database for verifications assigned to Calibrator with Read Status = 'UNREAD'.
	 * Method is used for notification about unwatched verifications
	 * 
	 * @param calibratorId
	 * @return 
	 */
	@Transactional(readOnly = true)
	public Long findCountOfNewVerificationsByCalibratorId(Long calibratorId) {
		return verificationRepository.countByCalibratorIdAndStatusAndReadStatus(calibratorId, Status.IN_PROGRESS,
				ReadStatus.UNREAD);
	}
	
	/**
	 * Finds count of rows in database for verifications assigned to Provider with Read Status = 'UNREAD'.
	 * Method is used for notification about unwatched verifications
	 * 
	 * @param providerId
	 * @return 
	 */
//	@Transactional(readOnly = true)
//	public Long findCountOfNewVerificationsByProviderId(Long providerId) {
//		System.err.println();
//		return verificationRepository.countByProviderIdAndStatusAndReadStatus(providerId, Status.SENT,
//				ReadStatus.UNREAD);
//	}
	
	/**
	 * Finds count of rows in database for verifications assigned to State Verificator with Read Status = 'UNREAD'.
	 * Method is used for notification about unwatched verifications
	 * 
	 * @param stateVerificatorId
	 * @return 
	 */
	@Transactional(readOnly = true)
	public Long findCountOfNewVerificationsByStateVerificatorId(Long stateVerificatorId) {
		return verificationRepository.countByStateVerificatorIdAndStatusAndReadStatus(stateVerificatorId, Status.SENT_TO_VERIFICATOR,
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
		return verificationRepository.findByCalibratorIdAndStatusOrderByInitialDateDesc(calibratorId, Status.IN_PROGRESS,
				pageRequest);
	}

	@Transactional(readOnly = true)
	public Page<Verification> findPageOfSentVerificationsByCalibratorIdAndSearch(Long calibratorId, int pageNumber,
			int itemsPerPage, String searchType, String searchText) {
		Pageable pageRequest = new PageRequest(pageNumber - 1, itemsPerPage);
		switch (searchType) {
		case "id":
			return verificationRepository.findByCalibratorIdAndStatusAndIdLikeIgnoreCase(calibratorId, Status.IN_PROGRESS,
					"%" + searchText + "%", pageRequest);

		case "lastName":
			return verificationRepository.findByCalibratorIdAndStatusAndClientData_lastNameLikeIgnoreCase(calibratorId,
					Status.IN_PROGRESS, "%" + searchText + "%", pageRequest);

		case "street":
			return verificationRepository.findByCalibratorIdAndStatusAndClientDataClientAddressStreetLikeIgnoreCase(
					calibratorId, Status.IN_PROGRESS, "%" + searchText + "%", pageRequest);

		case "date":

			SimpleDateFormat form = new SimpleDateFormat("dd-MM-yyyy");
			Date date = null;
			try {
				date = form.parse(searchText);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return verificationRepository.findByCalibratorIdAndStatusAndInitialDateLike(calibratorId, Status.IN_PROGRESS,
					date, pageRequest);

		default:
			return verificationRepository.findByCalibratorIdAndStatusOrderByInitialDateDesc(calibratorId,
					Status.IN_PROGRESS, pageRequest);
		}

	}

	@Transactional(readOnly = true)
	public Page<Verification> findPageOfSentVerificationsByStateVerificatorId(Long stateVerificatorId, int pageNumber,
			int itemsPerPage) {
		Pageable pageRequest = new PageRequest(pageNumber - 1, itemsPerPage);
		return verificationRepository.findByStateVerificatorIdAndStatus(stateVerificatorId, Status.SENT_TO_VERIFICATOR,
				pageRequest);
	}

	/**
	 * Find page of new verifications for provider with search parameters specified
	 * 
	 * @param providerId  
	 * 		ID of organization
	 * @param pageNumber
	 * 		number of page requested by user
	 * @param itemsPerPage
	 * 		desired number of rows to be displayed on page
	 * @param dateToSearch
	 * 		 search by initial date of verification
	 * @param idToSearch
	 * 		search by verification ID
	 * @param lastNameToSearch
	 * 		search by last name of client
	 * @param streetToSearch
	 * 		search by street where client lives
	 * @param providerEmployee
	 * 		restrict query by provider employee user name. Allows restrict query so that simple employee user
	 * 		can only see verifications assigned to him and free verifications (not yet assigned)
	 * @return ListToPageTransformer<Verification>
	 */
	@Transactional(readOnly = true)
	 public ListToPageTransformer<Verification> findPageOfSentVerificationsByProviderIdAndCriteriaSearch(Long providerId,
			 			int pageNumber, int itemsPerPage, String dateToSearch, String idToSearch, String lastNameToSearch,
			 														String streetToSearch, User providerEmployee) {

		CriteriaQuery<Verification> criteriaQuery = QueryConstructor.buildSearchQuery(providerId, dateToSearch, idToSearch,
			  																	lastNameToSearch, streetToSearch, providerEmployee, em);
  
		Long count = em.createQuery(QueryConstructor.buildCountQuery(providerId, dateToSearch, idToSearch, lastNameToSearch,
			  												streetToSearch, providerEmployee, em)).getSingleResult();
 
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
	 * verification, set verification read status to 'UNREAD', 
	 *  save verification
	 */
//	@Transactional
//	public void updateVerification(String verificationId, Organization calibrator) {
//		Verification verification = verificationRepository.findOne(verificationId);
//		if (verification == null) {
//			logger.error("verification haven't found");
//			return;
//		}
//		verification.setStatus(Status.IN_PROGRESS);
//		verification.setCalibrator(calibrator);
//		verification.setReadStatus(ReadStatus.UNREAD);
//		verificationRepository.save(verification);
//	}

	@Transactional
	public void assignProviderEmployee(String verificationId, User providerEmployee) {
		Verification verification = verificationRepository.findOne(verificationId);
		if (verification == null) {
			logger.error("verification haven't found");
			return;
		}
		verification.setProviderEmployee(providerEmployee);
		verification.setReadStatus(ReadStatus.READ);
		verificationRepository.save(verification);
	}

	
	/**
	 * Changes verification read status to 'READ' when Provider or Calibrator or State Verificator reads it
	 * @param verificationId
	 * @param readStatus
	 */
	@Transactional
	public void updateVerificationReadStatus(String verificationId, String readStatus) {
		Verification verification = verificationRepository.findOne(verificationId);
		System.err.println("INSIDEservice!!!");
		if (verification == null) {
			logger.error("verification haven't found");
			return;
		}
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
	public void updateVerificationByCalibrator(String verificationId, Organization stateVerificator) {
		Verification verification = verificationRepository.findOne(verificationId);
		verification.setStatus(Status.SENT_TO_VERIFICATOR);
		verification.setStateVerificator(stateVerificator);
		verification.setReadStatus(ReadStatus.UNREAD);
		verificationRepository.save(verification);
	}
	
	@Transactional
	public void updateVerificationStatus (String verificationId, Status status) {
		Verification verification = verificationRepository.findOne(verificationId);
		verification.setStatus(status);
		verificationRepository.save(verification);
	}
	
	@Transactional
	public void sendVerificationTo(String verificationId, Organization oraganization, Status status) {
		Verification verification = verificationRepository.findOne(verificationId);
		String organizationName = oraganization.getClass().getSimpleName();
			if (organizationName.equals(Organization.class.getSimpleName())) {
				verification.setProvider((Organization) oraganization);
			} else if (organizationName.equals(Organization.class.getSimpleName())) {
				verification.setCalibrator((Organization) oraganization);
			} else if (organizationName.equals(Organization.class.getSimpleName())) {
				verification.setStateVerificator((Organization) oraganization);
			}
		verification.setStatus(status);
		verification.setReadStatus(ReadStatus.UNREAD);
		verificationRepository.save(verification);
	}
	
	
	/**
	 * Find verification, add complete status to stateVerificator, add
	 * stateVerificator to verification save verification
	 */
	@Transactional
	public void updateVerification(String verificationId, Organization stateVerificator) {
		Verification verification = verificationRepository.findOne(verificationId);
		if (verification == null) {
			logger.error("verification haven't found");
			return;
		}
		verification.setStatus(Status.TEST_OK);
		verification.setStateVerificator(stateVerificator);
		verificationRepository.save(verification);
	}


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
		return verificationRepository.countByProviderEmployee_usernameAndStatus(username, Status.SENT);
	}

}
