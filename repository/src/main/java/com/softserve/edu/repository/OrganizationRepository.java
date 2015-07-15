package com.softserve.edu.repository;

import com.softserve.edu.entity.Organization;
import com.softserve.edu.entity.OrganizationType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganizationRepository extends CrudRepository<Organization, Long>, OrganizationRepositoryCustom {

    Page<Organization> findAll(Pageable pageable);

    Page<Organization> findByNameLikeIgnoreCase(String name, Pageable pageable);
    
      @Query("select t.type from OrganizationType t inner join t.organizations o where o.id=:id")
    String getTypeByOrganizationId(@Param("id") Long id);
    
    @Query(value = "select ot.type from ORGANIZATION_TYPE as ot inner join ORGANIZATIONS_TYPES AS ots "
    		+ "inner join ORGANIZATION AS o WHERE ots.typeId = ot.typeId "
    		+ "AND ots.organizationId = ?;", nativeQuery = true)
    String getTypeById(Long id);

}
