package com.softserve.edu.repository;

import com.softserve.edu.entity.CalibrationTestIMG;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * Created by Konyk on 30.07.2015.
 */
public interface CalibrationTestIMGRepository extends CrudRepository<CalibrationTestIMG, Long>{

    @Query("select i.imgName from CalibrationTestIMG i where i.calibrationTest.id=:calibrationTestId")
    String findImgNameByCalibrationTestId(@Param("calibrationTestId") String calibrationTestId);

    @Query("select i from CalibrationTestIMG i where i.calibrationTest.id=:calibrationTestId")
    CalibrationTestIMG findImgByCalibrationTestId(@Param("calibrationTestId") Long calibrationTestId);
}
