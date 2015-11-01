package com.softserve.edu.service.storage;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;


public interface FileOperations {

    public String putResourse(InputStream stream, String relativeFolder, String fileType);

    public URI getResourseURI(String relativeFilePath);

    public void putBbiFile(InputStream stream, String verificationId, String fileName)  throws IOException;

}
