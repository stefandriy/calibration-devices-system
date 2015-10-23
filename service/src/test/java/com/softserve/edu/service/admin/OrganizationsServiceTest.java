package com.softserve.edu.service.admin;


import com.softserve.edu.config.ServiceTestingConfig;
import com.softserve.edu.entity.Address;
import com.softserve.edu.entity.catalogue.util.LocalityDTO;
import com.softserve.edu.entity.device.Device;
import com.softserve.edu.entity.enumeration.organization.OrganizationType;
import com.softserve.edu.entity.organization.Organization;
import com.softserve.edu.service.utils.TypeConverter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ServiceTestingConfig.class})
public class OrganizationsServiceTest {

    @Autowired
    private OrganizationService organizationsService;


    @Test
    public void testFindOrganizationByLocalityId() {
        List<Organization> list = organizationsService.findAllByLocalityId(116031L);
        assertTrue(!list.isEmpty());
    }

    @Test
    public void testFindOrganizationByLocalityIdAndTypeId() {
        List<Organization> list = organizationsService.findAllByLocalityIdAndTypeId(116031L, OrganizationType.PROVIDER);
        assertTrue(!list.isEmpty());
    }

    @Test
    public void testFindOrganizationTypesById() {
        Set<OrganizationType> list = organizationsService.findOrganizationTypesById(1L);

        Set<String> testlist = TypeConverter.enumToString(list);
        list = TypeConverter.stringToEnum(testlist,OrganizationType.class);

        assertTrue(!list.isEmpty());
    }

    @Test
    public void testFindByLocalityIdAndTypeAndDevice() {
        List<Organization> list = organizationsService.findByLocalityIdAndTypeAndDevice(116031L, OrganizationType.PROVIDER, Device.DeviceType.WATER);

        assertTrue(!list.isEmpty());
    }

    @Test
    public void testFindLocalitiesByOrganizationId() {
        List<LocalityDTO> list = organizationsService.findLocalitiesByOrganizationId(1L);
        assertTrue(!list.isEmpty());
    }

    @Test
    public void testAddOrganization() {
        final String OrgName = "name";
        final String mname = "mname";
        final String phone = "99999999";
        final String email = "email";
        final List<String> types = new ArrayList<String>();
        final List<String> counters = new ArrayList<String>();
        types.add("PROVIDER");
        counters.add("WATER");
        final Integer employeesCapacity = 13;
        final Integer maxProcessTime = 456;
        final Address address = new Address("Lviv", "Leva", "123", "123", "123", "123");
        final Long[] localities = {116031L};
        final String username = "eric123";
        final String password = "root";

        organizationsService.addOrganizationWithAdmin(OrgName, email, phone, types, counters, employeesCapacity, maxProcessTime,
                username, mname, mname, mname, password, address, username, localities);
        assertTrue(true);
    }
    @Test
    public void testEditOrganization() {
        final String name = "name";
        final String mname = "mname";
        final String phone = "phone";
        final String email = "email";
        final List<String> types = new ArrayList<String>();
        final List<String> counters = new ArrayList<String>();
        final List<String> capacity = new ArrayList<String>();
        types.add("CALIBRATOR");
        types.add("PROVIDER");
        counters.add("WATER");
        capacity.add("");
        final Integer employeesCapacity = 123;
        final Integer maxProcessTime = 456;
        final Address address = new Address("Lviv", "Leva", "123", "123", "123", "123");
        final List<Long> localities = new ArrayList<>();
        localities.add(6L);
        localities.add(10L);
        localities.add(11L);
        localities.add(15L);
        final String username = "eric";
        final String password = "root";

        organizationsService.editOrganization(54L, name, email, phone, types, counters, employeesCapacity, maxProcessTime, address,
                username, mname, mname, mname, password,  username, localities);

        assertTrue(true);
    }



