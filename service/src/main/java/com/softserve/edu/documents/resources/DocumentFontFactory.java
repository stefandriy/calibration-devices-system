package com.softserve.edu.documents.resources;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;
import com.softserve.edu.documents.parameter.FileSystem;
import com.softserve.edu.documents.utils.FileUtils;
import org.apache.commons.vfs2.FileObject;

import java.io.IOException;
import java.util.EnumMap;

/**
 * Font factory.
 * Contains static methods for building fonts.
 */
public class DocumentFontFactory {
    /**
     * Default private constructor.
     */
    private DocumentFontFactory() {
    }

    private static EnumMap<DocumentFont, BaseFont> baseFontMap =
            new EnumMap<>(DocumentFont.class);

    /**
     * Creates a base font that can be used for creation of a font.
     *
     * @param font to create
     * @return created font
     * @throws IOException if font is invalid or the font's file couldn't be found
     */
    private static BaseFont getBaseFont(DocumentFont font) throws IOException {
        BaseFont baseFontObject = baseFontMap.get(font);

        if (baseFontObject == null) {
            FileObject file = FileUtils.findFile(FileSystem.RES,
                    ResourcesFolder.FONTS + "/" + font.toString());
            String path = file.getURL().getPath();

            try {
                baseFontObject = BaseFont.createFont(path,
                        BaseFont.IDENTITY_H, false);
            } catch (DocumentException exception) {
                exception.printStackTrace();
                throw new IOException("couldn't read font");
            }

            baseFontMap.put(font, baseFontObject);
        }

        return baseFontObject;
    }

    /**
     * Build font with the specified parameters.
     *
     * @param documentFont one of standard fonts
     * @return a font
     * @throws IOException if the requested font can't be found.
     */
    public static Font buildFont(DocumentFont documentFont) throws IOException {
        return createFont(documentFont);
    }

    /**
     * Build font with the specified parameters.
     *
     * @param documentFont one of standard fonts
     * @param size         of font
     * @return a font
     * @throws IOException if the requested font can't be found.
     */
    public static Font buildFont(DocumentFont documentFont, int size)
            throws IOException {
        return createFont(documentFont, size);
    }

    /**
     * Build font with the specified parameters.
     *
     * @param documentFont one of standard fonts
     * @param size         of font
     * @param style        style of font
     * @return a font
     * @throws IOException if the requested font can't be found.
     */
    public static Font buildFont(DocumentFont documentFont, int size, int style)
            throws IOException {
        return createFont(documentFont, size, style);
    }

    /**
     * Creates a font
     *
     * @param font to create
     * @return created font
     * @throws IOException if font is invalid or the font's file couldn't be found
     */
    static private Font createFont(DocumentFont font) throws IOException {
        BaseFont bf = getBaseFont(font);
        return new Font(bf);
    }

    /**
     * Creates a font
     *
     * @param font to create
     * @param size needed font size
     * @return created font
     * @throws IOException if font is invalid or the font's file couldn't be found
     */
    static private Font createFont(DocumentFont font, int size) throws IOException {
        BaseFont bf = getBaseFont(font);
        return new Font(bf, size);
    }

    /**
     * Creates a font
     *
     * @param font  to create
     * @param size  needed font size
     * @param style needed font style
     * @return created font
     * @throws IOException if font is invalid or the font's file couldn't be found
     */
    static private Font createFont(DocumentFont font, int size, int style)
            throws IOException {
        BaseFont bf = getBaseFont(font);
        return new Font(bf, size, style);
    }
}
