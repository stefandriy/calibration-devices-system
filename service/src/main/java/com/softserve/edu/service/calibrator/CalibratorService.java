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
    public List<Organization> findByDistrict(String district, String type) {
    	System.err.println("searching calibrators");
    	  return calibratorRepository.getByTypeAndDistrict(district, type);
    }

    @Transactional(readOnly = true)
    public Organization findById(Long id) {
        return calibratorRepository.findOne(id);
    }
}
