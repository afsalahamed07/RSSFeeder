package errors;

public class UserAlreadyExistError extends RuntimeException {
    public UserAlreadyExistError( String message ) {
        super( message );
    }
}
