package com.softserve.edu.documents.document;

import com.softserve.edu.documents.document.meta.Placeholder;
import com.softserve.edu.entity.CalibrationTest;
import com.softserve.edu.entity.Verification;

/**
 * Represents an unfitness certificate.
 */
@com.softserve.edu.documents.document.meta.Document
public class UnfitnessCertificate extends BaseCertificate {
    /**
     * Constructor.
     *
     * @param verification    entity to get document's data from
     * @param calibrationTest one of calibration test that is assigned to
     *                        the verification
     */
    public UnfitnessCertificate(Verification verification,
                                CalibrationTest calibrationTest) {
        super(verification, calibrationTest);
    }

    @Override
    @Placeholder(name = "UNFITNESS_CERTIFICATE_NUMBER")
    public String getVerificationCertificateNumber() {
        return super.getVerificationCertificateNumber();
    }
}
