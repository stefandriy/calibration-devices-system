package com.softserve.edu.repository;

import com.softserve.edu.entity.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganizationRepository extends CrudRepository<Organization, Long>, OrganizationRepositoryCustom {

    Page<Organization> findAll(Pageable pageable);

    List<Organization> findByTypeAndDistrict(String district, String type);
}
