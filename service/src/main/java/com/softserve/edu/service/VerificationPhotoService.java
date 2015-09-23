package com.softserve.edu.service;

import java.io.InputStream;

public interface VerificationPhotoService {
    boolean putResource(long testId, InputStream stream, String fileType);
}
