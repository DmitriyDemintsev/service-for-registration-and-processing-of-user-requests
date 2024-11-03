package com.example.task_for_VitaSoft.exception;

public class ApplicationNotFoundException extends RuntimeException {

    public ApplicationNotFoundException(final String message) {
        super(message);
    }
}
