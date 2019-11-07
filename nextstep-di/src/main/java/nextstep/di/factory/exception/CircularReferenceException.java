package nextstep.di.factory.exception;

public class CircularReferenceException extends RuntimeException {
    public CircularReferenceException(final String message) {
        super(message);
    }
}
