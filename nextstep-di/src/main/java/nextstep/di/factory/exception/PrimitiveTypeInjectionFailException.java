package nextstep.di.factory.exception;

public class PrimitiveTypeInjectionFailException extends RuntimeException {
    private static final String ERROR_MSG = "기본형을 주입할 수 없습니다.";

    public PrimitiveTypeInjectionFailException() {
        super(ERROR_MSG);
    }
}
