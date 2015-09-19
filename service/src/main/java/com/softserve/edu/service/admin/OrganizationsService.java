package com.softserve.edu.service.admin;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;

import com.softserve.edu.entity.AddEmployeeBuilderNew;
import com.softserve.edu.service.provider.ProviderEmployeeService;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softserve.edu.entity.Address;
import com.softserve.edu.entity.Organization;
import com.softserve.edu.entity.OrganizationType;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.user.UserRole;
import com.softserve.edu.repository.OrganizationRepository;
import com.softserve.edu.repository.UserRepository;
import com.softserve.edu.service.utils.ArchivalOrganizationsQueryConstructorAdmin;
import com.softserve.edu.service.utils.ListToPageTransformer;

@Service
public class OrganizationsService {

	private final Logger logger = Logger.getLogger(OrganizationsService.class);
	
	@Autowired
	private OrganizationRepository organizationRepository;

	@Autowired
	private ProviderEmployeeService providerEmployeeService;

	@Autowired
	private UserRepository userRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Transactional
	public void addOrganizationWithAdmin(String name, String email, String phone, String[] types, Integer employeesCapacity,
										 Integer maxProcessTime, String firstName, String lastName, String middleName,
										 String username, String password, Address address) {

		Organization organization = new Organization(name, email, phone, employeesCapacity, maxProcessTime, address);
		String passwordEncoded = new BCryptPasswordEncoder().encode(password);
		User employeeAdmin = new User(firstName, lastName, middleName, username, passwordEncoded, organization);

		for (String strType : types) {
			OrganizationType type = organizationRepository
					.getOrganizationType(strType);
			organization.addOrganizationType(type);
			String strRole = strType + "_ADMIN";
			UserRole userRole = userRepository.getUserRole(strRole);
			employeeAdmin.addUserRole(userRole);
		}

		organizationRepository.save(organization);
		userRepository.save(employeeAdmin);
}

/*	@Transactional
	public Page<Organization> getOrganizationsBySearchAndPagination(
			int pageNumber, int itemsPerPage, String search/*, String adress, String type ) {
		/* pagination starts from 1 at client side, but Spring Data JPA from 0
		PageRequest pageRequest = new PageRequest(pageNumber - 1, itemsPerPage);
		return search == null ? organizationRepository.findAll(pageRequest)
				: organizationRepository.findByNameLikeIgnoreCase("%" + search
						+ "%", pageRequest);
	}*/

	@Transactional(readOnly = true)
	public ListToPageTransformer<Organization> getOrganizationsBySearchAndPagination(int pageNumber, int itemsPerPage,String name,
																					 String email, String number, String type, String region, String district, String locality, String streetToSearch, String sortCriteria, String sortOrder) {

		CriteriaQuery<Organization> criteriaQuery = ArchivalOrganizationsQueryConstructorAdmin.buildSearchQuery(name, email, number, type, region, district, locality, streetToSearch, sortCriteria, sortOrder, entityManager);

		Long count = entityManager.createQuery(ArchivalOrganizationsQueryConstructorAdmin.buildCountQuery(name, email, number, type, region, district, locality, streetToSearch, sortCriteria, sortOrder, entityManager)).getSingleResult();

		TypedQuery<Organization> typedQuery = entityManager.createQuery(criteriaQuery);
		typedQuery.setFirstResult((pageNumber - 1) * itemsPerPage);
		typedQuery.setMaxResults(itemsPerPage);
		List<Organization> OrganizationList = typedQuery.getResultList();

		ListToPageTransformer<Organization> result = new ListToPageTransformer<Organization>();
		result.setContent(OrganizationList);
		result.setTotalItems(count);
		return result;
	}
	@Transactional
	public Organization getOrganizationById(Long id) {
		 return organizationRepository.findOne(id);
	}

	@Transactional
	public Set<String> getOrganizationTypes(Organization organization) {
		Long id = organization.getId();
		return organizationRepository.getOrganizationTypesById(id);
	}


	@Transactional(readOnly = true, propagation= Propagation.SUPPORTS)
	public void editOrganization(Long organizationId, String name,
								 String phone, String email, String[] types, Integer employeesCapacity, Integer maxProcessTime, Address address, String password, String username,  String firstName, String lastName, String middleName) {
		Organization organization = organizationRepository
				.findOne(organizationId);
		logger.debug(organization);

		organization.setName(name);
		organization.setPhone(phone);
		organization.setEmail(email);
		organization.setEmployeesCapacity(employeesCapacity);
		organization.setMaxProcessTime(maxProcessTime);
		organization.setAddress(address);
		HashSet<OrganizationType> organizationTypes = new HashSet<OrganizationType>();

		for (String strType : types) {
			OrganizationType type = organizationRepository
					.getOrganizationType(strType);
			organizationTypes.add(type);
		}
		for (OrganizationType type : organizationTypes){
			logger.debug(type);
		}
		organization.setOrganizationTypes(organizationTypes);

		User employeeAdmin = userRepository.getUserByUserName(username);
        logger.info("==========employeeAdmin=============");
        logger.info(employeeAdmin);
			employeeAdmin.setFirstName(firstName);
			employeeAdmin.setLastName(lastName);
			employeeAdmin.setMiddleName(middleName);

			employeeAdmin.setPassword(password != null && password.equals("generate") ?
					"generate" : employeeAdmin.getPassword());


			providerEmployeeService.updateEmployee(employeeAdmin);
			userRepository.save(employeeAdmin);

		organizationRepository.save(organization);


	}

	@Transactional
	public Integer getOrganizationEmployeesCapacity(Long organizationId) {
		return organizationRepository.getOrganizationEmployeesCapacity(organizationId);
	}

	@Transactional
	public Set<String> getDeviceTypesByOrganization(Long organizationId){
		return organizationRepository.getDeviceTypesByOrganization(organizationId);
	}

	@Transactional
	public Set<String> getOrganizationTypesById( Long id){
	 	return 	organizationRepository.getOrganizationTypesById(id);
	}

}
