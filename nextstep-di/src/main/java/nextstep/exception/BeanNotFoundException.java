package nextstep.exception;

public class BeanNotFoundException extends RuntimeException {
    private static final String BEAN_NOT_FOUND_EXCEPTION = "해당하는 빈을 찾을 수 없습니다.";

    public BeanNotFoundException() {
        super(BEAN_NOT_FOUND_EXCEPTION);
    }
}
