package com.softserve.edu.documents.action;

import com.lowagie.text.Chunk;
import com.lowagie.text.Font;
import com.softserve.edu.documents.parameter.DocumentFont;
import com.softserve.edu.documents.parameter.DocumentFontFactory;
import com.softserve.edu.documents.parameter.FileParameters;
import com.softserve.edu.documents.size.SizeUnit;
import com.softserve.edu.documents.size.SizeUnitConverter;
import org.apache.commons.vfs2.FileContent;
import org.apache.commons.vfs2.FileObject;
import org.apache.log4j.Logger;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageMar;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageSz;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Singleton.
 * Represents an operation that reads file, finds tokens that represent
 * formatting rules and adjusts text accordingly.
 */
public enum FormatText implements Operation {
    INSTANCE;

    private Logger logger = Logger.getLogger(FormatText.class);

    /**
     * {inherit}
     */
    @Override
    public FileObject perform(FileObject sourceFile,
                              FileParameters fileParameters) throws IOException {
        XWPFDocument templateDocument;
        FileContent sourceFileContent = sourceFile.getContent();

        try (InputStream inputStream = sourceFileContent.getInputStream()) {
            templateDocument = new XWPFDocument(inputStream);
        } catch (IOException exception) {
            logger.error("exception: ", exception);
            throw exception;
        }

        XWPFDocument newDocument =
                new XWPFDocument(templateDocument.getPackage());

        List<XWPFParagraph> paragraphs = newDocument.getParagraphs();

        List<XWPFParagraph> paragraphList = paragraphs.
                stream().
                filter(paragraph -> !paragraph.getParagraphText().isEmpty()).
                collect(Collectors.toList());

        double contentWidth = getContentWidth(newDocument);
        contentWidth = SizeUnitConverter.convert(contentWidth, SizeUnit.TWIP,
                SizeUnit.PT);

        for (XWPFParagraph paragraph : paragraphList) {
            setCorrectText(paragraph, contentWidth);
        }

        try (OutputStream outputStream = sourceFileContent.getOutputStream()) {
            newDocument.write(outputStream);
        }

        return sourceFile;
    }

    /**
     * Set the correct data for each run in the supplied paragraph
     *
     * @param sourceParagraph paragraph to copy runs from
     */
    private void setCorrectText(XWPFParagraph sourceParagraph,
                                double contentWidth) throws IOException {
        final int textPosition = 0;

        List<XWPFRun> runs = sourceParagraph.getRuns();

        // format every run
        for (XWPFRun sourceRun : runs) {
            String textInRun = sourceRun.getText(textPosition);

            if (textInRun == null || textInRun.isEmpty()) {
                continue;
            }

            Font font = DocumentFontFactory.buildFont(DocumentFont.FREE_SERIF,
                    sourceRun.getFontSize());

            int indexOfRight = textInRun.lastIndexOf("#");

            if (indexOfRight != -1) {
                textInRun = align(new StringBuilder(textInRun), font,
                        contentWidth);
            }

            sourceRun.setText(textInRun, textPosition);
        }
    }

    private String align(StringBuilder textInRun, Font font,
                         Double contentWidth) {
        Double paragraphWidth = getStringWidth(textInRun.toString(), font);
        int index = textInRun.lastIndexOf("#");

        final double epsilon = 0.0001;
        while ((paragraphWidth - contentWidth) < epsilon) {
            textInRun.insert(index, ' ');
            paragraphWidth = getStringWidth(textInRun.toString(), font);
        }

        return textInRun.toString();
    }

    public double getStringWidth(String string, Font font) {
        Chunk chunk = new Chunk(string, font);
        return chunk.getWidthPoint();
    }

    public double getContentWidth(XWPFDocument document) {
        CTBody body = document.getDocument().getBody();
        CTSectPr docSp = body.getSectPr();

        CTSectPr sectPr = document.getDocument().getBody().getSectPr();
        if (sectPr == null) return -1;
        CTPageSz pageSize = sectPr.getPgSz();

        CTPageMar margin = docSp.getPgMar();

        BigInteger pageWidth = pageSize.getW();
        pageWidth = pageWidth.add(BigInteger.ONE);
        BigInteger totalMargins = margin.getLeft().add(margin.getRight());
        BigInteger contentWidth = pageWidth.subtract(totalMargins);

        return contentWidth.doubleValue();
    }
}
