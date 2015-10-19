package com.softserve.edu.service.admin.impl;

import com.softserve.edu.entity.Address;
import com.softserve.edu.entity.catalogue.Locality;
import com.softserve.edu.entity.enumeration.device.DeviceType;
import com.softserve.edu.entity.enumeration.organization.OrganizationType;
import com.softserve.edu.entity.organization.Organization;
import com.softserve.edu.entity.organization.OrganizationChangesHistory;
import com.softserve.edu.repository.OrganizationChangesHistoryRepository;
import com.softserve.edu.repository.OrganizationRepository;
import com.softserve.edu.service.catalogue.LocalityService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.stub;

/**
 * Created by cam on 19.10.15.
 */
public class OrganizationServiceImplTest {
    private final Long organizationId = 1L;
    private final Long localityId = 2L;
    private final OrganizationType orgType = OrganizationType.CALIBRATOR;
    private final DeviceType deviceType = DeviceType.WATER;
    private final List<Organization> organizationList = new ArrayList<>();
    private final Set<OrganizationType> organizationTypes = new HashSet<>();
    private final List<OrganizationChangesHistory> historyList = new ArrayList<>();
    private final int employeesCapasity = 6;

    @Mock
    private OrganizationRepository organizationRepository;

    @Mock
    private Organization organization;

    @Mock
    private OrganizationChangesHistoryRepository organizationChangesHistoryRepository;

    @Mock
    private LocalityService localityService;

    @Mock
    private Locality locality;

    @InjectMocks
    private OrganizationServiceImpl organizationService;


    @Before
    public void setUp() throws Exception {
        organizationService = new OrganizationServiceImpl();
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() throws Exception {
        organizationService = null;
    }

    @Test
    public void testAddOrganizationWithAdmin() throws Exception {
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
        final Long[] localityIdList = {123L};
        final String username = "eric123";
        final String password = "root";
        String firstName = "firstName";
        String lastName = "lastName";
        String middleName = "middleName";
        String adminName = "admin";

        stub(localityService.findById(anyLong())).toReturn(locality);

//        Organization expected = new Organization(OrgName, email, phone, employeesCapacity, maxProcessTime, address);

        organizationService.addOrganizationWithAdmin(OrgName, email, phone, types, employeesCapacity, maxProcessTime,
                firstName, lastName, middleName, username, password, address, adminName, localityIdList);
//        organizationService.getOrganizationById(organizationService.findAllByLocalityId(localityIdList[0]).get(0).getId());
    }

    @Test
    public void testGetOrganizationsBySearchAndPagination() throws Exception {
        int pageNumber = 2;
        int itemsPerPage = 10;
        String name = "name";
        String email = "email";
        String number = "0000000000";
        String type = "PROVIDER";
        String region = "Lv";
        String district = "Lviv";
        String locality = "Lviv";
        String streetToSearch = "Shev";
        String sortCriteria = null;
        String sortOrder = null;


//        organizationService.getOrganizationsBySearchAndPagination(pageNumber, itemsPerPage, name, email,
//                number, type, region, district, locality, streetToSearch, sortCriteria, sortOrder);
    }

    @Test
    public void testGetOrganizationById() throws Exception {
        stub(organizationRepository.findOne(organizationId)).toReturn(organization);
        Organization actual = organizationService.getOrganizationById(organizationId);

        assertEquals(organization, actual);
    }

    public void testEditOrganization() throws Exception {

    }

    @Test
    public void testGetOrganizationEmployeesCapacity() throws Exception {
        stub(organization.getEmployeesCapacity()).toReturn(employeesCapasity);
        stub(organizationRepository.findOne(organizationId)).toReturn(organization);
        int actual = organizationService.getOrganizationEmployeesCapacity(organizationId);

        assertEquals(employeesCapasity, actual);
    }

    public void testSendOrganizationChanges() throws Exception {

    }

    @Test
    public void testGetHistoryByOrganizationId() throws Exception {
        stub(organizationChangesHistoryRepository.findByOrganizationId(organizationId)).toReturn(historyList);
        List<OrganizationChangesHistory> actual = organizationService.getHistoryByOrganizationId(organizationId);

        assertEquals(historyList, actual);
    }

    @Test
    public void testFindAllByLocalityId() throws Exception {
        stub(organizationRepository.findOrganizationByLocalityId(localityId)).toReturn(organizationList);
        List<Organization> actual = organizationService.findAllByLocalityId(localityId);

        assertEquals(organizationList, actual);
    }

    @Test
    public void testFindAllByLocalityIdAndTypeId() throws Exception {
        stub(organizationRepository.findOrganizationByLocalityIdAndType(localityId, orgType)).toReturn(organizationList);
        List<Organization> actual = organizationService.findAllByLocalityIdAndTypeId(localityId, orgType);

        assertEquals(organizationList, actual);
    }

    @Test
    public void testFindOrganizationTypesById() throws Exception {
        stub(organizationRepository.findOrganizationTypesById(organizationId)).toReturn(organizationTypes);
        Set<OrganizationType> actual = organizationService.findOrganizationTypesById(organizationId);

        assertEquals(organizationTypes, actual);
    }


    @Test
    public void testFindByLocalityIdAndTypeAndDevice() throws Exception {
        stub(organizationRepository.findByLocalityIdAndTypeAndDevice(localityId, orgType, deviceType)).toReturn(organizationList);
        List<Organization> actual = organizationService.findByLocalityIdAndTypeAndDevice(localityId, orgType, deviceType);

        assertEquals(organizationList, actual);
    }
}