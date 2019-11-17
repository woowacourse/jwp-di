package nextstep.di.exception;

public class NotFoundConstructorException extends RuntimeException {
    public NotFoundConstructorException(String message) {
        super(message);
    }
}
