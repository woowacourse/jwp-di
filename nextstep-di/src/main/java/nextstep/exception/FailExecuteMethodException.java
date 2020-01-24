package nextstep.exception;

public class FailExecuteMethodException extends RuntimeException {
    private static final String FAIL_EXECUTE_METHOD_MESSAGE = "메서드 실행 중 예외가 발생했습니다. : %s";

    public FailExecuteMethodException(Exception e) {
        super(String.format(FAIL_EXECUTE_METHOD_MESSAGE, e.getMessage()));
    }
}
