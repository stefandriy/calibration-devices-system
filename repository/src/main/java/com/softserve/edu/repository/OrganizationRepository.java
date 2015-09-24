package com.softserve.edu.repository;

import com.softserve.edu.entity.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface OrganizationRepository extends CrudRepository<Organization, Long>, OrganizationRepositoryCustom {

    Page<Organization> findAll(Pageable pageable);

    @Query
    (
        value =
                "FROM Organization org INNER JOIN ORGANIZATION_TYPE orgType  " +
                "ON org.id = orgType.organizationId " +
                "WHERE orgType.value = :type AND org.district = :district",
        nativeQuery = true
    )
    List<Organization> findByDistrictAndType(@Param("district") String district, @Param("type") String type);

    @Query
    (
       value =
               "SELECT orgType.value " +
               "FROM ORGANIZATION_TYPE orgType " +
               "WHERE orgType.organizationId = :organizationId",
       nativeQuery = true
    )
    Set<String> findOrganizationTypesById(@Param("organizationId") Long organizationId);
}
