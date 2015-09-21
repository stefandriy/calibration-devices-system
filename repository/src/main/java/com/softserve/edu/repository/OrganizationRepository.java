package com.softserve.edu.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.softserve.edu.entity.Organization;

@Repository
public interface OrganizationRepository extends CrudRepository<Organization, Long>, OrganizationRepositoryCustom {

    Page<Organization> findAll(Pageable pageable);

    @Query("SELECT t.typeValue, o.id FROM OrganizationType t INNER JOIN Organization o ON t.organizationId = o.id WHERE o.id = :id")
    Set<String> getOrganizationTypesById(@Param("id") Long id);

    List<Organization> findByTypeAndDistrict(String district, String type);

    @Query("SELECT o.employeesCapacity FROM Organization o WHERE o.id =: id")
    Integer getOrganizationEmployeesCapacity(@Param("id") Long id);

    @Query("SELECT d.deviceType FROM  Device d INNER JOIN d.provider WHERE d.provider.id = :organizationId")
    Set<String> getDeviceTypesByOrganization(@Param("organizationId") Long organizationId);
}
