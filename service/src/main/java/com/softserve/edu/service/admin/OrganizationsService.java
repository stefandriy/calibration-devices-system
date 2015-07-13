package com.softserve.edu.service.admin;

import com.softserve.edu.entity.*;
import com.softserve.edu.entity.user.Employee;

import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.util.OrganizationName;
import com.softserve.edu.repository.OrganizationRepository;
import com.softserve.edu.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;



@Service
public class OrganizationsService {

	@Autowired
	private OrganizationRepository organizationRepository;

	@Autowired
	private UserRepository userRepository;

	@Transactional
	public void addOrganizationWithAdmin(String name, String email,
			String phone, OrganizationName type, String username, String password,
			Address address) {
		String passwordEncoded = new BCryptPasswordEncoder().encode(password);
		Organization organization;
		Employee employeeAdmin;
//		organization.setAddress(address);
//		organizationRepository.save(organization);
//		userRepository.save(employeeAdmin);
	}

	@Transactional
	public Page<Organization> getOrganizationsBySearchAndPagination(
			int pageNumber, int itemsPerPage, String search) {
		/* pagination starts from 1 at client side, but Spring Data JPA from 0 */
		PageRequest pageRequest = new PageRequest(pageNumber - 1, itemsPerPage);
		return search == null ? organizationRepository.findAll(pageRequest)
				: organizationRepository.findByNameLikeIgnoreCase("%" + search
						+ "%", pageRequest);
	}

	@Transactional(readOnly = true)
	public Organization getOrganizationById(Long id) {
		return organizationRepository.findOne(id);
	}

	@Transactional
	public String getOrganizationType(Organization organization) {
		return null;
	}

	@Transactional
	public void editOrganization(Long organizationId, String name,
			String phone, String email, Address address) {
		Organization organization = organizationRepository
				.findOne(organizationId);
		organization.setName(name);
		organization.setPhone(phone);
		organization.setEmail(email);
		organization.setAddress(address);
	}
}
