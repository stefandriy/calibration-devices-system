package com.softserve.edu.documents.action;

import com.lowagie.text.*;
import com.softserve.edu.documents.parameter.FileParameters;
import com.softserve.edu.documents.resources.DocumentFont;
import com.softserve.edu.documents.resources.DocumentFontFactory;
import com.softserve.edu.documents.utils.FileUtils;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.log4j.Logger;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import com.lowagie.text.html.HtmlWriter;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by cam on 28.11.15.
 */
public enum DocxToHTML implements Operation {
    INSTANCE;

    private static Logger logger = Logger.getLogger(DocxToHTML.class);

    @Override
    public FileObject perform(FileObject sourceFile, FileParameters fileParameters) throws IOException {
        FileObject fileHtml = FileUtils.createFile(fileParameters.getFileSystem(),
                fileParameters.getFileName() + "_html");

        createHTMLFile(sourceFile, fileHtml);
        return fileHtml;
    }

    private void createHTMLFile(FileObject docxFile, FileObject fileHtml) throws IOException {
        Document resultHTMLDocument;
        HtmlWriter writer;

        resultHTMLDocument = new Document();
        System.err.println("pdfFile is null" + fileHtml == null);
        OutputStream outputStream = fileHtml.getContent().getOutputStream();

        writer = HtmlWriter.getInstance(resultHTMLDocument, outputStream);

        resultHTMLDocument.open();
        resultHTMLDocument.newPage();

        XWPFDocument docx;
        try (InputStream inputStream = docxFile.getContent().getInputStream()) {
            docx = new XWPFDocument(inputStream);
        } catch (FileSystemException exception) {
            logger.error("can't open an input stream to " +
                    docxFile.getName().toString(), exception);
            throw exception;
        }

        copyParagraphs(docx, resultHTMLDocument);
        resultHTMLDocument.close();
        outputStream.close();
    }

    private void copyParagraphs(XWPFDocument docx, Document resultHTMLDocument) throws IOException {
        java.util.List<XWPFParagraph> docxParagraphs =
                docx.getParagraphs();
        Paragraph pdfParagraph;

        for (XWPFParagraph docxParagraph : docxParagraphs) {
            try {
                pdfParagraph = createParagraph(docxParagraph);
                resultHTMLDocument.add(pdfParagraph);
            } catch (Exception e) {
                logger.error("exception while trying to copy docx paragraph " +
                        docxParagraph.toString() + ": ");
                logger.error(e); // for prevent critical issue "Either log or rethrow this exception"
                throw new IOException("The output file couldn't be reached.");
            }
        }
    }

    private Paragraph createParagraph(XWPFParagraph docxParagraph) throws IOException {
        String text = docxParagraph.getText();
        XWPFRun run = docxParagraph.getRuns().get(0);

        int fontSize = run.getFontSize();
        if (fontSize < 1) {
            fontSize = 12;
        }
        int align;
        switch (docxParagraph.getAlignment()) {
            case LEFT : align = Element.ALIGN_LEFT;
                break;
            case CENTER : align = Element.ALIGN_CENTER;
                break;
            case RIGHT : align = Element.ALIGN_RIGHT;
                break;
            default : align = Element.ALIGN_LEFT;
                break;
        }
        int style = !run.isBold() ? 0 : Font.BOLD;
        style = !run.isItalic() ? style : Font.ITALIC;

        Font font = DocumentFontFactory.INSTANCE.buildFont(DocumentFont.FREE_SERIF,
                fontSize, style);

        Paragraph paragraph = new Paragraph(text, font);
        paragraph.setAlignment(align);

        return paragraph;
    }
}
