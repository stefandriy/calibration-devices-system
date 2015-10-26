package com.softserve.edu.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
//@Import(JPATestingConfig.class)
@ComponentScan("com.softserve.edu")
public class ServiceTestingConfig {}

