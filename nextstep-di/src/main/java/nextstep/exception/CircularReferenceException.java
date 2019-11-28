package nextstep.exception;

public class CircularReferenceException extends RuntimeException {
    public static final String CIRCULAR_REFERENCE_MESSAGE = "순환 참조입니다.";

    public CircularReferenceException() {
        super(CIRCULAR_REFERENCE_MESSAGE);
    }
}
