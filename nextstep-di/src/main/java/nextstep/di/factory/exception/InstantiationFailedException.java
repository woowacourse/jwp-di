package nextstep.di.factory.exception;

public class InstantiationFailedException extends RuntimeException {
    public InstantiationFailedException(final String message) {
        super(message);
    }
}
