package com.softserve.edu.service.calibrator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.softserve.edu.entity.Organization;
import com.softserve.edu.repository.OrganizationRepository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

@Service
public class CalibratorService {

    @Autowired
    private OrganizationRepository calibratorRepository;

    @PersistenceContext
	private EntityManager em;
    
    @Transactional (readOnly = true)
    public List<Organization> findByDistrict(String district) {
    	
    	System.err.println("INISIDE FIND BY DISTRICT IN CALIBRATOR SERVICE");
       
//   	TypedQuery<String> query = em.createQuery("SELECT o.name FROM Organization o WHERE o.address.district = ?", String.class);
//    	  query.setParameter(1, "Львівський");
    	
    	  String sql = "SELECT * FROM ORGANIZATION AS o  WHERE o.district = ?";
    	  Query q = em.createNativeQuery(sql, Organization.class);
    			  q.setParameter(1, "Львівський");
    			  
//    	  Organization org = (Organization) q.getSingleResult();
//    	  System.out.println("org name is " + org.getName());
//    	  List<Organization> list  = q.getResultList();
    	// System.err.println(q.getResultList().get(0) instanceof Organization);
//    	  System.err.println(list.get(0).getName() + list.get(1).getName() + list.get(2).getName());
//    	  List<String> names = query.getResultList();
//    	  System.out.println(names.get(0));
    			  
    		List<Organization> list1 = (List<Organization>)q.getResultList();
    			  
//    	  List<Organization> list =	calibratorRepository.findOrganizationByType("CALIBRATOR");
    	 
    	  return list1;
    	  
    	  //return calibratorRepository.findByAddressDistrict(district);
    }

    @Transactional(readOnly = true)
    public Organization findById(Long id) {
        return calibratorRepository.findOne(id);
    }
}
