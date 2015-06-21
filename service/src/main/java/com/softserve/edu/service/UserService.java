package com.softserve.edu.service;

import com.softserve.edu.entity.user.Employee;
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
     * Changes given type of user's name
     *
     * @param username must not be non {@literal null}
     * @param newName new name to change
     * @param type (first, last or middle name)
     * @return {@literal true} if changed, if not or passwords don't match - {@literal false}
     */
    public boolean changeName(String username, String newName, String type) {
        boolean isChanged = false;
        if (type != null && username != null && newName != null) {
            User user = userRepository.findOne(username);
            if (user instanceof Employee) {
                Employee employee = (Employee) user;
                switch (type) {
                    case "firstName":
                        employee.setFirstName(newName);
                        isChanged = true;
                        break;
                    case "lastName":
                        employee.setLastName(newName);
                        isChanged = true;
                        break;
                    case "middleName":
                        employee.setMiddleName(newName);
                        isChanged = true;
                        break;
                }
                if (isChanged) {
                    userRepository.save(employee);
                }
            }
        }
        return isChanged;
    }

    /**
     * Changes user's password
     *
     * @param username must not be non {@literal null}
     * @param oldPassword old password
     * @param newPassword new password
     * @return {@literal true} if changed, if not or passwords don't match - {@literal false}
     */
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        boolean isChanged = false;
        if (username != null && oldPassword != null && newPassword != null) {
            User user = userRepository.findOne(username);
            if (user != null) {
                PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                isChanged = passwordEncoder.matches(oldPassword, user.getPassword());
                if (isChanged) {
                    user.setPassword(passwordEncoder.encode(newPassword));
                    userRepository.save(user);
                }
            }
        }
        return isChanged;
    }
}
