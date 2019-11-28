package nextstep.exception;

public class BeanCreateFailException extends RuntimeException {
    private static final String BEAN_CREATE_FAIL_MESSAGE = "빈 생성에 실패했습니다.";

    public BeanCreateFailException() {
        super(BEAN_CREATE_FAIL_MESSAGE);
    }
}
