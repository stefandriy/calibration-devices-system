package com.softserve.edu.documents.utils;

import com.softserve.edu.documents.parameter.FileSystem;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;
import org.apache.log4j.Logger;

import java.io.FileNotFoundException;

/**
 * Utility class for files operations.
 */
public class FileUtils {
    private static Logger logger = Logger.getLogger(FileUtils.class);

    /**
     * Separator used to mark the file system part of path.
     */
    private static final String FILE_SYSTEM_SEPARATOR = "://";

    /**
     * Creates a file.
     *
     * @param fileSystem one of the supported file systems
     * @param fileName   name of the file
     * @return the created file
     */
    public static FileObject createFile(FileSystem fileSystem, String fileName) {
        FileObject fileToReturn;

        String filePath = fileSystem.name().toLowerCase() +
                FILE_SYSTEM_SEPARATOR + fileName;

        FileSystemManager manager = getFileSystemManager();

        try {
            fileToReturn = manager.resolveFile(filePath);

            if (!fileToReturn.exists())
                fileToReturn.createFile();
        } catch (FileSystemException e) {
            logger.error("", e);
            throw new RuntimeException(e);
        }

        return fileToReturn;
    }

    /**
     * Gets file by it's name in the specified file system.
     *
     * @param fileSystem one of the supported file systems
     * @param fileName   name of the file
     * @return the found file
     */
    public static FileObject findFile(FileSystem fileSystem, String fileName) {
        FileObject fileToReturn;

        String filePath = fileSystem.name().toLowerCase() +
                FILE_SYSTEM_SEPARATOR + fileName;

        FileSystemManager manager = getFileSystemManager();

        try {
            fileToReturn = manager.resolveFile(filePath);

            if (!fileToReturn.exists()) {
                String errorMessage = "file with name " +
                        fileName + " couldn't be found in file system " +
                        fileSystem;

                logger.error(errorMessage);
                throw new RuntimeException(new FileNotFoundException(errorMessage));
            }
        } catch (FileSystemException exception) {
            logger.error("couldn't create " +
                    FileSystemManager.class.getSimpleName(), exception);
            throw new RuntimeException(exception);
        }

        return fileToReturn;
    }

    /**
     * @return the file system manager
     */
    private static FileSystemManager getFileSystemManager() {
        FileSystemManager manager;

        try {
            manager = VFS.getManager();
        } catch (FileSystemException exception) {
            logger.error("couldn't create " +
                    FileSystemManager.class.getSimpleName(), exception);
            throw new RuntimeException(exception);
        }

        return manager;
    }
}