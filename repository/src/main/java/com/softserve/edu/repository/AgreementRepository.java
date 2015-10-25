package com.softserve.edu.repository;

import com.softserve.edu.entity.device.Device;
import com.softserve.edu.entity.organization.Agreement;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.Set;

@Repository
public interface AgreementRepository extends PagingAndSortingRepository<Agreement, Long>, JpaSpecificationExecutor<Agreement> {

    Set<Agreement> findAll();

    @Query("SELECT A FROM Agreement A INNER JOIN A.customer C " +
            "WHERE C.id =:customerId AND A.deviceType =:deviceType AND A.isAvailable = true")
    Set<Agreement> findByCustomerIdAndDeviceType(@Param("customerId") Long customerId,
                                                 @Param("deviceType") Device.DeviceType deviceType);

    @Query("SELECT A FROM Agreement A INNER JOIN A.customer C " +
            "WHERE C.id =:customerId AND A.isAvailable = true")
    Set<Agreement> findByCustomerId(@Param("customerId") Long customerId);

    @Query("SELECT MIN(A.date) FROM Agreement A WHERE  A.isAvailable = true")
    Date findEarliestDateAvalibleAgreement();
}
