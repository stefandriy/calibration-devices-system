package com.softserve.edu.documents.document;

import com.softserve.edu.documents.document.meta.Placeholder;
import com.softserve.edu.entity.CalibrationTest;
import com.softserve.edu.entity.Verification;

/**
 * Represents a verification certificate.
 */
@com.softserve.edu.documents.document.meta.Document
public class VerificationCertificate extends BaseCertificate {
    public VerificationCertificate(Verification verification, CalibrationTest calibrationTest) {
        super(verification, calibrationTest);
    }

    /**
     * @return get the sign of the document, which contains the metrological characteristics
     */
    @Placeholder(name = "METR_DOC_SIGN")
    public String getMetrologicalDocumentSign() {
        return getCalibrationTest().getMeteorologicalDocument().getSign();
    }

    /**
     * @return get the name of the document, which contains the metrological characteristics
     */
    @Placeholder(name = "METR_DOC_NAME")
    public String getMetrologicalDocumentName() {
        return getCalibrationTest().getMeteorologicalDocument().getName();
    }
}
