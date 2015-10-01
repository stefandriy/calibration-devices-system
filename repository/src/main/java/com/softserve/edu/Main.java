package com.softserve.edu;

import com.softserve.edu.config.JPAConfig;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.enumeration.user.UserRole;
import com.softserve.edu.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class Main {

    @Autowired
    private UserRepository userRepository;

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(JPAConfig.class);
        context.getBean(Main.class).testQuery();
    }

    public void testQuery() {
    }

    public void add() {
        User user = new User("dmytro", "pass");
        user.addRole(UserRole.CALIBRATOR_ADMIN);
        user.addRole(UserRole.PROVIDER_ADMIN);
        userRepository.save(user);
    }
}
