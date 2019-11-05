package nextstep.exception;

public class ImplementationNotFoundException extends RuntimeException {
    private static final String IMPLEMENTATION_NOT_FOUND_MESSAGE = "구현체를 찾을 수 없습니다.";

    public ImplementationNotFoundException() {
        super(IMPLEMENTATION_NOT_FOUND_MESSAGE);
    }
}
