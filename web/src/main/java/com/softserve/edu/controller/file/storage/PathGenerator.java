package com.softserve.edu.controller.file.storage;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PathGenerator {

    @Value("${photo.storage.local}")
    private String ROOT_PATH;

    public Path getPath(String fileType) {
        Base64 base64 = new Base64();
        UUID uuid = UUID.randomUUID();
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        Path path = Paths.get(ROOT_PATH + File.separator + date + File.separator
                + base64.encodeBase64URLSafeString(bb.array()).concat(fileType));
        return path;
    }

    public String getROOT_PATH() {
        return ROOT_PATH;
    }
}
