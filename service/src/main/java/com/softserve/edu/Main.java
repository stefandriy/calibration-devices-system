package com.softserve.edu;

import com.softserve.edu.config.ServiceConfig;
import com.softserve.edu.entity.Address;
import com.softserve.edu.entity.Organization;
import com.softserve.edu.entity.OrganizationType;
import com.softserve.edu.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class Main {

    @Autowired
    private OrganizationRepository organizationRepository;

    public static void main(String[] args) {
        new AnnotationConfigApplicationContext(ServiceConfig.class).getBean(Main.class).test();
    }

    public void test() {
        Organization organization = new Organization("Company", "email", "phone", 20, 20,
                new Address(
                    "Lviv",
                    "Lviv",
                    "Lviv",
                    "Lviv",
                    "Lviv",
                    "Lviv"
                )
        );
        organization.getOrganizationTypes().add(OrganizationType.PROVIDER);
        organization.getOrganizationTypes().add(OrganizationType.CALIBRATOR);

        organizationRepository.save(organization);

        System.out.println(organizationRepository.findByTypeAndDistrict(OrganizationType.CALIBRATOR.name(), "Lviv"));
    }
}
