package com.softserve.edu.repository;

import com.softserve.edu.entity.OrganizationChangeHistory;
import com.softserve.edu.entity.util.OrganizationChangeHistoryPK;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by vova on 23.09.15.
 */

@Repository
public interface OrganizationChangeHistoryRepository extends CrudRepository<OrganizationChangeHistory, OrganizationChangeHistoryPK> {

    @Query("select o from OrganizationChangeHistory o where o.organizationChangeHistoryPK.orgId =:orgId")
    List<OrganizationChangeHistory> getById(@Param("orgId") Long orgId);
}
