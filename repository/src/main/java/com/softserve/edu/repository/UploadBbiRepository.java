package com.softserve.edu.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softserve.edu.entity.BbiProtocol;

/**
 * Created by MAX on 25.07.2015.
 */
public interface UploadBbiRepository extends CrudRepository<BbiProtocol, Long> {

    @Query("select b.fileName from BbiProtocol b  where b.verification.id=:verificationId")
    String findFileNameByVerificationId(@Param("verificationId") String verificationId);

    @Query("select b from BbiProtocol b  where b.verification.id=:verificationId")
    BbiProtocol findByVerification(@Param("verificationId") String verificationId);

}
