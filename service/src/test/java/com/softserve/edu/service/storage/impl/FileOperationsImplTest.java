package com.softserve.edu.service.storage.impl;

import org.junit.Test;
import org.mockito.InjectMocks;
import java.net.URI;

import static org.junit.Assert.*;

/**
 * Created by Yurij Dvornyk on 21.10.2015.
 */
public class FileOperationsImplTest {

    @InjectMocks
    FileOperationsImpl fileOperationImpl = new FileOperationsImpl();

    @Test
    public void testPutResourse() throws Exception {
        boolean actual;
        try {
            fileOperationImpl.putResourse(null, "abc", "file.bbi");
            actual = true;
        } catch (Exception ex) {
            actual = false;
        }
        assertEquals(false, actual);
    }

    @Test
    public void testGetResourseURI() throws Exception {
        String relativeFilePath = "/rel.path";
        URI actual = fileOperationImpl.getResourseURI(relativeFilePath);
        assertEquals("null/rel.path", actual.toString());
    }
}