    /*   @InjectMocks
    private OrganizationService organizationsService;

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
        final int pageNumber = 5;
        final int itemsPerPage = 10;
        final String search = "calib";
        final Page<Organization> pageOrganizations = (Page<Organization>) mock(Page.class);
        PageRequest pageRequest = new PageRequest(pageNumber - 1, itemsPerPage);

        when(mockOrganizationRepository.findAll(pageRequest)).thenReturn(pageOrganizations);
        when(mockOrganizationRepository.findByNameLikeIgnoreCase("%" + search
                + "%", pageRequest)).thenReturn(pageOrganizations);

        ArgumentCaptor<PageRequest> pageRequestArg = ArgumentCaptor.forClass(PageRequest.class);


        organizationsService.getOrganizationsBySearchAndPagination(pageNumber, itemsPerPage, null);

        verify(mockOrganizationRepository).findAll(pageRequestArg.capture());
        assertEquals(pageRequest.first(), pageRequestArg.getValue().first());
        assertEquals(pageOrganizations, organizationsService
                .getOrganizationsBySearchAndPagination(pageNumber, itemsPerPage, null));

        ArgumentCaptor<PageRequest> pageRequestArg2 = ArgumentCaptor.forClass(PageRequest.class);

        organizationsService.getOrganizationsBySearchAndPagination(pageNumber, itemsPerPage, search);

        verify(mockOrganizationRepository).findByNameLikeIgnoreCase(anyString(), pageRequestArg2.capture());
        assertEquals(pageRequest.first(), pageRequestArg.getValue().first());
        assertEquals(pageRequest.first(), pageRequestArg2.getValue().first());
        assertEquals(pageOrganizations, organizationsService
                .getOrganizationsBySearchAndPagination(pageNumber, itemsPerPage, search));

    }

    @Test
    public void testGetOrganizationById() throws Exception {
        final Long id = 100000L;
        final Organization organization = mock(Organization.class);

        when(mockOrganizationRepository.findOne(id)).thenReturn(organization);
        ArgumentCaptor<Long> idArg = ArgumentCaptor.forClass(Long.class);

        organizationsService.getOrganizationById(id);

        assertEquals(organization, organizationsService.getOrganizationById(id));

    }

    @Test
    public void testGetOrganizationTypes() throws Exception {
        final Long id = 1L;
        final Organization organization = mock(Organization.class);
        final Set<String> mockedSet = mock(Set.class);
        ArgumentCaptor<Long> idArg = ArgumentCaptor.forClass(Long.class);

        when(organization.getId()).thenReturn(id);
        when(mockOrganizationRepository.getOrganizationTypesById(anyLong())).thenReturn(mockedSet);

        organizationsService.getOrganizationTypes(organization);

        verify(mockOrganizationRepository).getOrganizationTypesById(idArg.capture());
        assertEquals(id, idArg.getValue());
        assertEquals(mockedSet, organizationsService.getOrganizationTypes(organization));

    }

    @Ignore
    @Test
    public void testEditOrganization() throws Exception {
        final Long organizationId = 1L;
        final String name = "name";
        final String phone = "phone";
        final String email = "email";
        final String[] types = {"CALIBRATOR", "PROVIDER"};
        final Integer employeesCapacity = 123;
        final Integer maxProcessTime = 456;
        final Address address = new Address("Lviv", "Leva", "123", "123", "123", "123");
        final Organization organization = mock(Organization.class);

        when(mockOrganizationRepository.findOne(organizationId)).thenReturn(organization);

        ArgumentCaptor<String> nameArg = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> phoneArg = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> emailArg = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Integer> employeesCapacityArg = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> maxProcessTimeArg = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Address> addressArg = ArgumentCaptor.forClass(Address.class);

        organizationsService.editOrganization(organizationId, name, phone, email, types,
                employeesCapacity, maxProcessTime, address);

        verify(organization).setName(nameArg.capture());
        verify(organization).setPhone(phoneArg.capture());
        verify(organization).setEmail(emailArg.capture());

        verify(organization).setEmployeesCapacity(employeesCapacityArg.capture());
        verify(organization).setMaxProcessTime(maxProcessTimeArg.capture());
        verify(organization).setAddress(addressArg.capture());

        assertEquals(name, nameArg.getValue());
        assertEquals(phone, phoneArg.getValue());
        assertEquals(email, emailArg.getValue());
        assertEquals(employeesCapacity, employeesCapacityArg.getValue());
        assertEquals(maxProcessTime, maxProcessTimeArg.getValue());
        assertEquals(address.getBuilding(), addressArg.getValue().getBuilding());
    }

    @Test
    public void testGetOrganizationEmployeesCapacity() throws Exception {
        final Long organizationId = 1L;
        Integer someInt = 123;
        when(mockOrganizationRepository.getOrganizationEmployeesCapacity(organizationId)).thenReturn(someInt);

        ArgumentCaptor<Long> organizationIdArg = ArgumentCaptor.forClass(Long.class);

        organizationsService.getOrganizationEmployeesCapacity(organizationId);

        verify(mockOrganizationRepository).getOrganizationEmployeesCapacity(organizationIdArg.capture());

        assertEquals(organizationId, organizationIdArg.getValue());
        assertEquals(someInt, organizationsService.getOrganizationEmployeesCapacity(organizationId));
    }
    */
}