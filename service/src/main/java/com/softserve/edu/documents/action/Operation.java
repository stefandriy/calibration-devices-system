package com.softserve.edu.documents.action;

import com.softserve.edu.documents.parameter.FileParameters;
import org.apache.commons.vfs2.FileObject;

import java.io.IOException;

/**
 * Interface for all operations that can be done on a file.
 */
@FunctionalInterface
public interface Operation {
    /**
     * Performs the operation and returns it's result.
     *
     * @param sourceFile file to perform the operation on
     * @param fileParameters parameters that specify how the file
     *                       must be generated
     * @return file that contains results of the operation
     * @throws IOException if the source file can't be reached or a resulting
     *         file can't be created
     */
    FileObject perform(FileObject sourceFile, FileParameters fileParameters)
            throws IOException;
}
