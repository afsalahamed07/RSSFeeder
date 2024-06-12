package org.araa.error;

public class UserAlreadyExistError extends RuntimeException {
    public UserAlreadyExistError( String message ) {
        super( message );
    }
}
