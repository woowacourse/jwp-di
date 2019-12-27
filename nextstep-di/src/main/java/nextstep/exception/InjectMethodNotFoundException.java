package nextstep.exception;

public class InjectMethodNotFoundException extends RuntimeException {
    private static final String INJECT_METHOD_NOT_FOUND_MESSAGE = "inject 메소드를 찾을 수 없습니다.";

    public InjectMethodNotFoundException() {
        super(INJECT_METHOD_NOT_FOUND_MESSAGE);
    }
}
