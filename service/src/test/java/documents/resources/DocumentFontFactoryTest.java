package documents.resources;

import com.softserve.edu.documents.resources.DocumentFont;
import com.softserve.edu.documents.resources.DocumentFontFactory;
import org.junit.Test;

import java.io.IOException;

/**
 * Tests for the font factory.
 */
public class DocumentFontFactoryTest {
    @Test(expected = IllegalArgumentException.class)
    public void passNullAsDocumentFont() throws IOException {
        DocumentFontFactory.INSTANCE.buildFont(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void passNegativeSize() throws IOException {
        DocumentFontFactory.INSTANCE.buildFont(DocumentFont.FREE_SERIF, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void passNegativeStyle() throws IOException {
        DocumentFontFactory.INSTANCE.buildFont(DocumentFont.FREE_SERIF, 1, -1);
    }
}
