package com.softserve.edu.documents.document;

import com.softserve.edu.documents.document.meta.*;
import com.softserve.edu.entity.device.CalibrationModule;
import com.softserve.edu.entity.verification.calibration.CalibrationTask;
import com.softserve.edu.entity.verification.calibration.CalibrationTest;
import com.softserve.edu.entity.verification.Verification;
import com.softserve.edu.service.admin.CounterTypeService;
import com.softserve.edu.service.admin.impl.CounterTypeServiceImpl;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Represents a verification certificate.
 */
@com.softserve.edu.documents.document.meta.Document
public class VerificationCertificate extends BaseCertificate {

    public VerificationCertificate () {
    }

    public VerificationCertificate(Verification verification, CalibrationTest calibrationTest) {
        super(verification, calibrationTest);
    }

    /**
     * @return get the sign of the document, which contains the metrological characteristics
     */
    @Placeholder(name = "COUNTER_TYPE_GOST")
    public String getCounterTypeGost() {
        return getVerification().getCounter().getCounterType().getGost();
    }

    /**
     * @return get the name of the document, which contains the metrological characteristics
     */
    @Placeholder(name = "CALIBRATION_TYPE")
    public String getCalibrationType() {
        return getVerification().getTask().getModule().getCalibrationType();

    }
}
