package com.softserve.edu.repository;

import com.softserve.edu.entity.device.CalibrationModule;

/**
 * Created by roman on 13.11.15.
 *
 */

public interface CalibrationModuleRepositoryCustom {

    CalibrationModule saveWithGenerating(CalibrationModule calibrationModule);

}
