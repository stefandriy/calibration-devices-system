package com.softserve.edu.documents.resources;

import com.softserve.edu.documents.parameter.FileSystem;
import com.softserve.edu.documents.utils.FileUtils;
import org.apache.commons.vfs2.FileObject;

import java.io.File;
import java.util.EnumMap;

/**
 * Singleton.
 * Factory for creating object.
 */
public enum DocumentTemplateFactory {
    INSTANCE;

    /**
     * Map for storing object files for reuse.
     */
    private EnumMap<DocumentType, FileObject> documentTemplateMap =
            new EnumMap<>(DocumentType.class);

    /**
     * Builds file.
     * If the file has been previously build this function doesn't create
     * a new file, but returns the existing file.
     *
     * @param documentType document type of the needed file
     * @return the built file
     */
    public FileObject build(DocumentType documentType) {
        FileObject template = documentTemplateMap.get(documentType);

        if (template == null) {
            template = FileUtils.createFile(FileSystem.RES,
                    ResourcesFolder.DOCUMENTS_TEMPLATES + File.separator +
                            documentType.toString());
            documentTemplateMap.put(documentType, template);
        }

        return template;
    }
}
