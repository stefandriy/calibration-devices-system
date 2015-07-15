package com.softserve.edu.service.state.verificator;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.softserve.edu.entity.Organization;
import com.softserve.edu.repository.OrganizationRepository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;


@Service
@Transactional
public class StateVerificatorService {

        @Autowired
        private OrganizationRepository stateVerificatorRepository;

        @Transactional
        public void saveStateVerificator(Organization cstateVerificatorlibrator) {
            stateVerificatorRepository.save(cstateVerificatorlibrator);
        }
        
        @PersistenceContext
    	private EntityManager em;
        
        @Transactional (readOnly = true)
        public List<Organization> findByDistrict(String district, String type) {

        	String sql = "SELECT * FROM ORGANIZATION AS o INNER JOIN ORGANIZATION_TYPE t  WHERE o.district = ? and t.type = ?";
        	Query q = em.createNativeQuery(sql, Organization.class);
        	q.setParameter(1, district);
        	q.setParameter(2, type);
        	List<Organization> list = (List<Organization>)q.getResultList();
        	  return list;
        }
        
        @Transactional(readOnly = true)
        public Organization findById(Long id){
        	return stateVerificatorRepository.findOne(id);
        }

}
