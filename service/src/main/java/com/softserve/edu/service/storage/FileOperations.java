package com.softserve.edu.service.storage;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;


public interface FileOperations {

    String putResourse(InputStream stream, String relativeFolder, String fileType);

    URI getResourseURI(String relativeFilePath);

    void putBbiFile(InputStream stream, String verificationId, String fileName)  throws IOException;

}
