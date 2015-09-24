package com.softserve.edu;

import com.softserve.edu.config.JPAConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        new AnnotationConfigApplicationContext(JPAConfig.class);
    }
}
