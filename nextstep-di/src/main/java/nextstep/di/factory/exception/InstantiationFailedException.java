package nextstep.di.factory.exception;

public class InstantiationFailedException extends RuntimeException {

    public InstantiationFailedException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
