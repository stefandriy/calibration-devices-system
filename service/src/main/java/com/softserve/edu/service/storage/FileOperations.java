package com.softserve.edu.service.storage;

import java.io.InputStream;
import java.net.URI;
import java.nio.file.Path;

public interface FileOperations {

    public Boolean putResourse(InputStream stream, String relativeFolder);

    public URI getResourseURI(Path directory, String relativeFilePath);

}