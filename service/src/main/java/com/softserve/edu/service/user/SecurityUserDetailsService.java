package com.softserve.edu.service.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.user.UserRole;
import com.softserve.edu.repository.UserRepository;

@Service
public class SecurityUserDetailsService implements UserDetailsService {

	private final Logger logger = Logger.getLogger(SecurityUserDetailsService.class);

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = userRepository.findOne(username);

		if ((user == null) || (user.getIsAvaliable() == null) || (!user.getIsAvaliable())) {
			logger.error("Username " + username + " not found");
			throw new UsernameNotFoundException("Username " + username + " not found");
		}
		List<GrantedAuthority> authorities = new ArrayList<>();
		Set<UserRole> userRoles = user.getUserRoles();
		String role = null;

		for (UserRole userRole : userRoles) {

			role = userRole.getRole();

			authorities.add(new SimpleGrantedAuthority(role));
		}

		Long employeeOrganizationId = role.equals("SYS_ADMIN") ? null : ((User) user).getOrganization().getId();

		return new CustomUserDetails(username, user.getPassword(), authorities, employeeOrganizationId);

	}

	/**
	 * Provide additional information about company(organization) where user
	 * works except SYS_ADMIN role.
	 */
	public static class CustomUserDetails extends org.springframework.security.core.userdetails.User {
		private static final long serialVersionUID = UUID.randomUUID().getMostSignificantBits();

		private Long organizationId;

		public CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities,
				Long organizationId) {
			super(username, password, authorities);

			this.organizationId = organizationId;

		}

		public Long getOrganizationId() {
			return organizationId;
		}
	}
}
