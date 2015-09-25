package com.softserve.edu;

import com.softserve.edu.config.JPAConfig;
import com.softserve.edu.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Component
public class Main {

    @Autowired
    private UserRepository userRepository;

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(JPAConfig.class);
        context.getBean(Main.class).test();
    }

    public void test() {
        userRepository
                .findByOrganizationId(43L, new PageRequest(0, 10))
                .forEach(System.out::println);
    }
}
