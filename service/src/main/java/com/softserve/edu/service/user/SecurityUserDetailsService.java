package com.softserve.edu.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.List;
import java.util.UUID;

@Service
public class SecurityUserDetailsService extends JdbcDaoImpl {

	public static final String DEF_USERS_BY_USERNAME_QUERY = "select username,password,isAvaliable, organizationId from user where username = ?";
	public static final String DEF_AUTHORITIES_BY_USERNAME_QUERY = "select u.username as username, ur.role as authority" +
			" from user u" +
			" join user_user_role uur on u.username = uur.username" +
			" join user_role ur on uur.id = ur.id where" +
			" u.username = ?";

	@Autowired
	private DataSource dataSource;

	public SecurityUserDetailsService() {
		super();
		setUsersByUsernameQuery(DEF_USERS_BY_USERNAME_QUERY);
		setAuthoritiesByUsernameQuery(DEF_AUTHORITIES_BY_USERNAME_QUERY);
	}

	@PostConstruct
	private void initialize() {
		setDataSource(dataSource);
	}

	@Override
	protected List<UserDetails> loadUsersByUsername(String username) {
		return this.getJdbcTemplate().query(getUsersByUsernameQuery(), new String[]{username}, (rs, rowNum) -> {
            String username1 = rs.getString(1);
            String password = rs.getString(2);
            boolean enabled = rs.getBoolean(3);
            Object organizationId = rs.getObject(4);
            return new CustomUserDetails(username1, password, enabled, (organizationId == null) ? null : ((Number) organizationId).longValue());
        });
	}

//	@Override
//	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//
//		User user = userRepository.findOne(username);
//
//		if ((user == null) || (user.getIsAvaliable() == null) || (!user.getIsAvaliable())) {
//			logger.error("Username " + username + " not found");
//			throw new UsernameNotFoundException("Username " + username + " not found");
//		}
//		List<GrantedAuthority> authorities = new ArrayList<>();
//		Set<UserRole> userRoles = user.getUserRoles();
//		String role = null;
//
//		for (UserRole userRole : userRoles) {
//
//			role = userRole.getRole();
//
//			authorities.add(new SimpleGrantedAuthority(role));
//		}
//
//		Long employeeOrganizationId = role.equals("SYS_ADMIN") ? null : ((User) user).getOrganization().getId();
//
//		return new CustomUserDetails(username, user.getPassword(), enabauthorities, employeeOrganizationId);
//
//	}

	/**
	 * Provide additional information about company(organization) where user
	 * works except SYS_ADMIN role.
	 */
	public static class CustomUserDetails extends org.springframework.security.core.userdetails.User {
		private static final long serialVersionUID = UUID.randomUUID().getMostSignificantBits();

		private Long organizationId;

		public CustomUserDetails(String username, String password, boolean enabled, Long organizationId) {
			super(username, password, enabled, true, true, true, AuthorityUtils.NO_AUTHORITIES);
			this.organizationId = organizationId;
		}

		public Long getOrganizationId() {
			return organizationId;
		}
	}
}
