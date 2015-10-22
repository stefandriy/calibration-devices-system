package com.softserve.edu.service.admin.impl;

import com.softserve.edu.entity.device.CounterType;
import com.softserve.edu.repository.CounterTypeRepository;
import com.softserve.edu.service.admin.CounterTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
public class CounterTypeServiceImpl implements CounterTypeService{

    @Autowired
    private CounterTypeRepository counterTypeRepository;

    @PersistenceContext
    private EntityManager entityManager;



}
