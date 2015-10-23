package com.softserve.edu.service.storage.impl;

import com.softserve.edu.service.calibrator.impl.CalibratorPlaningTaskServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;

import java.io.IOException;
import java.net.URI;

import static org.junit.Assert.*;

/**
 * Created by Yurko on 23.10.2015.
 */
@RunWith(MockitoJUnitRunner.class)
@PrepareForTest(CalibratorPlaningTaskServiceImpl.class)
public class FileOperationImplTest {

    @InjectMocks
    FileOperationImpl fileOperationImpl = new FileOperationImpl();

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