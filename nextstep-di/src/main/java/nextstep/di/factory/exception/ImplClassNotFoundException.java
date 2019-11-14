package nextstep.di.factory.exception;

public class ImplClassNotFoundException extends RuntimeException {
    public static final String ERROR_MSG = "인터페이스를 구현하는 클래스를 찾지 못했습니다.";

    public ImplClassNotFoundException() {
        super(ERROR_MSG);
    }
}
