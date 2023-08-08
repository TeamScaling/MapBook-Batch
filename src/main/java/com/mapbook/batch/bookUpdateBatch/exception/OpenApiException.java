package com.mapbook.batch.bookUpdateBatch.exception;

public class OpenApiException extends RuntimeException{

    public OpenApiException(String message) {
        super(message);
    }

    public OpenApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
