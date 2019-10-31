package nextstep.di.factory.exception;

public class NoSuchDefaultConstructorException extends RuntimeException {
    public NoSuchDefaultConstructorException(String message) {
        super(message);
    }
}
