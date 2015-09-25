package com.softserve.edu.service.admin.impl;

import com.softserve.edu.entity.user.User;
import com.softserve.edu.repository.UserRepository;
import com.softserve.edu.repository.UserRoleRepository;
import com.softserve.edu.service.admin.UserService;
import org.apache.commons.collections.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsersServiceImpl implements UserService  {

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private UserRoleRepository userRoleRepository;
    /**
     * Check whereas user with {@code username} exist in database
     *
     * @param username must not be non {@literal null}
     * @return {@literal true} if user with {@code username} doesn't exist in
     * database, else {@literal false}
     */
    @Override
    public boolean existsWithUsername(String username) {
        return userRepository.findOne(username) == null;
    }

    @Override
    @Transactional
    public List<String> getRoles(String username){
        return userRoleRepository.getRoles(username);
    }


    @Override
    @Transactional
    public User getUserByRoleAndOrganization(String role, Long organizationId){
        return userRepository.findByRoleAndOrganizationId(role, organizationId);
    }

    @Override
    @Transactional
    public void addEmployee(User user) {
        String passwordEncoded = new BCryptPasswordEncoder().encode(user.getPassword());
        user.setPassword(passwordEncoded);
        userRepository.save(user);
    }

    @Override
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<User> findByOrganizationId(Long organizationId, int pageNumber, int itemsPerPage) {
        return IteratorUtils.toList(
                userRepository.findByOrganizationId(organizationId, new PageRequest(pageNumber, itemsPerPage)).iterator()
        );
    }
}
