package com.softserve.edu.service;

import com.softserve.edu.entity.user.User;
import com.softserve.edu.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Checks whereas user with {@code username} exist in database
     *
     * @param username must not be non {@literal null}
     * @return {@literal true} if user with {@code username} doesn't exist in database, else {@literal false}
     */
    public boolean existsWithUsername(String username) {
        return userRepository.findOne(username) == null;
    }

    /**
     * Changes user's password
     *
     * @param username user identification
     * @param oldPassword old password
     * @param newPassword new password
     * @return {@literal true} if changed, if not or passwords don't match - {@literal false}
     */
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        boolean changed = false;
        User user = userRepository.findOne(username);
        if (user != null) {
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            changed = passwordEncoder.matches(oldPassword, user.getPassword());
            if (changed) {
                user.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(user);
            }
        }
        return changed;
    }
}
