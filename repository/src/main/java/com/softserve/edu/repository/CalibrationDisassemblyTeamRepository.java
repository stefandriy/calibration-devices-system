package com.softserve.edu.repository;

import com.softserve.edu.entity.catalogue.Team.DisassemblyTeam;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface CalibrationDisassemblyTeamRepository extends PagingAndSortingRepository<DisassemblyTeam, Long>, JpaSpecificationExecutor {
}
