package com.softserve.edu.repository;

import com.softserve.edu.entity.verification.calibration.AdditionalInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Vasyl on 20.10.2015.
 */
@Repository
public interface AdditionalInfoRepository extends CrudRepository<AdditionalInfo, Long>{

    AdditionalInfo findAdditionalInfoByVerificationId(String verificationId);
}
