package nextstep.di.factory.exception;

public class NotFoundConstructorException extends RuntimeException {
    public NotFoundConstructorException() {
        super("생성자를 찾을 수 없습니다.");
    }
}
