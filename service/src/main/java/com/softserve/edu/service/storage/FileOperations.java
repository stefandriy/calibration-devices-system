package com.softserve.edu.service.storage;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;


public interface FileOperations {

    public String putResourse(InputStream stream, String relativeFolder, String fileType);

    public URI getResourseURI(String relativeFilePath);

    public String putBbiFile(InputStream stream, String fileName)  throws IOException;

}
