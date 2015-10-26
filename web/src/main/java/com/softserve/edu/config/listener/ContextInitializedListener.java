package com.softserve.edu.config.listener;


import com.softserve.edu.entity.enumeration.user.UserRole;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;


@Component
public class ContextInitializedListener implements ApplicationListener<ContextRefreshedEvent>{


    static final String password = "$2a$10$59Mv7tEUrVH8iBeDsm9y7.zUcJoPHnnyOvMnC4zKRV8.wlnugQ2G2";
    static final String username = "Super_Admin";

    @Autowired
    UserService userService;


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        User user = new User(username, password);
        user.addRole(UserRole.SUPER_ADMIN);
        user.setIsAvailable(true);

        userService.createSuperAdminIfNotExists(user);


    }
}
