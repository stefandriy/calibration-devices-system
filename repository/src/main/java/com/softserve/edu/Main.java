package com.softserve.edu;

import com.softserve.edu.config.JPAConfig;
import com.softserve.edu.entity.enumeration.user.UserRole;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class Main {

    @Autowired
    private UserRepository userRepository;

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(JPAConfig.class);
        context.getBean(Main.class).testQuery();

    }

    public void testQuery() {
        userRepository.findByOrganizationId(43L, new PageRequest(0, 10))
                .forEach(System.out::println);
    }

    public void getAllAvailableUser () {
        List<User> userAvailable = userRepository.findAllAvailableUsersByRoleAndOrganizationId(UserRole.PROVIDER_EMPLOYEE, 43L)
                .stream()
                .collect(Collectors.toList());
        for (User user : userAvailable) {
            System.out.println(user);
        }
    }

    public void findByRoleByIgnoreCase () {
        List<User> userAvailable = userRepository.findByUserRoleAllIgnoreCase(UserRole.PROVIDER_EMPLOYEE)
                .stream()
                .collect(Collectors.toList());
        userAvailable.forEach(System.out::println);
    }

    public void findByUserRoleAndOrganizationId () {
        List<User> userAvailable = userRepository.findByUserRoleAndOrganizationId(UserRole.PROVIDER_EMPLOYEE, 43L)
                .stream()
                .collect(Collectors.toList());
        for (User user : userAvailable) {
            System.out.println(user);
        }
    }

    public void add() {
        User user = new User("dmytro", "pass");
        user.addRole(UserRole.CALIBRATOR_ADMIN);
        user.addRole(UserRole.PROVIDER_ADMIN);
        userRepository.save(user);
    }
}
