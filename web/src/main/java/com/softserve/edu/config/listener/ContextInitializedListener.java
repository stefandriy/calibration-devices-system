package com.softserve.edu.config.listener;

import com.softserve.edu.entity.enumeration.user.UserRole;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;


/**
 *Application event listener class handles ContextRefreshedEvent
 */
@Component
public class ContextInitializedListener implements ApplicationListener<ContextRefreshedEvent>{

    private static final String SUPER_USER_LOGIN = "super";

    @Autowired
    UserService userService;

    /**
     * Handle an application event {@param ContextRefreshedEvent}
     * Create new user with role SUPER_ADMIN if that user not exists
     *
     * @param event raised when an {@code ApplicationContext} gets initialized or refreshed.
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        User user = new User(SUPER_USER_LOGIN, new BCryptPasswordEncoder().encode(SUPER_USER_LOGIN));
        user.addRole(UserRole.SUPER_ADMIN);
        user.setIsAvailable(true);

        userService.createSuperAdminIfNotExists(user);

    }

}
