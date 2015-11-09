package com.softserve.edu.service.storage.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.UUID;

import com.softserve.edu.service.storage.FileOperations;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FileOperationsImpl implements FileOperations{

    @Value("${photo.storage.local}")
    private String localStorage;

    @Value("${photo.path.prefix}")
    private String photoPathPref;

    @Value("${bbi.storage.local}")
    private String bbiLocalStorage;

    public String putResourse(InputStream stream, String relativeFolder, String fileType) {
        String fileName = getFileName(fileType);
        try {
            FileUtils.copyInputStreamToFile(stream, new File(localStorage + relativeFolder
                    + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return relativeFolder + fileName;
    }

    public URI getResourseURI(String relativeFilePath) {
        return URI.create(photoPathPref + relativeFilePath);
    }

    /***
     * Reads stream and saves content in file on local storage.
     * @return Absolute path to saved file.
     */
    public void putBbiFile(InputStream stream, String verificationId, String fileName) throws IOException {
        String absolutePath = System.getProperty("catalina.base") + "/bbi/" + verificationId + "/" + fileName;
        FileUtils.copyInputStreamToFile(stream, new File(absolutePath));
    }

    private String getFileName(String fileType) {
        Base64 base64 = new Base64();
        UUID uuid = UUID.randomUUID();
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return base64.encodeBase64URLSafeString(bb.array()).concat(fileType);
    }

}