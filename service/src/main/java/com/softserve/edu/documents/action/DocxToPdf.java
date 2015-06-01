package com.softserve.edu.documents.action;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import com.softserve.edu.documents.parameter.DocumentFont;
import com.softserve.edu.documents.parameter.DocumentFontFactory;
import com.softserve.edu.documents.parameter.FileParameters;
import com.softserve.edu.documents.utils.FileLocator;
import org.apache.commons.vfs2.FileObject;
import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Singleton.
 * Represents an operation that can be done on a document.
 */
public enum DocxToPdf implements Operation {
    INSTANCE;

    private static Logger logger = Logger.getLogger(DocxToPdf.class);

    /**
     * Transform a docx file to pdf format.
     *
     * @param sourceFile     a docx file to transform to pdf
     * @param fileParameters parameters that specify how the file
     *                       must be generated
     * @return generated pdf file
     * @throws IOException
     */
    @Override
    public FileObject perform(FileObject sourceFile,
                              FileParameters fileParameters) throws IOException {
        FileObject filePdf = FileLocator.getFile(fileParameters.getFileSystem(),
                fileParameters.getFileName() + "_pdf");

        createPdfFile(sourceFile, filePdf);

        return filePdf;
    }

    /**
     * Create a pdf document.
     *
     * @param docxFile source docx file
     * @param pdfFile target pdf file
     * @throws IOException
     */
    public void createPdfFile(FileObject docxFile,
                              FileObject pdfFile) throws IOException {
        Document resultPdfDocument;
        PdfWriter writer;

        resultPdfDocument = new Document();

        try (OutputStream outputStream = pdfFile.getContent().getOutputStream()) {
            writer = PdfWriter.getInstance(resultPdfDocument, outputStream);
        } catch (DocumentException exception) {
            resultPdfDocument.close();
            logger.error("exception while opening an input stream to the " +
                    pdfFile.getName().toString(), exception);
            throw new IOException("The output file couldn't be reached.");
        }

        resultPdfDocument.open();
        writer.setPageEmpty(true);
        resultPdfDocument.newPage();
        writer.setPageEmpty(true);

        InputStream inputStream = docxFile.getContent().getInputStream();
        XWPFDocument doc = new XWPFDocument(inputStream);
        inputStream.close();


        copyParagraphs(doc, resultPdfDocument);

        resultPdfDocument.close();
    }

    /**
     * Copy paragraphs from the source docx document to the target document.
     *
     * @param sourceDocxDocument source document
     * @param targetPdfDocument  target document
     * @throws IOException
     */
    private void copyParagraphs(XWPFDocument sourceDocxDocument,
                                Document targetPdfDocument) throws IOException {
        java.util.List<XWPFParagraph> docxParagraphs =
                sourceDocxDocument.getParagraphs();
        Paragraph pdfParagraph;

        try {
            for (XWPFParagraph docxParagraph : docxParagraphs) {
                pdfParagraph = createParagraph(docxParagraph);
                targetPdfDocument.add(pdfParagraph);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("The output file couldn't be reached.");
        }
    }

    /**
     * Creates a pdf paragraph.
     *
     * @param docxParagraph MSDocx paragraph with text for the new pdf paragraph
     * @return created header paragraph
     * @throws IOException if font file is invalid
     */
    public Paragraph createParagraph(XWPFParagraph docxParagraph)
            throws IOException, InvalidFormatException {
        String text = docxParagraph.getText();

        XWPFRun run = docxParagraph.getRuns().get(0);

        int fontSize = run.getFontSize();
        int align = docxParagraph.getAlignment() == ParagraphAlignment.CENTER ?
                Element.ALIGN_CENTER : Element.ALIGN_LEFT;
        int style = !run.isBold() ? 0 : Font.BOLD;
        style = !run.isItalic() ? style : Font.ITALIC;

        Font font = DocumentFontFactory.buildFont(DocumentFont.FREE_SERIF,
                fontSize, style);

        Paragraph paragraph = new Paragraph(text, font);
        paragraph.setAlignment(align);

        return paragraph;
    }
}
