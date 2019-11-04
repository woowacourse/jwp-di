package nextstep.di.factory;

public class RecursiveFieldException extends RuntimeException {
    private static final String ERROR_MSG = "의존 객체가 순환 참조되어 생성에 실패했습니다.";

    public RecursiveFieldException() {
        super(ERROR_MSG);
    }
}
