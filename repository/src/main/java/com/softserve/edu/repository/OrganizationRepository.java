package com.softserve.edu.repository;

import com.softserve.edu.entity.Address;
import com.softserve.edu.entity.Organization;
import com.softserve.edu.entity.OrganizationType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface OrganizationRepository extends CrudRepository<Organization, Long>, OrganizationRepositoryCustom {

	Page<Organization> findAll(Pageable pageable);

	Page<Organization> findByNameLikeIgnoreCase(String name, Pageable pageable);

	@Query("select t from OrganizationType t where t.type=:type")
	OrganizationType getOrganizationType(@Param("type") String type);

	@Query("select t.type from OrganizationType t inner join t.organizations o where o.id=:id")
	Set<String> getOrganizationTypesById(@Param("id") Long id);

	@Query("select o from Organization o inner join o.organizationTypes ot where o.address.district=:district and ot.type=:type")
	List<Organization> getByTypeAndDistrict(@Param("district") String district, @Param("type") String type);

	/*@Query("select o from Organization o inner join o.organizationTypes ot where o.address.district.id=:district.id and ot.type=:type")
	List<Organization> getByTypeAndDistrictId(@Param("id") Long id, @Param("type") String type);*/


	@Query("select o.employeesCapacity from Organization o where o.id=:id")
	Integer getOrganizationEmployeesCapacity(@Param("id") Long id);

}
