package com.softserve.edu.documents.action;

import com.mysql.jdbc.log.Log4JLogger;
import com.softserve.edu.documents.parameter.FileParameters;
import org.apache.commons.io.IOUtils;
import org.apache.commons.vfs2.FileContent;
import org.apache.commons.vfs2.FileObject;
import org.apache.log4j.Logger;

import java.io.*;

/**
 * Load template that corresponds to the document's type, copy it to the
 * source file and return it.
 */
public class LoadTemplate implements Operation {
    static Logger log = Logger.getLogger(LoadTemplate.class.getName());

    /**
     * {inherit}
     */
    @Override
    public FileObject perform(FileObject sourceFile,
                              FileParameters fileParameters) throws IOException {
        FileObject template = fileParameters.getDocumentType().getTemplate();
        FileContent templateContent = template.getContent();
        FileContent sourceContent = sourceFile.getContent();

        try (InputStream inputStream = templateContent.getInputStream();
             OutputStream outputStream = sourceContent.getOutputStream()) {
            IOUtils.copy(inputStream, outputStream);
        } catch (IOException exception) {
            log.error("exception: ", exception);
            throw exception;
        }

        return sourceFile;
    }

    private static final LoadTemplate instance = new LoadTemplate();

    public static LoadTemplate getInstance() {
        return instance;
    }
}
