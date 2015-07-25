package com.softserve.edu.controller.file.storage;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * Created by MAX on 25.07.2015.
 */
public class BbiFileUtils {

    public static File multipartToFile(MultipartFile multipart) throws IllegalStateException, IOException {
        File file = new File(multipart.getOriginalFilename());
        multipart.transferTo(file);
        return file;
    }


}