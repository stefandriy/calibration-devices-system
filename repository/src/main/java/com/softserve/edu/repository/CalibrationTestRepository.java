package com.softserve.edu.repository;

import com.softserve.edu.entity.CalibrationTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CalibrationTestRepository extends CrudRepository<CalibrationTest, Long> {

    @Query("select c.name from CalibrationTest c where c.verification.id=:verificationId")
    String findTestNameByVerificationId(@Param("verificationId") String verificationId);

    @Query("select c from CalibrationTest c where c.verification.id=:verificationId")
    String findTestByVerificationId(@Param("verificationId") String verificationId);

    List<CalibrationTest> findByName(String name);

    CalibrationTest findById(Long id);

    CalibrationTest findByVerificationId(String verifId);

    public Page<CalibrationTest> findAll(Pageable pageable);
    Page<CalibrationTest> findByNameLikeIgnoreCase(String name, Pageable pageable);
}