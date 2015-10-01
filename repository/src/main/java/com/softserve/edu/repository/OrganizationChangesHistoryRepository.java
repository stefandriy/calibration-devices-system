package com.softserve.edu.repository;

import com.softserve.edu.entity.organization.OrganizationChangesHistory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganizationChangesHistoryRepository extends CrudRepository<OrganizationChangesHistory, Long> {

    List<OrganizationChangesHistory> findByOrganizationId(Long organizationId);
}
