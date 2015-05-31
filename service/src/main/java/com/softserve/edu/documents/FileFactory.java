package com.softserve.edu.documents;

import com.softserve.edu.documents.action.*;
import com.softserve.edu.documents.chain.ActionChain;
import com.softserve.edu.documents.parameter.FileParameters;
import org.apache.commons.vfs2.FileObject;

import java.util.ArrayList;
import java.util.List;

public class FileFactory {
    public static FileObject buildFile(FileParameters fileParameters) {
        List<Operation> operations = new ArrayList<>();

        switch (fileParameters.getDocumentFormat()) {
            case DOCX:
                operations.add(LoadTemplate.getInstance());
                operations.add(new Normalize());
                operations.add(new InsertText());
                operations.add(FormatText.INSTANCE);
                operations.add(new Cleanse());
                break;
            case PDF:
                operations.add(LoadTemplate.getInstance());
                operations.add(new Normalize());
                operations.add(new InsertText());
                operations.add(FormatText.INSTANCE);
                operations.add(new Cleanse());
                operations.add(new TransformToPdf());
                break;
        }

        return ActionChain.processChain(fileParameters, operations);
    }
}
