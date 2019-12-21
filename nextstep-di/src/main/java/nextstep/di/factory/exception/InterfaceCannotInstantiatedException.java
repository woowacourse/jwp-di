package nextstep.di.factory.exception;

public class InterfaceCannotInstantiatedException extends RuntimeException {
    private static final String ERROR_MSG = "인터페이스는 빈으로 등록할 수 없습니다.";

    public InterfaceCannotInstantiatedException() {
        super(ERROR_MSG);
    }
}
