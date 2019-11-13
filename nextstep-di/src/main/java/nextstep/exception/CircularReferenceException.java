package nextstep.exception;

public class CircularReferenceException extends RuntimeException {
    private static final String CIRCULAR_REFERENCE_MESSAGE = "순환참조는 허용하지 않습니다.";

    public CircularReferenceException() {
        super(CIRCULAR_REFERENCE_MESSAGE);
    }
}
