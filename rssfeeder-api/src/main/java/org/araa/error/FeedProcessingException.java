package org.araa.error;

public class FeedProcessingException extends RuntimeException{
    public FeedProcessingException(String message) {
        super(message);
    }

    public FeedProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
