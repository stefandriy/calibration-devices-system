package com.softserve.edu.service.calibrator;

import com.softserve.edu.entity.BbiProtocol;
import com.softserve.edu.entity.Organization;
import com.softserve.edu.entity.Verification;
import com.softserve.edu.repository.OrganizationRepository;
import com.softserve.edu.repository.UploadBbiRepository;
import com.softserve.edu.repository.VerificationRepository;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class CalibratorService {

    @Autowired
    private OrganizationRepository calibratorRepository;

    @Autowired
    private UploadBbiRepository uploadBbiRepository;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private VerificationRepository verificationRepository;

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
    public void uploadBbi(InputStream fileInputStream,String idVerification) throws IOException {
         byte[] bytesOfBbi = IOUtils.toByteArray(fileInputStream);
          Verification verification= verificationRepository.findOne(idVerification);
        BbiProtocol bbiProtocol = new BbiProtocol(bytesOfBbi,verification);
        uploadBbiRepository.save(bbiProtocol);
    }

}
