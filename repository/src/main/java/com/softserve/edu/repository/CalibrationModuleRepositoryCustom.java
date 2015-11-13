package com.softserve.edu.repository;

import com.softserve.edu.entity.device.CalibrationModule;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by roman on 13.11.15.
 *
 */

public interface CalibrationModuleRepositoryCustom {

    CalibrationModule saveWithGenerating(CalibrationModule calibrationModule);

}
