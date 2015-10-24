package com.softserve.edu.service.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DuplicateRecordException extends Exception {
    public DuplicateRecordException(String format) {
        super(format);
    }
}
