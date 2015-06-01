package com.softserve.edu.documents.parameter;

import com.softserve.edu.documents.document.Document;
import com.softserve.edu.documents.resources.DocumentType;

/**
 * Contains information about a file that is to be generated.
 * It is meant to be used outside of the documents package to provide info
 * about the needed file.
 */
public class FileParameters {
    private Document document;
    private DocumentType documentType;
    private FileFormat fileFormat;
    private FileSystem fileSystem;
    private String fileName;

    public FileParameters(Document document, DocumentType documentType,
                          FileFormat fileFormat) {
        this.document = document;
        this.documentType = documentType;
        this.fileFormat = fileFormat;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public FileFormat getFileFormat() {
        return fileFormat;
    }

    public void setFileFormat(FileFormat fileFormat) {
        this.fileFormat = fileFormat;
    }

    public FileSystem getFileSystem() {
        return fileSystem;
    }

    public void setFileSystem(FileSystem fileSystem) {
        this.fileSystem = fileSystem;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }
}
