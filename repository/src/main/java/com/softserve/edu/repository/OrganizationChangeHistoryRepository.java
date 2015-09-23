package com.softserve.edu.repository;

import com.softserve.edu.entity.OrganizationChangeHistory;
import com.softserve.edu.entity.util.OrganizationChangeHistoryPK;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by vova on 23.09.15.
 */

@Repository
public interface OrganizationChangeHistoryRepository extends CrudRepository<OrganizationChangeHistory, OrganizationChangeHistoryPK> {
}
