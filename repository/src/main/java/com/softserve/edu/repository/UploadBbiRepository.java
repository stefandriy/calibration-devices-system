package com.softserve.edu.repository;

import com.softserve.edu.entity.verification.BbiProtocol;
import com.softserve.edu.entity.verification.Verification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * Created by MAX on 25.07.2015.
 */
public interface UploadBbiRepository extends CrudRepository<BbiProtocol, Long> {

    @Query("SELECT b.fileName FROM BbiProtocol b  WHERE b.verification.id=:verificationId")
    String findFileNameByVerificationId(@Param("verificationId") String verificationId);

    @Query("SELECT b.bbi FROM BbiProtocol b WHERE b.fileName=:fileName")
    byte[] findFileBytesByFileName(@Param("fileName") String fileName);

    BbiProtocol findByVerification(Verification verification);


}
