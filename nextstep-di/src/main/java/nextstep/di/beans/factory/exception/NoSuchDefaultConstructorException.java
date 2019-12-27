package nextstep.di.beans.factory.exception;

public class NoSuchDefaultConstructorException extends RuntimeException {

    public NoSuchDefaultConstructorException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
