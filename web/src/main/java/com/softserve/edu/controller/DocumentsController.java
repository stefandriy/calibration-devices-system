package com.softserve.edu.controller;

import com.softserve.edu.documents.parameter.DocumentFormat;
import com.softserve.edu.documents.parameter.DocumentType;
import com.softserve.edu.service.DocumentsService;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Controller for file generation requests.
 * Generates a requested file and sends it to the caller, in case of an error
 * returns one of the http statuses that signals an error.
 * All exceptions are handled by the @ExceptionHandler methods.
 */
@RestController
@RequestMapping(value = "/doc")
public class DocumentsController {
    static Logger log = Logger.getLogger(DocumentsController.class.getName());

    @Autowired
    DocumentsService documentsService;

    /**
     * Returns a document with a specific documentFormat using verification and one of it's tests.
     * For example: .../verification_certificate/1/1/pdf.
     *
     * @param documentType     document to generate
     * @param verificationCode id of the verification, for which the document is to be generated
     * @param testID           one of the verification's tests, for which the document is to be generated
     * @param documentFormat   documentFormat of the resulting document
     * @throws IOException           if file can't be generated because of a file system error
     * @throws IllegalStateException if one of parameters is incorrect
     */
    @RequestMapping(value = "{documentType}/{verificationCode}/{testID}/{documentFormat}", method = RequestMethod.GET)
    public void getDocument(HttpServletResponse response,
                            @PathVariable DocumentType documentType,
                            @PathVariable String verificationCode,
                            @PathVariable Long testID,
                            @PathVariable DocumentFormat documentFormat)
            throws IOException, IllegalStateException {
        InputStream inputStream = documentsService.getFile(verificationCode, testID, documentType, documentFormat);
        sendFile(response, documentFormat, inputStream);
        response.getOutputStream().close();
        inputStream.close();
    }

    /**
     * Returns a document with a specific documentFormat using verification that has only one test.
     * For example: .../verification_certificate/1/pdf.
     *
     * @param documentType     document to generate
     * @param verificationCode id of the verification, for which the document is to be generated. This verification
     *                         must have only one test
     * @param documentFormat   documentFormat of the resulting document
     * @throws IOException           if file can't be generated because of a file system error
     * @throws IllegalStateException if one of parameters is incorrect
     */
    @RequestMapping(value = "{documentType}/{verificationCode}/{documentFormat}", method = RequestMethod.GET)
    public void getDocument(HttpServletResponse response,
                            @PathVariable DocumentType documentType,
                            @PathVariable String verificationCode,
                            @PathVariable DocumentFormat documentFormat)
            throws IOException, IllegalStateException {
        InputStream inputStream = documentsService.getFile(verificationCode, documentType, documentFormat);
        sendFile(response, documentFormat, inputStream);
        response.getOutputStream().close();
        inputStream.close();
    }

    /**
     * Writes contents of the input stream to the response' output stream and sets http headers depending on
     * the documentFormat.
     * Doesn't close the streams.
     *
     * @param response       servlet response
     * @param documentFormat of the file to be sent
     * @param inputStream    input steam to the data that must be written to the output stream
     * @throws IOException
     */
    private void sendFile(HttpServletResponse response, DocumentFormat documentFormat, InputStream inputStream) throws IOException {
        setContentType(response, documentFormat);
        writeToOutputStream(inputStream, response.getOutputStream());
    }

    /**
     * Set content type of the response depending on the document's format.
     *
     * @param response       servlet response
     * @param documentFormat of the file to be sent
     */
    private void setContentType(HttpServletResponse response, DocumentFormat documentFormat) {
        switch (documentFormat) {
            case PDF:
                response.setContentType("application/pdf");
                break;
            case DOCX:
                response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
                break;
        }

        response.setHeader("Content-Disposition", "attachment; filename=\"myfile." +
                documentFormat.name().toLowerCase() +
                "\"");
        //request.setContentLength(bytes.length);
    }

    /**
     * Writes contents of the input stream to the output stream
     *
     * @param inputStream  to the data to be written
     * @param outputStream to write data to
     * @throws IOException if one of streams is unreachable
     */
    private void writeToOutputStream(InputStream inputStream,
                                     OutputStream outputStream) throws IOException {
        final int bufferSize = 10240;
        byte[] buffer = new byte[bufferSize];

        int length = inputStream.read(buffer);

        do {
            outputStream.write(buffer, 0, length);
            length = inputStream.read(buffer);
        } while (length > 0);

        inputStream.close();
    }

    /**
     * In case of a illegal state of a path parameter logs exception and
     * sends http status NOT_FOUND to the client.
     *
     * @param exception thrown exception
     */
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalStateException.class)
    public void illegalStateExceptionHandler(IllegalStateException exception) {
        log.error("exception: ", exception);
    }

    /**
     * In case of an file system logs exception and
     * sends http status NOT_FOUND to the client.
     *
     * @param exception thrown exception
     */
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(IOException.class)
    public void ioExceptionHandler(IOException exception) {
        log.error("exception: ", exception);
    }

    /**
     * In case of an uncaught exception logs exception and
     * sends http status INTERNAL_SERVER_ERROR to the client.
     *
     * @param exception thrown exception
     */
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public void uncaughtExceptionHandler(Exception exception) {
        log.error("exception: ", exception);
    }

    /**
     * {inherit}
     */
    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        // register custom editor for the DocumentFormat enum
        dataBinder.registerCustomEditor(DocumentFormat.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                String capitalize = text.toUpperCase();
                DocumentFormat documentFormat = DocumentFormat.valueOf(capitalize);
                setValue(documentFormat);
            }
        });

        // register custom editor for the DocumentType enum
        dataBinder.registerCustomEditor(DocumentType.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                String capitalize = text.toUpperCase();
                DocumentType documentType = DocumentType.valueOf(capitalize);
                setValue(documentType);
            }
        });
    }
}
