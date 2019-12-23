package nextstep.di.factory.exception;

public class NoDefaultConstructorException extends RuntimeException {
    private static final String ERROR_MSG = "기본 생성자가 없는 객체는 생성할 수 없습니다.";

    public NoDefaultConstructorException(Throwable e) {
        super(ERROR_MSG, e);
    }
}
