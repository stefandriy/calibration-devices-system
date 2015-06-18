package com.softserve.edu.service.storage.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.io.*;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.springframework.stereotype.Service;

import com.softserve.edu.service.storage.FileOperations;

@Service
public class FileOperationImpl implements FileOperations {
    
    public static final String ROOT_PATH = System.getProperty("user.home") + File.separator
            + "MetrologyFiles";

    @Override
    public Boolean putResourse(InputStream stream, Path path, String resourceName,
            SaveOptions options) {

        try {
            FileUtils.copyInputStreamToFile(stream, path.toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public InputStream getResourseStream(Path path) throws IOException, FileNotFoundException {
        return FileUtils.openInputStream(path.toFile());
    }

    @Override
    public URI getResourseURI(Path directory, String fileName) {
        
        String file = FileSearch.find(directory.toFile(), "ccV01Nm0RiaXrQp4anPyzA.jpg");
        System.out.println(file);
        
        Path pathAbsolute = Paths.get(FileSearch.find(directory.toFile(), "ccV01Nm0RiaXrQp4anPyzA.jpg"));
        Path pathBase = Paths.get(ROOT_PATH);
        Path pathRelative = pathBase.relativize(pathAbsolute);
        System.out.println(pathRelative);
        
        
        
        return pathRelative.toUri();
    }

}
