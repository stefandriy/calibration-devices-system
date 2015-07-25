package com.softserve.edu.service.calibrator;

import com.softserve.edu.entity.BbiProtocol;
import com.softserve.edu.entity.Organization;
import com.softserve.edu.repository.OrganizationRepository;
import com.softserve.edu.repository.UploadBbiRepository;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@Service
public class CalibratorService {

    @Autowired
    private OrganizationRepository calibratorRepository;

    @Autowired
    private UploadBbiRepository uploadBbiRepository;

    @PersistenceContext
    private EntityManager em;

    @Transactional(readOnly = true)
    public List<Organization> findByDistrict(String district, String type) {
        System.err.println("searching calibrators");
        return calibratorRepository.getByTypeAndDistrict(district, type);
    }

    @Transactional(readOnly = true)
    public Organization findById(Long id) {
        return calibratorRepository.findOne(id);
    }

    @Transactional
    public void upload(File file) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] bytesOfBbi = IOUtils.toByteArray(fileInputStream);
        BbiProtocol bbiProtocol = new BbiProtocol(bytesOfBbi);
        uploadBbiRepository.save(bbiProtocol);
    }

}
