package nextstep.di.factory.exception;

public class InaccessibleConstructorException extends RuntimeException {
    private static final String message = "bean 생성에 맞는 생성자를 찾을 수 없습니다.";

    public InaccessibleConstructorException() {
        super(message);
    }
}
