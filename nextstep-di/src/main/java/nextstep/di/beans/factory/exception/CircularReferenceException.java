package nextstep.di.beans.factory.exception;

public class CircularReferenceException extends RuntimeException {
    public CircularReferenceException(final String message) {
        super(message);
    }
}
