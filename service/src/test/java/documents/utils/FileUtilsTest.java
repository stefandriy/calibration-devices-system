package documents.utils;

import com.softserve.edu.documents.parameter.FileSystem;
import com.softserve.edu.documents.utils.FileUtils;
import org.apache.commons.vfs2.FileObject;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileNotFoundException;

/**
 * Tests for file utilities.
 */
public class FileUtilsTest {
    @Test(expected = FileNotFoundException.class)
    public void getNonExistingFileTest() throws FileNotFoundException {
        FileUtils.findFile(FileSystem.RAM, "test_file");
    }

    @Test
    public void getExistingFile() throws FileNotFoundException {
        String testFile = "testfile";
        FileUtils.createFile(FileSystem.RAM, testFile);

        FileObject foundFile = FileUtils.findFile(FileSystem.RAM, testFile);
        Assert.assertNotNull(foundFile);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createFilePassNullForFileSystem() throws FileNotFoundException {
        String testFile = "testfile";
        FileUtils.createFile(null, testFile);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createFilePassNullForFileName() throws FileNotFoundException {
        FileUtils.createFile(FileSystem.RAM, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createFilePassEmptyFilename() throws FileNotFoundException {
        FileUtils.createFile(FileSystem.RAM, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void findFilePassNullForFileSystem() throws FileNotFoundException {
        String testFile = "testfile";
        FileUtils.createFile(null, testFile);
    }

    @Test(expected = IllegalArgumentException.class)
    public void findFilePassNullForFileName() throws FileNotFoundException {
        FileUtils.createFile(FileSystem.RAM, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void findFilePassEmptyFilename() throws FileNotFoundException {
        FileUtils.createFile(FileSystem.RAM, null);
    }


}
