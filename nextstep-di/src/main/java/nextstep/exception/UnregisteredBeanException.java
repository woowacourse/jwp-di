package nextstep.exception;

public class UnregisteredBeanException extends RuntimeException {
    private static final String UN_REGISTERED_BEAN_MESSAGE = "등록되지 않은 빈입니다.";

    public UnregisteredBeanException() {
        super(UN_REGISTERED_BEAN_MESSAGE);
    }
}
