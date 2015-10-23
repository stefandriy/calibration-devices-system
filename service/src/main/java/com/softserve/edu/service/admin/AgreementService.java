package com.softserve.edu.service.admin;

import com.softserve.edu.entity.device.Device;
import com.softserve.edu.entity.organization.Agreement;
import com.softserve.edu.entity.organization.Organization;
import com.softserve.edu.service.utils.ListToPageTransformer;

import java.util.Date;
import java.util.Set;


public interface AgreementService {

    Agreement add(Agreement agreement);

    Agreement add(Organization customer, Organization executor, String number, Long deviceCount, Date date, Device.DeviceType deviceType);

    Agreement add(Long customerId, Long executorId, String number, Long deviceCount, Date date, Device.DeviceType deviceType);

    Agreement findAgreementById(Long id);

    Set<Agreement> findAll();

    void update(Agreement agreement);

    ListToPageTransformer<Agreement> getCategoryDevicesBySearchAndPagination(int pageNumber, int itemsPerPage, String customer, String executor, String number,
                                                                                    String deviceCount, String date, String deviceType, String sortCriteria, String sortOrder);

    }
