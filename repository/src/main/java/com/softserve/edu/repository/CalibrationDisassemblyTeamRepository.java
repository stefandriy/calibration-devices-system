package com.softserve.edu.repository;

import com.softserve.edu.entity.catalogue.Team.DisassemblyTeam;
import com.softserve.edu.entity.organization.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CalibrationDisassemblyTeamRepository extends PagingAndSortingRepository<DisassemblyTeam, String>, JpaSpecificationExecutor {

        Page<DisassemblyTeam> findAll(Pageable pageable);

        List<DisassemblyTeam> findByOrganization(Organization organization);

        Page<DisassemblyTeam> findByOrganization(Organization organization, Pageable pageable);

        Page<DisassemblyTeam> findByOrganizationAndNameLikeIgnoreCase(Organization organization, String name, Pageable pageable);
}
