package com.softserve.edu.service.verification;

import java.io.InputStream;

public interface VerificationPhotoService {
    boolean putResource(long testId, InputStream stream, String fileType);
}
