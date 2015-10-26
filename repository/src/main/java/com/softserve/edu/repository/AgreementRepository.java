package com.softserve.edu.repository;

import com.softserve.edu.entity.device.Device;
import com.softserve.edu.entity.organization.Agreement;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface AgreementRepository extends CrudRepository<Agreement, Long> {

    Set<Agreement> findAll();

    @Query("SELECT A FROM Agreement A INNER JOIN A.customer C " +
            "WHERE C.id =:customerId AND A.deviceType =:deviceType AND A.isAvailable = true")
    Set<Agreement> findByCustomerIdAndDeviceType(@Param("customerId") Long customerId,
                                                 @Param("deviceType") Device.DeviceType deviceType);

    @Query("SELECT A FROM Agreement A INNER JOIN A.customer C " +
            "WHERE C.id =:customerId AND A.isAvailable = true")
    Set<Agreement> findByCustomerId(@Param("customerId") Long customerId);


}
