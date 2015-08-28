package com.softserve.edu.service.admin;

import com.softserve.edu.repository.UserRepository;
import com.softserve.edu.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsersService {

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
    public boolean existsWithUsername(String username) {
        return userRepository.findOne(username) == null;
    }

    @Transactional
    public List<String> getRoles(String username){
        return userRoleRepository.getRoles(username);
    }

}
