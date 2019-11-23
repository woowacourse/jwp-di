package nextstep.di.factory.exception;

public class BeanInstantiationFailException extends RuntimeException {
    private static final String ERROR_MSG = "의존 주입 객체를 생성하는 데 실패했습니다.";

    public BeanInstantiationFailException(Throwable cause) {
        super(ERROR_MSG, cause);
    }
}
