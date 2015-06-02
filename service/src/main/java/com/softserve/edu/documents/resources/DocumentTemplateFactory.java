package com.softserve.edu.documents.resources;

import com.softserve.edu.documents.parameter.FileSystem;
import com.softserve.edu.documents.utils.FileUtils;
import org.apache.commons.vfs2.FileObject;

import java.util.EnumMap;

/**
 *
 */
public class DocumentTemplateFactory {
    static private EnumMap<DocumentType, FileObject> documentTemplateMap =
            new EnumMap<>(DocumentType.class);

    public static FileObject build(DocumentType documentType) {
        FileObject template = documentTemplateMap.get(documentType);

        if (template == null) {
            template = FileUtils.findFile(FileSystem.RES,
                    ResourcesFolder.DOCUMENTS_TEMPLATES + "/" +
                            documentType.toString());
            documentTemplateMap.put(documentType, template);
        }

        return template;
    }
}
