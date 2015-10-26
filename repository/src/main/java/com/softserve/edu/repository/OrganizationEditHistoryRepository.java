package com.softserve.edu.repository;

import com.softserve.edu.entity.organization.OrganizationEditHistory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganizationEditHistoryRepository extends CrudRepository<OrganizationEditHistory, Long> {

    List<OrganizationEditHistory> findByOrganizationId(Long organizationId);
}
