package com.softserve.edu.service.utils;

import org.apache.log4j.Logger;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by Yurko on 28.11.2015.
 */
public class ZipArchiver {
    static Logger logger = Logger.getLogger(ZipArchiver.class);

    public static void createZip(OutputStream output, List<File> sources) throws Exception {
        ZipOutputStream zipOut = new ZipOutputStream(output);
        zipOut.setLevel(Deflater.DEFAULT_COMPRESSION);

        for (File source : sources) {
            if (source.isDirectory()) {
                zipDirectory(zipOut, "", source);
            } else {
                zipFile(zipOut, "", source);
            }
        }
        zipOut.flush();
        zipOut.close();
        logger.debug("Done");
    }

    private static String buildPath(String path, String file) {
        if (path == null || path.isEmpty()) {
            return file;
        } else {
            return path + "/" + file;
        }
    }

    private static void zipDirectory(ZipOutputStream zos, String path, File dir) throws Exception {
        if (!dir.canRead()) {
            throw new AccessDeniedException("Cannot read " + dir.getCanonicalPath() + " (maybe because of permissions)");
        }

        File[] files = dir.listFiles();
        path = buildPath(path, dir.getName());
        logger.debug("Adding Directory " + path);

        for (File source : files) {
            if (source.isDirectory()) {
                zipDirectory(zos, path, source);
            } else {
                zipFile(zos, path, source);
            }
        }

        logger.debug("Leaving Directory " + path);
    }

    private static void zipFile(ZipOutputStream zos, String path, File file) throws Exception {
        if (!file.canRead()) {
            throw new AccessDeniedException("Cannot read " + file.getCanonicalPath() + " (maybe because of permissions)");
        }

        logger.debug("Compressing " + file.getName());
        zos.putNextEntry(new ZipEntry(buildPath(path, file.getName())));

        FileInputStream fis = new FileInputStream(file);

        byte[] buffer = new byte[4092];
        int byteCount = 0;
        while ((byteCount = fis.read(buffer)) != -1) {
            zos.write(buffer, 0, byteCount);
            System.out.print('.');
            System.out.flush();
        }

        fis.close();
        zos.closeEntry();
    }
}
