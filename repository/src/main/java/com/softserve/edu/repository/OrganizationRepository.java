package com.softserve.edu.repository;

import com.softserve.edu.entity.catalogue.Locality;
import com.softserve.edu.entity.catalogue.util.LocalityDTO;
import com.softserve.edu.entity.enumeration.device.DeviceType;
import com.softserve.edu.entity.enumeration.organization.OrganizationType;
import com.softserve.edu.entity.organization.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface OrganizationRepository extends CrudRepository<Organization, Long> {

    Page<Organization> findAll(Pageable pageable);

    //TODO NEED TO REFACTOR
    @Query
    (
        value =
                "SELECT * FROM Organization org INNER JOIN ORGANIZATION_TYPE orgType  " +
                "ON org.id = orgType.organizationId " +
                "WHERE orgType.value = :type AND org.district = :district",
        nativeQuery = true
    )
    List<Organization> findByDistrictAndType(@Param("district") String district, @Param("type") String type);

    /*@Query("SELECT org FROM Organization org " +
            "WHERE org.district = :district AND  :typeId in elements(org.organizationTypes)")
    List<Organization> findByDistrictAndType(@Param("district") String district, @Param("type") OrganizationType type);*/

    /**
     *
     * @param organizationId
     * @return
     */
    @Query("SELECT elements(org.organizationTypes) FROM Organization org WHERE org.id=:organizationId")
    Set<OrganizationType> findOrganizationTypesById(@Param("organizationId") Long organizationId);

    /**
     * FInd all organizations in selected location by
     * @param localityId
     * @return
     */
    @Query("SELECT o FROM Organization o INNER JOIN o.localities l WHERE l.id=:localityId")
    List<Organization> findOrganizationByLocalityId(@Param("localityId") Long localityId);

    /**
     * Find all organizations in selected locality and organization type
     *
     * @param localityId
     * @param typeId
     * @return
     */
    @Query("SELECT org FROM Organization org " +
            "INNER JOIN org.localities l " +
            "WHERE l.id=:localityId AND  :typeId in elements(org.organizationTypes)")
    List<Organization> findOrganizationByLocalityIdAndType(@Param("localityId") Long localityId, @Param("typeId") OrganizationType typeId);

    /**
     * Find all organizations in selected locality, organization type and device type
     *
     * @param localityId  id of locality
     * @param orgType  type of organization
     * @param deviceType  device type
     * @return list of organizations
     */
    @Query("SELECT org FROM Organization org " +
            "INNER JOIN org.localities l " +
            "WHERE l.id=:localityId AND  :orgType in elements(org.organizationTypes) AND :deviceType in elements(org.deviceTypes)")
    List<Organization> findByLocalityIdAndTypeAndDevice(@Param("localityId") Long localityId, @Param("orgType") OrganizationType orgType, @Param("deviceType") DeviceType deviceType );

    @Query("SELECT NEW com.softserve.edu.entity.catalogue.util.LocalityDTO(l.id, l.designation, l.district.id) " +
            "FROM Organization org INNER JOIN org.localities l " +
            "WHERE org.id=:organizationId")
    List<LocalityDTO> findLocalitiesByOrganizationId(@Param("organizationId") Long organizationId);

}
