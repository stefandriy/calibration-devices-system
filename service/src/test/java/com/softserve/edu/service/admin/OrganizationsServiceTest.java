package com.softserve.edu.service.admin;

import com.softserve.edu.entity.Address;
import com.softserve.edu.entity.Organization;
import com.softserve.edu.entity.OrganizationType;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.user.UserRole;
import com.softserve.edu.repository.OrganizationRepository;
import com.softserve.edu.repository.UserRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.mockito.internal.util.StringJoiner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Created by Dmytro on 8/20/2015.
 */
public class OrganizationsServiceTest {

    @InjectMocks
    private OrganizationsService organizationsService;

    @Mock
    private OrganizationRepository mockOrganizationRepository;

    @Mock
    private UserRepository mockUserRepository;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddOrganizationWithAdmin() throws Exception {
        final String name = "Eric";
        final String email = "dot@gmail.com";
        final String phone = "0934453246";
        final String[] types = {"asfdasf", "asdasda"};
        final Integer employeesCapacity = 12;
        final Integer maxProcessTime = 123;
        final String username = "eric";
        final String password = "root";
        final Address address = new Address("Lviv", "Leva", "123", "123", "123", "123");

        Organization spyOrg = spy(new Organization(name, email, phone, employeesCapacity, maxProcessTime, address));
        String passwordEncoded = new BCryptPasswordEncoder().encode(password);
        User spyUser = spy(new User(username,passwordEncoded, spyOrg));
        UserRole userRole = new UserRole("admin");
        OrganizationType typeOne = new OrganizationType();
        OrganizationType typeTwo = new OrganizationType();
        typeOne.setId(5);
        typeOne.setType("calibrator");
        typeTwo.setId(6);
        typeTwo.setType("provider");

        when(mockOrganizationRepository.getOrganizationType(anyString())).thenReturn(typeOne).thenReturn(typeTwo);
        doNothing().when(spyOrg).addOrganizationType(any());
        when(mockUserRepository.getUserRole(anyString())).thenReturn(userRole);
        doNothing().when(spyUser).addUserRole(userRole);

        organizationsService.addOrganizationWithAdmin(name, email, phone, types, employeesCapacity, maxProcessTime,
                username, password, address);

        ArgumentCaptor<Organization> organizationArg = ArgumentCaptor.forClass(Organization.class);
        ArgumentCaptor<User> employeeAdminArg = ArgumentCaptor.forClass(User.class);

        verify(mockOrganizationRepository).save(organizationArg.capture());
        verify(mockUserRepository).save(employeeAdminArg.capture());

        assertEquals(userRole, mockUserRepository.getUserRole(anyString()));
        assertEquals(typeTwo, mockOrganizationRepository.getOrganizationType(anyString()));
        assertEquals(spyOrg.getName(), organizationArg.getValue().getName());
        assertEquals(spyUser.getUsername(), employeeAdminArg.getValue().getUsername());



    }

    @Test
    public void testGetOrganizationsBySearchAndPagination() throws Exception {

    }

    @Test
    public void testGetOrganizationById() throws Exception {

    }

    @Test
    public void testGetOrganizationTypes() throws Exception {

    }

    @Test
    public void testEditOrganization() throws Exception {

    }

    @Test
    public void testGetOrganizationEmployeesCapacity() throws Exception {

    }
}