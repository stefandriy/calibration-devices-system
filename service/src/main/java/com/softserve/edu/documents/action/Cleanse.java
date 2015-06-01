package com.softserve.edu.documents.action;

import com.softserve.edu.documents.parameter.FileParameters;
import com.softserve.edu.documents.utils.RegEx;
import org.apache.commons.vfs2.FileContent;
import org.apache.commons.vfs2.FileObject;
import org.apache.log4j.Logger;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Singleton.
 * Represents an operation that can be done on a document.
 */
public enum Cleanse implements Operation {
    INSTANCE;

    private static Logger logger = Logger.getLogger(Cleanse.class);

    /**
     * Cleans formatting data from template that is left in the document.
     *
     * @param sourceFile file to perform the operation on
     * @param fileParameters parameters that specify how the file
     *                       must be generated
     * @return file without formatting tokens
     * @throws IOException
     */
    @Override
    public FileObject perform(FileObject sourceFile,
                              FileParameters fileParameters) throws IOException {
        XWPFDocument templateDocument;

        FileContent sourceFileContent = sourceFile.getContent();
        try (InputStream inputStream = sourceFileContent.getInputStream()) {
            templateDocument = new XWPFDocument(inputStream);
        } catch (IOException exception) {
            logger.error("exception while trying to open stream to " +
                    sourceFile.getName().toString(), exception);
            throw exception;
        }

        XWPFDocument newDocument = new XWPFDocument(templateDocument.getPackage());

        List<XWPFParagraph> paragraphs = newDocument.getParagraphs();

        List<XWPFParagraph> paragraphList = paragraphs.
                stream().
                filter(paragraph -> !paragraph.getParagraphText().isEmpty()).
                collect(Collectors.toList());

        for (XWPFParagraph paragraph : paragraphList) {
            cleansText(paragraph);
        }

        try (OutputStream stream = sourceFileContent.getOutputStream()) {
            newDocument.write(stream);
        }

        return sourceFile;
    }

    /**
     * Cleanse the supplied paragraph of any formatting data used by this
     * application.
     *
     * @param sourceParagraph paragraph to copy runs from
     */
    private void cleansText(XWPFParagraph sourceParagraph) throws IOException {
        int textPosition = 0;

        List<XWPFRun> runs = sourceParagraph.getRuns();

        String regex = RegEx.findAllFormattingTokens();

        for (XWPFRun sourceRun : runs) {
            String textInRun = sourceRun.getText(textPosition);

            if (textInRun == null || textInRun.isEmpty()) {
                continue;
            }

            textInRun = textInRun.replaceAll(regex, "");

            sourceRun.setText(textInRun, textPosition);
        }
    }
}
