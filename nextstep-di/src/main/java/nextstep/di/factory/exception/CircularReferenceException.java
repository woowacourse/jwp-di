package nextstep.di.factory.exception;

public class CircularReferenceException extends RuntimeException {
    private static final String ERROR_MESSAGE = "순환 참조 발생";

    public CircularReferenceException() {
        super(ERROR_MESSAGE);
    }
}
