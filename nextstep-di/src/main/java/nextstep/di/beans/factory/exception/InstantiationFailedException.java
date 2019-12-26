package nextstep.di.beans.factory.exception;

public class InstantiationFailedException extends RuntimeException {

    public InstantiationFailedException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
